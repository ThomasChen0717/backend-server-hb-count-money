package common.pb.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import common.pb.ProtoFile;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * 彩票信息
 *
 * @author mark
 * @date 2023-06-08
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class LotteryTicketInfoPb {
    /** 面值 **/
    int faceValue;
    /** 中奖号码 **/
    String winningNumber;
    /** 我的号码：[{"number":"08","isWinning":false,"bonus":40000},{"number":"04","isWinning":false,"bonus":39000}] **/
    String myNumberList = "";
}
