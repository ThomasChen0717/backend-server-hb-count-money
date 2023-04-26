package logic.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * action
 *
 * @author mark
 * @date 2023-04-10
 */
@Configuration
public class NacosConfiguration {
    /** xxljob **/
    @Value("${xxl.job.admin.addresses}")
    private String xxlJobAdminAddresses;
    public String getXxlJobAdminAddresses() { return xxlJobAdminAddresses; }
    @Value("${xxl.job.executor.port}")
    private int xxlJobExecutorPort;
    public int getXxlJobExecutorPort() { return xxlJobExecutorPort; }
    @Value("${xxl.job.executor.logpath}")
    private String xxlJobExecutorLogPath;
    public String getXxlJobExecutorLogPath() { return xxlJobExecutorLogPath; }
    @Value("${xxl.job.executor.logretentiondays}")
    private int xxlJobExecutorLogRetentionDays;
    public int getXxlJobExecutorLogRetentionDays() { return xxlJobExecutorLogRetentionDays; }
    @Value("${xxl.job.accessToken}")
    private String xxlJobAccessToken;
    public String getXxlJobAccessToken() { return xxlJobAccessToken; }

    /** dy **/
    @Value("${dy.url}")
    private String dyUrl;
    public String getDyUrl() { return dyUrl; }
    @Value("${dy.appId}")
    private String dyAppId;
    public String getDyAppId() { return dyAppId; }
    @Value("${dy.secret}")
    private String dySecret;
    public String getDySecret() { return dySecret; }

    /** brokerServer **/
    @Value("${brokerServer.url}")
    private String brokerServerUrl;
    public String getBrokerServerUrl() { return brokerServerUrl; }
    @Value("${brokerServer.port}")
    private int brokerServerPort;
    public int getBrokerServerPort() { return brokerServerPort; }

    @Value("${server.id}")
    private String serverId;
    public String getServerId() { return serverId; }
}
