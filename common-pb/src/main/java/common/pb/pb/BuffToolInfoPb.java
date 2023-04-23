package common.pb.pb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
 * 角色BuffTool信息
 *
 * @author mark
 * @date 2023-04-23
 */
@Data
@Accessors(chain = true)
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class BuffToolInfoPb {
    /** buff_tool_id **/
    int buffToolId;
    /** 影响属性信息 **/
    List<EffectAttributeInfoPb> effectAttributeInfoPbList = new ArrayList<>();
    /** 持续时间，单位秒 **/
    int durations;
}
