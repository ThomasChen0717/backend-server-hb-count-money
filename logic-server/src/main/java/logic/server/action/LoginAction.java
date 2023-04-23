package logic.server.action;

import com.iohao.game.action.skeleton.core.exception.MsgException;
import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import logic.server.service.ILoginService;
import common.pb.cmd.LoginCmdModule;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;
import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author mark
 * @date 2023-04-09
 */
@Slf4j
@Component
@ActionController(LoginCmdModule.cmd)
public class LoginAction {
    @Autowired
    private ILoginService loginService;

    /**
     * 登录
     * @param loginReqPb
     * @param myFlowContext
     * @return
     * @throws MsgException
     */
    @ActionMethod(LoginCmdModule.loginVerify)
    public LoginResPb loginVerify(LoginReqPb loginReqPb, MyFlowContext myFlowContext) throws MsgException{
        return loginService.Login(loginReqPb,myFlowContext);
    }

    /**
     * 登出
     * @param myFlowContext
     * @return
     * @throws MsgException
     */
    @ActionMethod(LoginCmdModule.logout)
    public void logout(MyFlowContext myFlowContext) throws MsgException{
        loginService.Logout(myFlowContext);
    }
}
