package logic.server.event.listener;

import logic.server.event.name.UserLogoutEvent;
import logic.server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserLogoutListener {
    @Autowired
    private IUserService userService;
    @Async
    @EventListener
    public void handleUserLogout(UserLogoutEvent event) {
        long userId = event.getUserId();
        userService.saveDataFromCacheToDB(userId);
    }
}
