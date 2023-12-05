package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "smart-contract")
public class SmartContractProperties {

    private String mspId;

    private String channelName;

    private String chaincodeName;

    private String cryptoPath;

    private String certPath;

    private String keyDirPath;

    private String tlsCertPath;

    private String peerEndPointIp;

    private String peerEndPointPort;

    private String overrideAuth;
}
