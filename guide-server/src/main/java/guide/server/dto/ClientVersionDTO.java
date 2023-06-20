package guide.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientVersionDTO implements DTO{
    private static final long serialVersionUID = 1L;

    private long id;
    private String clientVersion;
    private boolean isVerified;
    private String verificationServer;
    private String releaseServer;
    private String verificationPreLoginServer;
    private String releasePreLoginServer;
}
