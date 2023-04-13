package common.pb;

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
    /** 号已经在线上，不允许重复登录 */
    accountOnline(103, "号已经在线上，不允许重复登录"),
    /** 登录异常 */
    loginError(104, "登录异常"),
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
