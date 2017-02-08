package com.github.bingoohuang.blackcat.consumer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class QyMsgResponse {
    @JSONField(name = "errcode") private int errorCode;
    @JSONField(name = "errmsg") private String errorMsg;
    @JSONField(name = "msgid") private String messageId;
}
