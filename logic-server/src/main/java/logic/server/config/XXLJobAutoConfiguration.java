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
//@Configuration
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
        xxlJobSpringExecutor.setAppname(applicationName);
        return xxlJobSpringExecutor;
    }
}
