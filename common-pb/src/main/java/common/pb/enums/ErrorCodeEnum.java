package common.pb.enums;

import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * 断言 + 异常机制
 * @author mark
 * @date 2023-04-09
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCodeEnum implements MsgExceptionInfo {
    /** 登录模块-错误码 **/
    /** 号已经在线上，不允许重复登录 */
    accountOnline(10001, "用户已经在线上，不允许重复登录"),
    /** 创建或者获取用户失败 */
    addOrGetUserFailed(10002, "创建或者获取用户信息失败"),
    /** 登录异常 */
    loginError(10003, "登录异常"),
    ;

    /** 消息码 */
    final int code;
    /** 消息模板 */
    final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
