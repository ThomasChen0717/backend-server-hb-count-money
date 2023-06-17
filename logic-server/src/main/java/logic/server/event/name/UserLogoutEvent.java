package logic.server.event.name;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class UserLogoutEvent extends ApplicationEvent {
    private final long userId;
    public UserLogoutEvent(Object source,long userId) {
        super(source);
        this.userId = userId;
    }
}
