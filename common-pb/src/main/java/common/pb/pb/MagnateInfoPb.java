package common.pb.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import common.pb.ProtoFile;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 角色富豪挑战信息
 *
 * @author mark
 * @date 2023-04-25
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class MagnateInfoPb {
    /** 富豪id **/
    int magnateId;
    /** 是否已解除锁定：0 未解锁 1 已解锁 **/
    boolean isUnlocked;

    /** 富豪名称 **/
    String magnateName;
    /** 速度值，以每秒计 **/
    int speed;
    /** 目标金钱数量 **/
    int targetMoneyAmount;
    /** 奖励金钱数量 **/
    int rewardMoneyAmount;
    /** 解锁载具id，0 说明不解锁任何载具 **/
    int unlockVehicleId;
    /** 冷却时间，单位秒 **/
    int cdTime;
    /** 挑战时间，单位秒 **/
    int challengeTime;
    /** 资源名称 **/
    String resourceName;
}
