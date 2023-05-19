package guide.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 单独启动类：向导服务器
 *
 * @author mark
 * @date 2023-05-17
 */
@SpringBootApplication
public class GuideApplication {
    public static void main( String[] args ) {
        SpringApplication.run(GuideApplication.class, args);
    }
}
