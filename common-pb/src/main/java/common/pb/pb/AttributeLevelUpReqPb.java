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
 * 属性升级请求
 *
 * @author mark
 * @date 2023-04-20
 */
@Data
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class AttributeLevelUpReqPb {
    /** 属性类型：1 力量等级，2 体力等级，3 体力恢复等级 4 耐力等级，5 宠物等级*/
    @NotNull
    int attributeType;
    /** 目标等级 */
    @NotNull
    int targetLevel;
    /** 升级费用 */
    @NotNull
    long moneyCost;
}
