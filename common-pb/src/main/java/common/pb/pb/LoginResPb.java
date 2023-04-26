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
public class LoginResPb{
    /** 响应代码：0 表示成功 **/
    int code = 0;
    /** 响应文本：默认 success **/
    String message = "success";

    /** token */
    String token;
    /** 角色id */
    long userId;
    /** 金钱数量 */
    long money;
    /** 历史金钱数量 */
    long moneyHistory;

    /** 宠物离线时间：单位秒 **/
    int offlineTime;
    /** 宠物离线收益 **/
    int petOfflineIncome;

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

    /** 完成一次数钱操作增加能量点数 */
    int energyAddValue;
    /** 能量最大点数 */
    int energyMaxValue;
    /** 宠物完成一次任务时间，单位秒 */
    int petFinishJobTime;

    /** 角色载具列表 **/
    List<VehicleInfoPb> vehicleInfoPbList = new ArrayList<>();

    /** 角色装备列表 **/
    List<EquipmentInfoPb> equipmentInfoPbList = new ArrayList<>();

    /** 角色BuffTool列表 **/
    List<BuffToolInfoPb> buffToolInfoPbList = new ArrayList<>();

    /** 角色富豪挑战列表 **/
    List<MagnateInfoPb> magnateInfoPbList = new ArrayList<>();

    /** 角色boss挑战列表 **/
    List<BossInfoPb> bossInfoPbList = new ArrayList<>();
}
