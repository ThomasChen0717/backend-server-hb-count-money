package logic.server.service;

/**
 * 推送消息服务接口
 * @author mark
 * @date 2023-04-20
 */
public interface IPushPbService {
    /** 同步角色金钱数量 **/
    void moneySync(long userId);
}
