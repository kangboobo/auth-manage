package cn.coolcollege.fast.entity.result;

import cn.coolcollege.fast.entity.EsOutputDto;
import lombok.ToString;
import net.coolcollege.platform.util.model.BasePageResult;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/7 20:13
 */
@ToString(callSuper = true)
public class EsOutputResult extends BasePageResult {

    private List<EsOutputDto> esOutputDtoList;

    public List<EsOutputDto> getEsOutputDtoList() {
        return esOutputDtoList;
    }

    public void setEsOutputDtoList(List<EsOutputDto> esOutputDtoList) {
        this.esOutputDtoList = esOutputDtoList;
    }

}
