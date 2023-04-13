package common.pb.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * @author mark
 * @date 2023-04-10
 */
@ToString
@ProtobufClass
//@EnableZigZap
@FieldDefaults(level = AccessLevel.PUBLIC)
public class CatMessage {
    /** 请求命令类型: 0 心跳，1 业务 */
//    @Protobuf(fieldType = FieldType.INT32, order = 1)
    int cmdCode;
    /** 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验 */
//    @Protobuf(fieldType = FieldType.INT32, order = 2)

    int protocolSwitch;
    /** 业务路由（高16为主, 低16为子） */
//    @Protobuf(fieldType = FieldType.INT32, order = 3)
    int cmdMerge;


//    @Protobuf(fieldType = FieldType.SINT32, order = 4)
//    int responseStatus;
//    /** 验证信息 */
//    @Protobuf(fieldType = FieldType.STRING, order = 5)
//    String validMsg;
//    @Protobuf(fieldType = FieldType.BYTES, order = 6)
//    byte[] data;

}
