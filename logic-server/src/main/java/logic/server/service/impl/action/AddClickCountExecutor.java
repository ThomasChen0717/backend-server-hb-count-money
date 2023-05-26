package logic.server.service.impl.action;

import common.pb.pb.AddClickCountReqPb;
import common.pb.pb.AddClickCountResPb;
import logic.server.dto.UserDTO;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddClickCountExecutor implements BaseExecutor<AddClickCountReqPb, AddClickCountResPb,Long>{
    @Override
    public AddClickCountResPb executor(AddClickCountReqPb arg, Long userId){
        log.info("AddClickCountExecutor::executor:userId = {},arg = {},start",userId,arg);
        AddClickCountResPb addClickCountResPb = new AddClickCountResPb();

        UserDTO userDTO = UserManagerSingleton.getInstance().getUserByIdFromCache(userId);
        if(userDTO != null){
            userDTO.setClickCount(userDTO.getClickCount() + arg.getAddClickCount());
            if(userDTO.getClickCount() > 10) userDTO.setClickCount(10);
        }
        addClickCountResPb.setClickCount(userDTO.getClickCount());

        log.info("AddClickCountExecutor::executor:userId = {},addClickCountResPb = {},end",userId,addClickCountResPb);
        return addClickCountResPb;
    }
}
