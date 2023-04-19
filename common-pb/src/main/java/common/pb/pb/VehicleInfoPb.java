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
 * 用户载具信息
 *
 * @author mark
 * @date 2023-04-19
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class VehicleInfoPb {
    /** 载具id **/
    int vehicleId;
    /** 是否使用中：false 未使用 true 使用中 **/
    boolean isInUse;
    /** 当前解锁条件数值 **/
    int unlockConditionCurrCount;
    /** 是否已解除锁定：false 未解锁 true 已解锁 **/
    boolean isUnlocked;

    /** 载具名称 **/
    String vehicleName;
    /** 解锁条件类型：0 广告 1 金钱 2 挑战boss **/
    int unlockConditionType;
    /** 解锁条件数值（如是挑战boss类型，此值为boss id） **/
    int unlockConditionCount;
    /** 容量 **/
    int capacity;
    /** 额外奖励 **/
    int extraRewardValue;
}
