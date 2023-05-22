package logic.server.service.impl.action;

import common.pb.pb.SelectStoneReqPb;
import common.pb.pb.SelectStoneResPb;
import logic.server.dto.UserDTO;
import logic.server.dto.UserMagnateDTO;
import logic.server.enums.StoneEnum;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class SelectStoneExecutor implements BaseExecutor<SelectStoneReqPb, SelectStoneResPb,Long> {
    @Override
    public SelectStoneResPb executor(SelectStoneReqPb arg, Long userId){
        log.info("SelectStoneExecutor::executor:userId = {},arg = {},start",userId,arg);
        SelectStoneResPb selectStoneResPb = new SelectStoneResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        if(userDTO != null){
            if(arg.getStoneType() == StoneEnum.green.getStoneType()){
                userDTO.setSelectStoneCount(0);
            }else{
                userDTO.setSelectStoneCount(userDTO.getSelectStoneCount() + 1);
            }
        }
        selectStoneResPb.setSelectStoneCount(userDTO.getSelectStoneCount());

        log.info("SelectStoneExecutor::executor:userId = {},selectStoneResPb = {},end",userId,selectStoneResPb);
        return selectStoneResPb;
    }
}
