package common.pb.pb;

import com.alibaba.fastjson.JSONObject;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import common.pb.ProtoFile;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * gm命令请求
 *
 * @author mark
 * @date 2023-05-06
 */
@Data
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class GmCommandReqPb {
    /** gmCommandId:1:加钱 2:装备全解锁命令 3:载具全解锁命令 4:富豪全解锁 5:boss全解锁 6:属性升级 7:角色数据重置 **/
    @NotNull
    int gmCommandId;

    /** gm命令对应的信息:加钱命令:{"money",10},装备全解锁命令:{},载具全解锁命令:{},富豪全解锁:{},boss全解锁:{},属性升级:{"attributeType":1,"targetLevel":1000},角色数据重置:{} **/
    @NotNull
    String jsonGmCommandInfo;
}
