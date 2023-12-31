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
 * buffTool开始使用或者结束使用请求
 *
 * @author mark
 * @date 2023-04-20
 */
@Data
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class StartOrEndBuffToolReqPb {
    @NotNull
    int buffToolId;
    @NotNull
    boolean isStart;
    /** 剩余生效时间，单位秒 */
    @NotNull
    int effectLeftTime;
}
