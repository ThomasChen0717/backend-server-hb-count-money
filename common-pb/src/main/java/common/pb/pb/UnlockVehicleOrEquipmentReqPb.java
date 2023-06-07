package common.pb.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import common.pb.ProtoFile;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * 解锁载具或者装备请求
 *
 * @author mark
 * @date 2023-04-27
 */
@Data
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class UnlockVehicleOrEquipmentReqPb {
    /** 1 载具 2 装备 3 载具（新） **/
    @NotNull
    int type;
    /** 载具或者装备或者载具（新）id **/
    @NotNull
    int itemId;
}
