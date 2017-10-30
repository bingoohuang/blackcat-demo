package com.github.bingoohuang.blackcat.consumer.wx;

import com.github.bingoohuang.blackcat.consumer.domain.QyTextMsg;
import com.github.bingoohuang.blackcat.consumer.domain.Text;
import com.github.bingoohuang.blackcat.server.base.MsgService;
import com.github.bingoohuang.westcache.utils.WestCacheConnector;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.joda.time.DateTime;
import org.n3r.diamond.client.Miner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class WxMsgService implements MsgService {
    @Value("${blackcat^wx^corpid}") String corpid;
    @Value("${blackcat^wx^corpsecret}") String corpsecret;

    @Autowired QyWxAccessTokenService qyWxAccessTokenService;
    @Autowired QywxApi qywxApi;

    public void sendBlackCatMsg(Text data) {
        val miner = new Miner().getMiner("blackcat", "userIds");
        String userIds = miner.getString("userIds");
        val qyTextMsg = QyTextMsg.builder()
                .agentid(4)
                .text(data)
                .msgtype("text")
                .safe("0")
                .touser(userIds)
                .build();
        sendWithRetry(qyTextMsg);
    }

    private void sendWithRetry(QyTextMsg qyTextMsg) {
        for (int retriedTimes = 1; retriedTimes < 3; ++retriedTimes) {
            val accessToken = qyWxAccessTokenService.getAccessToken(corpid, corpsecret);
            val rsp = qywxApi.send(accessToken.getAccessToken(), qyTextMsg);

            // {"errcode":40001,"errmsg":"invalid credential, access_token
            //               is invalid or not latest hint: [XPKpLA0601age8]"}
            // {"errcode":42001,"errmsg":"access_token expired hint: [g240000age6]"}
            if (rsp.getErrorCode() == 0) return;

            WestCacheConnector.clearCache(() ->
                    qyWxAccessTokenService.getAccessToken(corpid, corpsecret));

            log.debug("clear AccessToken Cache because of" +
                    " {} and retry at times {} ", rsp, retriedTimes);
        }
    }

    @Override
    public void sendMsg(String title, String detail) {
        val miner = new Miner().getMiner("blackcat", "area");
        val area = miner.getString("area");
        val text = Text.builder().content(area + "监控:\n" + title + "\n"
                + detail + "\nat " + now()).build();
        sendBlackCatMsg(text);
    }

    public static String now() {
        return DateTime.now().toString("MM月dd日HH:mm:ss");
    }
}
