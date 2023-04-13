package logic.server.parent.action.skeleton.core.flow;

import com.iohao.game.action.skeleton.core.flow.FlowContext;

/**
 * @author mark
 * @date 2023-04-09
 */
public class MyFlowContext extends FlowContext {
    public String hello() {
        // 在 MyFlowContext 中，扩展的方法
        return "MyFlowContext hello";
    }
}
