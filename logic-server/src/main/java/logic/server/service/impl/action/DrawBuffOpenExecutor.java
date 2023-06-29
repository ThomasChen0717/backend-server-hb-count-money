package logic.server.service.impl.action;

import common.pb.enums.ErrorCodeEnum;
import common.pb.pb.DrawBuffOpenReqPb;
import common.pb.pb.DrawBuffOpenResPb;
import logic.server.dto.UserDrawDTO;
import logic.server.singleton.UserManagerSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DrawBuffOpenExecutor implements BaseExecutor<DrawBuffOpenReqPb, DrawBuffOpenResPb,Long>{
    @Override
    public DrawBuffOpenResPb executor(DrawBuffOpenReqPb arg, Long userId){
        log.info("DrawBuffOpenExecutor::executor:userId = {},arg = {},start",userId,arg);
        DrawBuffOpenResPb drawBuffOpenResPb = new DrawBuffOpenResPb();

        UserDrawDTO userDrawDTO = UserManagerSingleton.getInstance().getUserDrawFromCache(userId);
        if(userDrawDTO == null){
            log.info("DrawBuffOpenExecutor::executor:userId = {},userDrawDTO is null",userId);
            drawBuffOpenResPb.setCode(ErrorCodeEnum.drawCommonError.getCode()).setMessage(ErrorCodeEnum.drawCommonError.getMsg());
            return drawBuffOpenResPb;
        }
        userDrawDTO.setStickCount(2);

        log.info("DrawBuffOpenExecutor::executor:userId = {},drawBuffOpenResPb = {},end",userId,drawBuffOpenResPb);
        return drawBuffOpenResPb;
    }
}
