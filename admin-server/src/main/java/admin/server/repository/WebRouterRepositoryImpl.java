package admin.server.repository;

import admin.server.dto.WebRouterDTO;
import admin.server.mapper.WebRouterMapper;
import admin.server.po.WebRouterPO;
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
public class WebRouterRepositoryImpl implements WebRouterRepository{
    private final WebRouterMapper webRouterMapper;

    @Override
    public Map<String, WebRouterDTO> getMap() {
        List<WebRouterPO> webRouterPOList = webRouterMapper.selectList(new QueryWrapper<WebRouterPO>()
                .lambda());
        List<WebRouterDTO> webRouterDTOListDTOList = Convertor.convert(webRouterPOList, WebRouterDTO.class);
        Map<String, WebRouterDTO> webRouterDTOMap = webRouterDTOListDTOList.stream().collect(Collectors.toMap(WebRouterDTO::getRouteName, WebRouterDTO -> WebRouterDTO));
        return webRouterDTOMap;
    }

    @Override
    public int add(WebRouterDTO dto) {
        WebRouterPO po = Convertor.convert(WebRouterPO.class, dto);
        int result = webRouterMapper.insert(po);
        dto.setId(po.getId());
        return result;
    }
    @Override
    public int update(WebRouterDTO dto){
        WebRouterPO po = Convertor.convert(WebRouterPO.class, dto);
        return webRouterMapper.update(po, new QueryWrapper<WebRouterPO>()
                .lambda().eq(WebRouterPO::getId, po.getId()));
    }
}
