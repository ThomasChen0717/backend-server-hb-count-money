package logic.server.service.impl;

import logic.server.service.IServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServerServiceImpl implements IServerService {
    @Autowired
    private ApplicationContext context;

    @Override
    public void closeServer() {
        // shutdown()方法会在等待所有正在进行的HTTP请求处理完毕之后关闭服务，因此可能需要等待一段时间
        // 如果需要立即关闭服务，可以通过调用close()方法来实现
        log.info("ServerServiceImpl::closeServer:服务器关闭");
        ((ConfigurableApplicationContext) context).close();
    }
}
