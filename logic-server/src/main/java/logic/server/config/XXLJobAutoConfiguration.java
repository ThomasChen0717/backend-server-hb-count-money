package logic.server.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mark
 * @date 2023-04-12
 */
// 暂时屏蔽xxl-job，还未使用到
@Configuration
@Slf4j
public class XXLJobAutoConfiguration {
    @Autowired
    private NacosConfiguration nacosConfiguration;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(nacosConfiguration.getXxlJobAdminAddresses());
        xxlJobSpringExecutor.setPort(nacosConfiguration.getXxlJobExecutorPort());
        xxlJobSpringExecutor.setAccessToken(nacosConfiguration.getXxlJobAccessToken());
        xxlJobSpringExecutor.setLogPath(nacosConfiguration.getXxlJobExecutorLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(nacosConfiguration.getXxlJobExecutorLogRetentionDays());

        String profilesActive = nacosConfiguration.getSpringProfilesActive();
        String registerAppName = applicationName + "-" + profilesActive;
        xxlJobSpringExecutor.setAppname(registerAppName);
        log.info("XXLJobAutoConfiguration::xxlJobExecutor:adminAddress = {},port = {},accessToken = {},appName = {},registerAppName = {}",
                nacosConfiguration.getXxlJobAdminAddresses(),nacosConfiguration.getXxlJobExecutorPort(),
                nacosConfiguration.getXxlJobAccessToken(),applicationName,registerAppName);
        return xxlJobSpringExecutor;
    }
}
