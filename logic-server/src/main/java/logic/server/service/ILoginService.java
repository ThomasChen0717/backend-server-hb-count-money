package logic.server.service;

import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;

/**
 * @author mark
 * @date 2023-04-14
 */
public interface ILoginService {
    void Test();
    LoginResPb Login(LoginReqPb loginReqPb, MyFlowContext myFlowContext);
}
