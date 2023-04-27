package logic.server.listener;

import logic.server.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class MyApplicationListener implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    private IUserService userService;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 执行关闭服务的逻辑
        log.info("MyApplicationListener::onApplicationEvent:执行关闭服务的逻辑");
        userService.saveDataFromCacheToDB();
    }
}
