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
 * 富豪挑战成功请求
 *
 * @author mark
 * @date 2023-04-25
 */
@Data
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class ChallengeMagnateSuccessReqPb {
    /** 富豪id **/
    @NotNull
    int magnateId;
    /** 奖励倍数 */
    @NotNull
    int multiple;
}
