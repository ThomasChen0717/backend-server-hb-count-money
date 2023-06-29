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
 * 抽签-回合准备请求响应
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
public class DrawRoundReadyResPb {
    /** 响应代码：0 表示成功 **/
    int code = 0;
    /** 响应文本：默认 success **/
    String message = "success";

    /** 回合数 **/
    int roundNumber;
    /** 剩余轮数 **/
    int remainDrawCount;
    /** 上上签数量 **/
    int ssqCount;
    /** 上上签目标数量 **/
    int ssqTargetCount;
    /** 锦囊是否可用 **/
    boolean isBagUsable;
}
