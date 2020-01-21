package com.nexmore.rnd.batch.config;

import com.google.common.collect.Maps;
import com.nexmore.rnd.batch.domain.BatchInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 배치 실행 구성
 */
@Slf4j
@Component
public class BatchExecutionConfigure {

    private final ApplicationContext context;

    /**
     * 배치 실행 속성
     */
    @Value("${batch.execution.properties:NOT_FOUND}")
    private String propertiesPath;

    /** 배치 정보 */
    private Map<String, BatchInfo> currentBatchInfos = Maps.newHashMap();

    /** 작업 스케쥴러 */
    private Map<String, ThreadPoolTaskScheduler> taskSchedulers = Maps.newHashMap();

    @Autowired
    public BatchExecutionConfigure(ApplicationContext context) {
        this.context = context;
    }


    @PostConstruct
    public void init() {
        if(StringUtils.equalsIgnoreCase(propertiesPath, "NOT_FOUND")) {
            log.warn("## NOT FOUND PROPERTIES OF BATCH EXECUTION.");
            return;
        } else {
            log.info("propertiesPath : " + propertiesPath);
        }

        this.currentBatchInfos = loadConfig();

        for(Entry<String, BatchInfo> entry : currentBatchInfos.entrySet()) {
            if(BooleanUtils.isTrue(entry.getValue().getActived())) {
                createAndStart(entry.getValue());
            }

            log.info("## LOADED CONFIG : STATUS {}, VALUE : {}", entry.getValue().getActived(), entry.getValue());
        }

        /* 스케쥴 실행 서비스 */
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleWithFixedDelay(() -> {
            try {
                reload();
            } catch(Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }, 10, 1, TimeUnit.SECONDS);
    }





    private void reload() {
        Map<String, BatchInfo> reloadedBatchInfos = loadConfig();

        for(Entry<String, BatchInfo> entry : reloadedBatchInfos.entrySet()) {
            //새로 추가된 배치인 경우,
            if(!currentBatchInfos.containsKey(entry.getKey())) {
                if(BooleanUtils.isTrue(entry.getValue().getActived())) {
                    createAndStart(entry.getValue());
                }

                log.info("## RELOADED NEW_CONFIG : STATUS {}, TO {}", entry.getValue().getActived(), entry.getValue());

                continue;
            }
            //설정정보가 상이한 경우
            if(!Objects.equals(entry.getValue(), currentBatchInfos.get(entry.getKey()))) {
                this.stop(entry.getValue().getId());

                if(BooleanUtils.isTrue(entry.getValue().getActived())) {
                    createAndStart(entry.getValue());
                }

                log.info("## RELOADED CHANGED_CONFIG : STATUS {}, FROM {}, TO {}", entry.getValue().getActived(), currentBatchInfos.get(entry.getKey()), entry.getValue());
            }
        }

        this.currentBatchInfos = reloadedBatchInfos;
    }



    private void stop(String key) {
        if(taskSchedulers.containsKey(key)) {
            ThreadPoolTaskScheduler threadPoolTaskScheduler = taskSchedulers.get(key);
            taskSchedulers.remove(key);

            threadPoolTaskScheduler.destroy();
        }
    }



    @SuppressWarnings("ConstantConditions")
    private Map<String, BatchInfo> loadConfig() {
        Map<String, BatchInfo> batchInfos = Maps.newHashMap();

        Properties properties = null;
        InputStream is = null;

        try {
            properties = new Properties();
            URL url = ResourceUtils.getURL(this.propertiesPath);
            is = url.openStream();
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

        @SuppressWarnings("unchecked")
        List<String> keys = (List<String>) Collections.list(properties.propertyNames());


        for(String key : keys) {
            String id = StringUtils.substringBetween(key, "batch.execution.", ".");
            BatchInfo batchInfo = new BatchInfo();

            batchInfo.setId(id);

            batchInfos.put(id, batchInfo);
        }

        for(String key : keys) {
            String value = properties.getProperty(String.valueOf(key));
            String id = StringUtils.substringBetween(key, "batch.execution.", ".");

            BatchInfo batchInfo = batchInfos.get(id);

            if(StringUtils.endsWith(key, "bean")) {
                batchInfo.setBean(StringUtils.trim(value));

            } else if(StringUtils.endsWith(key, "method")) {
                batchInfo.setMethod(StringUtils.trim(value));

            } else if(StringUtils.endsWith(key, "actived")) {
                batchInfo.setActived(StringUtils.equalsIgnoreCase("TRUE", StringUtils.trim(value)));

            } else if(StringUtils.endsWith(key, "trigger.type")) {
                batchInfo.setTriggerType(StringUtils.trim(value));

            } else if(StringUtils.endsWith(String.valueOf(key), "trigger.value")) {
                batchInfo.setTriggerValue(StringUtils.trim(value));
            }
        }

        return batchInfos;
    }

    private void createAndStart(final BatchInfo batchInfo) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setThreadNamePrefix(batchInfo.getId());
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(false); //강제종료명령
        threadPoolTaskScheduler.initialize();

        Trigger trigger = new CronTrigger(batchInfo.getTriggerValue());

        if(StringUtils.equalsIgnoreCase(batchInfo.getTriggerType(), "cron")) {
            trigger = new CronTrigger(batchInfo.getTriggerValue());

        } else if(StringUtils.equalsIgnoreCase(batchInfo.getTriggerType(), "fixedDelay")) {
            trigger = new PeriodicTrigger(Long.parseLong(batchInfo.getTriggerValue()), TimeUnit.MILLISECONDS);

        } else if(StringUtils.equalsIgnoreCase(batchInfo.getTriggerType(), "fixedRate")) {
            trigger = new PeriodicTrigger(Long.parseLong(batchInfo.getTriggerValue()), TimeUnit.MILLISECONDS);
        }

        threadPoolTaskScheduler.schedule(() -> {
            Object bean = context.getBean(batchInfo.getBean());

            try {
                MethodUtils.invokeMethod(bean, batchInfo.getMethod());

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

        }, trigger);

        taskSchedulers.put(batchInfo.getId(), threadPoolTaskScheduler);
    }

}
