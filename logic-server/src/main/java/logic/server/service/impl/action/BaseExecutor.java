package logic.server.service.impl.action;

public interface BaseExecutor<T,R,U> {
    public R executor(T arg,U userId);
}
