package com.github.bingoohuang.blackcat.consumer.facade;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DiamondBaseUrlProvider;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringRestClientEnabled(baseUrlProvider = DiamondBaseUrlProvider.class)
@RequestMapping("/v1/merchant-admin")
public interface MerchantApi {
    @RequestMapping(value = "/get-class-notification-count", method = GET)
    String getClassNotificationCount();
}
