package external.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ConfigTemplate {
    @Value("${server.port}")
    private int serverPort;

    @Value("${brokerServer.url}")
    private String brokerServerUrl;
    @Value("${brokerServer.port}")
    private int brokerServerPort;
}
