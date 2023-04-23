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
 * 结算请求
 *
 * @author mark
 * @date 2023-04-20
 */
@Data
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class SettlementReqPb {
    /** 结算角色：1 主角 2 宠物 */
    @NotNull
    int settlementRole;
    /** 结算类型：1 在线计算 2 离线结算 （此字段只有结算角色为宠物时才有意义）*/
    @NotNull
    int settlementType;
}
