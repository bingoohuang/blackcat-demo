package com.github.bingoohuang.blackcat.consumer.eventlistener;

import com.github.bingoohuang.blackcat.server.base.BlackcatReqListener;
import com.github.bingoohuang.blackcat.server.domain.BlackcatConfigBean;
import com.github.bingoohuang.blackcat.server.domain.BlackcatMethodRuntimeReq;
import com.github.bingoohuang.utils.redis.Redis;
import com.github.bingoohuang.utils.time.Now;
import com.google.common.eventbus.Subscribe;
import lombok.val;
import org.n3r.diamond.client.Miner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlackcatMethodRuntimeTopn implements BlackcatReqListener {
    @Autowired Redis redis;
    @Autowired BlackcatConfigBean configBean;

    @Subscribe
    public void topn(BlackcatMethodRuntimeReq req) {
        if (req.rt.getCostNano() < 1000000) return;

        val configPrefix = findMethodPackagePrefix(req);
        if (configPrefix == null) return;

        val key = "method:" + Now.now("yyyyMMdd") + ':' + configPrefix;
        val member = req.getRt().getMethodName();

        val zscore = redis.zscore(key, member);
        if (zscore != null && zscore >= req.rt.getCostNano()) return;

        val miner = new Miner().getMiner("blackcat", "consumer");
        int topn = miner.getInt("topn", 10);
        redis.topn(key, req.rt.getCostNano(), member, topn);
    }

    private String findMethodPackagePrefix(BlackcatMethodRuntimeReq req) {
        String className = req.getRt().getClassName();
        for (String prefix : configBean.getMethodPkgPrefixes()) {
            if (className.startsWith(prefix)) return prefix;
        }

        return null;
    }
}
