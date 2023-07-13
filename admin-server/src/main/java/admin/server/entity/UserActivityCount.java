package admin.server.entity;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class UserActivityCount {
        private static final long serialVersionUID = 1L;

        private String time;
        private int activeUserCount;
        private int newUserCount;
        private int totalUserCount;
}
