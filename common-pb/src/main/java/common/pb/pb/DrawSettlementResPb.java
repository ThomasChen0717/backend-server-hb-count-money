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
 * 抽签-回合结算请求响应
 *
 * @author mark
 * @date 2023-06-26
 */
@Data
@ToString
@Accessors(chain = true)
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class DrawSettlementResPb {
    /** 响应代码：0 表示成功 **/
    int code = 0;
    /** 响应文本：默认 success **/
    String message = "success";
}
