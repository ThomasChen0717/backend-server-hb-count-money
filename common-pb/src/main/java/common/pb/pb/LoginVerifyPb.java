package common.pb.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.widget.light.protobuf.ProtoFileMerge;
import common.pb.ProtoFile;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * 登录信息
 *
 * @author mark
 * @date 2023-04-09
 */
@ToString
@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ProtoFileMerge(fileName = ProtoFile.COMMON_FILE_NAME, filePackage = ProtoFile.COMMON_FILE_PACKAGE)
public class LoginVerifyPb {
    /** age 测试用的,Integer */
    Integer age;
    /** jwt */
    String jwt;

    /*
     *       注意，这个业务码放这里只是为了方便测试多种情况
     *       交由测试请求端来控制
     *
     *       ------------业务码说明------------------
     *
     *       当业务码为 【0】时 (相当于号已经在线上了，不能重复登录)
     *       如果游戏对外服已经有该玩家的连接了，就抛异常，告诉请求端玩家已经在线。
     *       否则就正常登录成功
     *
     *       当业务码为 【1】时 （相当于顶号）
     *       强制断开之前的客户端连接，并让本次登录成功。
     */
    /** 登录业务码 */
    int loginBizCode;
    /** Long value */
    Long time;

    /** long value */
    long time2;
}
