package com.github.bingoohuang.blackcat.consumer.wx;

import com.github.bingoohuang.blackcat.consumer.domain.QyAccessToken;
import com.github.bingoohuang.westcache.WestCacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QyWxAccessTokenService {
    @Autowired QywxApi qywxApi;

    @WestCacheable(manager = "redis")
    public QyAccessToken getAccessToken(String corpId, String corpSecret) {
        return qywxApi.getToken(corpId, corpSecret);
    }
}
