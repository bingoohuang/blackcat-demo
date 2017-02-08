package com.github.bingoohuang.blackcat.consumer.wx;

import com.github.bingoohuang.blackcat.consumer.domain.QyAccessToken;
import com.github.bingoohuang.blackcat.consumer.domain.QyMsgResponse;
import com.github.bingoohuang.blackcat.consumer.domain.QyTextMsg;
import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@SpringRestClientEnabled(baseUrl = "https://qyapi.weixin.qq.com")
public interface QywxApi {
    @RequestMapping(value = "/cgi-bin/gettoken", method = GET)
    QyAccessToken getToken(@RequestParam("corpid") String corpId,
                           @RequestParam("corpsecret") String corpSecret);

    @RequestMapping(value = "/cgi-bin/message/send", method = POST)
    QyMsgResponse send(@RequestParam("access_token") String accessToken,
                       @RequestBody QyTextMsg qyTextMsg);
}
