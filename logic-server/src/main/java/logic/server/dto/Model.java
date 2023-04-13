package logic.server.dto;

import java.io.Serializable;

public interface Model extends Serializable {
    /**
     * 检查属性
     * @param isThrow 是否抛出异常
     * @return 结果
     */
    default boolean check(boolean isThrow) {
        return true;
    }
}
