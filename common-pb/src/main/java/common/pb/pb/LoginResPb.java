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
 * 登录请求响应
 *
 * @author mark
 * @date 2023-04-14
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class LoginResPb {
    /** token */
    String token;
    /** 用户id */
    long userId;
    /** 金钱数量 */
    long money;

    /** 完成一次数钱操作增加能量点数 */
    int energyAddValue;
    /** 能量最大点数 */
    int energyMaxValue;

    /** 力量等级 */
    int strengthLevel;
    /** 体力等级 */
    int physicalLevel;
    /** 体力恢复等级 */
    int physicalRestoreLevel;
    /** 耐力等级 */
    int enduranceLevel;
    /** 宠物等级 */
    int petLevel;
    /** 收益倍数 */
    float incomeMultiple;

    /** 力量升级花费公式 */
    String strengthLevelUpFormula;
    /** 体力升级花费公式 */
    String physicalLevelUpFormula;
    /** 体力恢复升级花费公式 */
    String physicalRestoreLevelUpFormula;
    /** 耐力升级花费公式 */
    String enduranceLevelUpFormula;
    /** 宠物升级花费公式 */
    String petLevelUpFormula;
    /** 力量效果公式 */
    String strengthEffectFormula;
    /** 体力效果公式 */
    String physicalEffectFormula;
    /** 体力恢复效果公式 */
    String physicalRestoreEffectFormula;
    /** 耐力效果公式 */
    String enduranceEffectFormula;
    /** 宠物效果公式 */
    String petEffectFormula;
}
