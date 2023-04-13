package common.pb.cmd;

public interface LoginCmdModule {
    int cmd = AllCmdModule.loginCmd;

    /** 登录 */
    int loginVerify = 1;

    /** 广播业务数据 */
    int broadcastData = 2;

    /** 测试 */
    int test = 3;
}
