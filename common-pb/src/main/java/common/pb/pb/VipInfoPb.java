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
 * 角色vip等级信息
 *
 * @author mark
 * @date 2023-05-16
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class VipInfoPb {
    /** vip_level **/
    int vipLevel;
    /** 达成条件：观看视频次数 **/
    int conditionCount;
    /** 影响属性信息 **/
    List<EffectAttributeInfoPb> effectAttributeInfoPbList = new ArrayList<>();
}
