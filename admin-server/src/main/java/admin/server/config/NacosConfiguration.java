package admin.server.config;

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
    /** active **/
    @Value("${spring.profiles.active}")
    private String springProfilesActive;
    public String getSpringProfilesActive() { return springProfilesActive; }
}
