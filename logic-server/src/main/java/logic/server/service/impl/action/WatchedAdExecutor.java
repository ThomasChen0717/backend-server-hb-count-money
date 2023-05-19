package logic.server.service.impl.action;

import common.pb.pb.WatchedAdReqPb;
import common.pb.pb.WatchedAdResPb;
import logic.server.dto.CfgVipDTO;
import logic.server.dto.UserVipDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.CfgManagerSingleton;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WatchedAdExecutor implements BaseExecutor<WatchedAdReqPb, WatchedAdResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;

    @Override
    public WatchedAdResPb executor(WatchedAdReqPb arg, Long userId){
        log.info("WatchedAdExecutor::executor:userId = {},arg = {},start",userId,arg);
        WatchedAdResPb watchedAdResPb = new WatchedAdResPb();

        UserVipDTO userVipDTO = UserManagerSingleton.getInstance().getUserVipFromCache(userId);
        if(userVipDTO != null){
            userVipDTO.setVipCurrConditionCount(userVipDTO.getVipCurrConditionCount() + 1);
            CfgVipDTO cfgVipDTO = CfgManagerSingleton.getInstance().getCfgVipDTOMap().get(userVipDTO.getVipLevel()+1);
            if(cfgVipDTO != null){
                if(userVipDTO.getVipCurrConditionCount() >= cfgVipDTO.getConditionCount()){
                    userVipDTO.setVipLevel(cfgVipDTO.getVipLevel());
                }
            }
            /** 同步vip等级（推送）**/
            pushPbService.vipSync(userId);
        }

        log.info("WatchedAdExecutor::executor:userId = {},watchedAdResPb = {},end",userId,watchedAdResPb);
        return watchedAdResPb;
    }
}
