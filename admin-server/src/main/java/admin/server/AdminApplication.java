package admin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 单独启动类：管理后台服务器
 *
 */
@SpringBootApplication
public class AdminApplication
{
    public static void main( String[] args ) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
