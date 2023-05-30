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
 * 角色载具（新）信息
 *
 * @author mark
 * @date 2023-05-26
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class VehicleInfoNewPb {
    /** 载具id **/
    int vehicleId;
    /** 当前解锁条件数值 **/
    int unlockConditionCurrCount;
    /** 是否已解除锁定：false 未解锁 true 已解锁 **/
    boolean isUnlocked;
    /** 当前等级 **/
    int level;
    /** 显示顺序：值越小展示越靠前 **/
    int showIndex;
}
