package com.baida.countmoney.client.command;

import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author mark
 * @date 2023-04-09
 */
@Getter
@FieldDefaults(level = AccessLevel.PUBLIC)
public class ClientCommand {
    ExternalMessage externalMessage;

    Class<?> resultClass;
    /** 执行完请求后，进行睡眠的时间 */
    long sleepMilliseconds;

    ClientCommand() {
    }
}
