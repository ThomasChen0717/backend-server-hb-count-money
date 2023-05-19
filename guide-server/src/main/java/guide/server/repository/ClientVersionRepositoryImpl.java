package guide.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import guide.server.dto.ClientVersionDTO;
import guide.server.mapper.ClientVersionMapper;
import guide.server.po.ClientVersionPO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class ClientVersionRepositoryImpl implements ClientVersionRepository{
    private final ClientVersionMapper clientVersionMapper;
    @Override
    public ClientVersionDTO get(String version){
        ClientVersionPO po = clientVersionMapper.selectOne(new QueryWrapper<ClientVersionPO>()
                .lambda().eq(ClientVersionPO::getClientVersion, version));
        return Convertor.convert(ClientVersionDTO.class, po);
    }
}
