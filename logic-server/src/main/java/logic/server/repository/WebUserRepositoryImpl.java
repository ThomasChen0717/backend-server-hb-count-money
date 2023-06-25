package logic.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import logic.server.dto.WebUserDTO;
import logic.server.mapper.WebUserMapper;
import logic.server.po.WebUserPO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class WebUserRepositoryImpl implements WebUserRepository{
    private final WebUserMapper webUserMapper;

    @Override
    public Map<String, WebUserDTO> getMap() {
        List<WebUserPO> webUserPOList = webUserMapper.selectList(new QueryWrapper<WebUserPO>()
                .lambda());
        List<WebUserDTO> webUserDTOListDTOList = Convertor.convert(webUserPOList, WebUserDTO.class);
        Map<String, WebUserDTO> webUserDTOMap = webUserDTOListDTOList.stream().collect(Collectors.toMap(WebUserDTO::getUsername,WebUserDTO -> WebUserDTO));
        return webUserDTOMap;
    }

    @Override
    public int add(WebUserDTO dto) {
        WebUserPO po = Convertor.convert(WebUserPO.class, dto);
        int result = webUserMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }
}
