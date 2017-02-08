package com.github.bingoohuang.blackcat.consumer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.bingoohuang.westcache.base.ExpireAfterWrite;
import lombok.Data;

@Data
public class QyAccessToken {
    @JSONField(name = "access_token") String accessToken;
    @JSONField(name = "expires_in") long expiresIn;

    public long getExpiresIn() {
        return expiresIn;
    }

    @ExpireAfterWrite public String expireAfterWrite() {
        return expiresIn + "s";
    }
}
