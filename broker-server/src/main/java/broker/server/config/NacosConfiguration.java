package broker.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NacosConfiguration {
    /** active **/
    @Value("${spring.profiles.active}")
    private String springProfilesActive;
    public String getSpringProfilesActive() { return springProfilesActive; }
}
