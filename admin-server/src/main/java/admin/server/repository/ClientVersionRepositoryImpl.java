package admin.server.repository;

import admin.server.dto.ClientVersionDTO;
import admin.server.mapper.ClientVersionMapper;
import admin.server.po.ClientVersionPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class ClientVersionRepositoryImpl implements ClientVersionRepository{

    private final ClientVersionMapper clientVersionMapper;

    @Override
    public Map<Long, ClientVersionDTO> getMap(){
        List<ClientVersionPO> clientVersionPOList = clientVersionMapper.selectList(new QueryWrapper<ClientVersionPO>()
                .lambda());
        List<ClientVersionDTO> clientVersionDTOList = Convertor.convert(clientVersionPOList, ClientVersionDTO.class);
        Map<Long, ClientVersionDTO> clientVersionDTOMap =  clientVersionDTOList.stream().collect(Collectors.toMap(ClientVersionDTO::getId, ClientVersionDTO -> ClientVersionDTO));
        return clientVersionDTOMap;
    }

    @Override
    public int add(ClientVersionDTO dto) {
        ClientVersionPO po = Convertor.convert(ClientVersionPO.class, dto);
        int result = clientVersionMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }

    @Override
    public int update(ClientVersionDTO dto){
        ClientVersionPO po = Convertor.convert(ClientVersionPO.class, dto);
        return clientVersionMapper.update(po, new QueryWrapper<ClientVersionPO>()
                .lambda().eq(ClientVersionPO::getId, po.getId()));
    }

    @Override
    public int deleteById(long userId){
        return clientVersionMapper.delete(new QueryWrapper<ClientVersionPO>()
                .lambda()
                .eq(ClientVersionPO::getId, userId)
        );
    }
}
