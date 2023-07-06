package admin.server.service;

public interface IWebValidateService {
    boolean checkUniqueUsername(String Username);

    boolean checkUniqueDisplayName(String DisplayName);

    boolean checkOldPassword(String password, long id);
}

