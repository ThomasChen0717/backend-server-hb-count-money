package logic.server.crontab.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import logic.server.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataUpdateJob {
    @Autowired
    private IUserService userService;

    @XxlJob("saveUserDataJobHandler")
    public ReturnT<String> saveUserDataJobHandler(String param){
        log.info("DataUpdateJob::saveUserDataJobHandler:param = {},定时任务开始",param);

        try {
            userService.checkSaveDataFromCacheToDB(0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("DataUpdateJob::saveUserDataJobHandler:param = {},定时任务结束",param);
        return ReturnT.SUCCESS;
    }
}
