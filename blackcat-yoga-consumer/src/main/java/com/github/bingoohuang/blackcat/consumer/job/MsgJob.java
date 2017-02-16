package com.github.bingoohuang.blackcat.consumer.job;

import com.github.bingoohuang.blackcat.server.job.AbstractMsgJob;
import org.n3r.diamond.client.Miner;
import org.springframework.stereotype.Component;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/1/19.
 */
// @Component
public class MsgJob extends AbstractMsgJob {
    @Override protected String getCron() {
        return new Miner().getStone("blackcat", "cron.schedule",
                "0 0/3 * 1/1 * ? *");
    }
}
