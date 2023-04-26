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
 * 角色boss挑战信息
 *
 * @author mark
 * @date 2023-04-25
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class BossInfoPb {
    /** bossId **/
    int bossId;
    /** 是否已解除锁定：0 未解锁 1 已解锁 **/
    boolean isUnlocked;

    /** boss名称 **/
    String bossName;
    /** 速度值，以每秒计 **/
    int speed;
    /** 目标金钱数量 **/
    int targetMoneyAmount;
    /** 奖励金钱数量 **/
    int rewardMoneyAmount;
    /** 挑战时间，单位秒 **/
    int challengeTime;
    /** 前置bossId，0 表示没有前置boss条件 **/
    int preBossId;
    /** 显示顺序：值越小展示越靠前 **/
    int showIndex;
    /** 资源名称 **/
    String resourceName;
}
