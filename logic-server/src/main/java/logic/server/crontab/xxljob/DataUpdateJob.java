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

    /** 定时检测保存符合条件用户数据 **/
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

    /** 定时检测当前用户数量 **/
    @XxlJob("onlineUserCountJobHandler")
    public ReturnT<String> onlineUserCountJobHandler(String param){
        log.info("DataUpdateJob::onlineUserCountJobHandler:param = {},定时任务开始",param);

        try {
            userService.onlineUserCount();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("DataUpdateJob::onlineUserCountJobHandler:param = {},定时任务结束",param);
        return ReturnT.SUCCESS;
    }

    /** 定时清理历史用户数据 **/
    @XxlJob("checkDeleteHistoryUserDataJobHandler")
    public ReturnT<String> checkDeleteHistoryUserDataJobHandler(String param){
        log.info("DataUpdateJob::checkDeleteHistoryUserDataJobHandler:param = {},定时任务开始",param);

        try {
            userService.checkDeleteHistoryUserData();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("DataUpdateJob::checkDeleteHistoryUserDataJobHandler:param = {},定时任务结束",param);
        return ReturnT.SUCCESS;
    }
}
