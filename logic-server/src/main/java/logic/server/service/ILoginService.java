package logic.server.service;

import com.alibaba.fastjson.JSONObject;
import common.pb.pb.LoginReqPb;
import common.pb.pb.LoginResPb;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;

/**
 * @author mark
 * @date 2023-04-14
 */
public interface ILoginService {
    JSONObject preLogin(JSONObject jsonPreLogin);
    LoginResPb login(LoginReqPb loginReqPb, MyFlowContext myFlowContext);

    void logout(MyFlowContext myFlowContext);
}
