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
 * 富豪挑战成功请求响应
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
public class ChallengeMagnateSuccessResPb {
    /** 响应代码：0 表示成功 **/
    int code = 0;
    /** 响应文本：默认 success **/
    String message = "success";

    /** 下一个解锁的富豪id:0 表示没有解锁下一个 **/
    int unlockedMagnateId = 0;
    /** 解锁的载具id：0 表示没有奖励解锁载具 */
    int unlockedVehicleId = 0;
    /** 打败的富豪Id */
    int beatMagnateId = 0;
}
