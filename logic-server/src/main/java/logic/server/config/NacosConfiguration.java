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
}
