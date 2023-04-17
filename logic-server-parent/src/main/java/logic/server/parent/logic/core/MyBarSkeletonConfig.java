package logic.server.parent.logic.core;

import common.pb.enums.ErrorCodeEnum;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilderParamConfig;
import com.iohao.game.action.skeleton.core.flow.interal.DebugInOut;
import common.pb.SendDoc;
import logic.server.parent.action.skeleton.core.flow.MyFlowContext;
import lombok.experimental.UtilityClass;

/**
 * @author mark
 * @date 2023-04-09
 */
@UtilityClass
public class MyBarSkeletonConfig {
    /**
     * 在实践项目中，可以提供这样一个通用的业务框架配置
     *
     * @return BarSkeletonBuilderParamConfig
     */
    public BarSkeletonBuilderParamConfig createBarSkeletonBuilderParamConfig() {
        // 业务框架构建器 配置
        return new BarSkeletonBuilderParamConfig()
                // 开启广播日志
                .setBroadcastLog(true)
                // 异常码文档生成
                .addErrorCode(ErrorCodeEnum.values())
                // 推送(广播)文档生成
                .scanActionSendPackage(SendDoc.class)
                ;
    }

    /**
     * 在实践项目中，可以提供这样一个通用的业务框架配置
     *
     * @param config BarSkeletonBuilderParamConfig
     * @return BarSkeletonBuilder
     */
    public BarSkeletonBuilder createBarSkeletonBuilder(BarSkeletonBuilderParamConfig config) {
        // 业务框架构建器
        return config.createBuilder()
                // 添加控制台输出插件
                .addInOut(new DebugInOut())
                // 设置一个自定义的 flow 上下文生产工厂
                .setFlowContextFactory(MyFlowContext::new)
                ;
    }

    private BarSkeleton createBarSkeleton() {
        BarSkeletonBuilderParamConfig config = new BarSkeletonBuilderParamConfig();

        BarSkeletonBuilder builder = config.createBuilder()
                // 设置一个自定义的 flow 上下文生产工厂
                .setFlowContextFactory(MyFlowContext::new)
                ;

        return builder.build();
    }
}
