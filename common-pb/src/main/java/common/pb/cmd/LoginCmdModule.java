package common.pb.cmd;

public interface LoginCmdModule {
    int cmd = AllCmdModule.loginCmd;

    /** 登录 */
    int loginVerify = 1;

    /** 登出 */
    int logout = 2;
}
