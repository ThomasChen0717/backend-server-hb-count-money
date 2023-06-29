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
 * 抽签-使用锦囊请求响应
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
public class DrawUseBagResPb {
    /** 响应代码：0 表示成功 **/
    int code = 0;
    /** 响应文本：默认 success **/
    String message = "success";

    /** 剩余轮数 **/
    int remainDrawCount;
    /** 上上签数量 **/
    int ssqCount;
    /** 锦囊是否可用 **/
    boolean isBagUsable;
    /** 0 轮数+1 1 上上签-1  **/
    int resultType;
}
