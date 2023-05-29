package common.pb.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import common.pb.ProtoFile;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 载具（新）升级请求
 *
 * @author mark
 * @date 2023-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class VehicleNewLevelUpReqPb {
    /** 载具（新）id*/
    @NotNull
    int vehicleId;
    /** 目标等级 */
    @NotNull
    int targetLevel;
    /** 升级费用 */
    @NotNull
    long moneyCost;
}
