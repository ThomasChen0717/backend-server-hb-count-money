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
 * 富豪挑战成功请求响应
 *
 * @author mark
 * @date 2023-04-25
 */
@Data
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class ChallengeMagnateSuccessResPb {
    /** 下一个富豪id:0 表示没有下一个富豪可以挑战 **/
    int nextMagnateId = 0;
    /** 解锁的载具id：0 表示没有奖励解锁载具 */
    int unlockedVehicleId = 0;
}
