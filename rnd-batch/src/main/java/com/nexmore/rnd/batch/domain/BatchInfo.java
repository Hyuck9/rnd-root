package com.nexmore.rnd.batch.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class BatchInfo implements Serializable {
    @SuppressWarnings("SpellCheckingInspection")
    private Boolean actived;
    private String id;
    private String bean;
    private String method;
    private String triggerType;
    private String triggerValue;
}
