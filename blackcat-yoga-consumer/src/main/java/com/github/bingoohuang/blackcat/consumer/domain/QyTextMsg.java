package com.github.bingoohuang.blackcat.consumer.domain;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class QyTextMsg {
    private String touser;
    private String toparty;
    private String totag;
    private String msgtype;
    private int agentid;
    private Text text;
    private String safe;
}
