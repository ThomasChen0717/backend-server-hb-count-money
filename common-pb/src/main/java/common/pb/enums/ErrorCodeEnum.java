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
    accountOnline(10001, "角色已经在线上，不允许重复登录"),
    /** 创建或者获取角色失败 */
    addOrGetUserFailed(10002, "创建或者获取角色信息失败"),
    /** 登录异常 */
    loginError(10003, "登录异常"),
    /** 内存存储角色数据失败 */
    addUserDataToCacheFailed(10004, "内存存储角色数据失败"),
    /** 角色绑定逻辑服失败 */
    userBindServerIdFailed(10005, "角色绑定逻辑服失败"),
    /** 角色数据还在缓存中 */
    userDataStillInCache(10007, "角色数据还在缓存中，请稍后登录"),
    /** 升级属性类型不存在 */
    attributeTypeError(10008, "升级属性类型不存在"),
    /** 目标等级不能低于当前等级 */
    targetLevelError(10009, "目标等级不能低于当前等级"),
    /** 升级费用错误 */
    levelUpMoneyCostError(10010, "升级费用错误"),
    /** 费用不足 */
    moneyCostNotEnough(10011, "费用不足"),
    /** 载具使用中 */
    vehicleIsUsing(10012, "载具使用中"),
    /** 载具不存在 */
    vehicleNotExist(10013, "载具不存在"),
    /** 载具未解锁 */
    vehicleIsLock(10014, "载具未解锁"),
    /** 装备使用中 */
    equipmentIsUsing(10015, "装备使用中"),
    /** 装备不存在 */
    equipmentNotExist(10016, "装备不存在"),
    /** buffTool不存在 */
    buffToolNotExist(10017, "buffTool不存在"),
    /** 角色不存在 */
    userNotExist(10018, "角色不存在"),
    /** 装备未解锁 */
    equipmentIsLock(10019, "装备未解锁"),
    /** 富豪不存在 */
    magnateNotExist(10020, "富豪不存在"),
    /** 富豪未解锁 */
    magnateIsLock(10021, "富豪未解锁"),
    /** boss不存在 */
    bossNotExist(10022, "boss不存在"),
    /** 富豪未解锁 */
    bossIsLock(10023, "boss未解锁"),
    /** 角色标记还在线上，请稍后登录 */
    userStillOnline(10024, "角色标记还在线上，请稍后登录"),
    /** 解锁类型不存在 */
    unlockTypeError(10025, "解锁类型不存在"),
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
