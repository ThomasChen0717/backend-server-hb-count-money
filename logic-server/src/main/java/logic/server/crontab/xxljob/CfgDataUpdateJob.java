package logic.server.crontab.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CfgDataUpdateJob {
    @XxlJob("cfgDataUpdateJobHandler")
    public ReturnT<String> cfgDataUpdateJobHandler(String param){
        log.info("CfgDataUpdateJob::cfgDataUpdateJobHandler:param = {},定时任务开始",param);

        try {
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("CfgDataUpdateJob::cfgDataUpdateJobHandler:param = {},定时任务结束",param);
        return ReturnT.SUCCESS;
    }
}
