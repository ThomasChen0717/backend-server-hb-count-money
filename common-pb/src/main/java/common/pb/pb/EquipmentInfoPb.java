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
 * 角色装备信息
 *
 * @author mark
 * @date 2023-04-19
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class EquipmentInfoPb {
    /** 装备id **/
    int equipmentId;
    /** 是否使用中：false 未使用 true 使用中 **/
    boolean isInUse;
    /** 当前解锁条件数值 **/
    int unlockConditionCurrCount;
    /** 是否已解除锁定：false 未解锁 true 已解锁 **/
    boolean isUnlocked;

    /** 装备名称 **/
    String equipmentName;
    /** 解锁条件类型：0 广告 1 金钱 **/
    int unlockConditionType;
    /** 解锁条件数值 **/
    int unlockConditionCount;
    /** 影响属性类型 **/
    int effectAttributeType;
    /** 影响属性倍数 **/
    float effectAttributeMultiple;
    /** 影响属性说明 **/
    String effectAttributeRemark;
    /** 显示顺序：值越小展示越靠前 **/
    int showIndex;
    /** 前置装备条件：0表示没有开启前置装备条件 **/
    int preEquipmentId;
    /** 资源名称 **/
    String resourceName;
}
