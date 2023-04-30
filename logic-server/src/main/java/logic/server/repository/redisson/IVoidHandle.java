package logic.server.repository.redisson;

/**
 * 带返回值的执行接口
 * <pre>
 *  后期可以放到conmmon中，再所有存在回调的地方使用
 * </pre>
 *
 * @author mark
 * @date 2023-04-17
 */
public interface IVoidHandle {
    void execute();
}
