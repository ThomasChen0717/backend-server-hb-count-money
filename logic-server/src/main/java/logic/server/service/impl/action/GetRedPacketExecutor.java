package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.GetRedPacketReqPb;
import common.pb.pb.GetRedPacketResPb;
import logic.server.dto.UserDTO;
import logic.server.service.IPushPbService;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GetRedPacketExecutor implements BaseExecutor<GetRedPacketReqPb, GetRedPacketResPb,Long>{
    @Autowired
    private IPushPbService pushPbService;
    @Override
    public GetRedPacketResPb executor(GetRedPacketReqPb arg, Long userId){
        log.info("GetRedPacketExecutor::executor:userId = {},arg = {},start",userId,arg);
        GetRedPacketResPb getRedPacketResPb = new GetRedPacketResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        if(userDTO == null){
            getRedPacketResPb.setCode(ErrorCodeEnum.userNotExist.getCode()).setMessage(ErrorCodeEnum.userNotExist.getMsg());
            log.info("GetRedPacketExecutor::executor:userId = {},getRedPacketResPb = {},end",userId,getRedPacketResPb);
            return getRedPacketResPb;
        }

        long moneyIncome = arg.getMoney() * arg.getMultiple();
        long finalMoney = userDTO.getMoney() + moneyIncome;
        userDTO.setMoney(finalMoney);

        /** 同步金钱数量（推送）**/
        pushPbService.moneySync(userId);

        log.info("GetRedPacketExecutor::executor:userId = {},getRedPacketResPb = {},end",userId,getRedPacketResPb);
        return getRedPacketResPb;
    }
}
