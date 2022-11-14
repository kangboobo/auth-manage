package cn.coolcollege.fast.storage.mapper;

import cn.coolcollege.fast.storage.entity.DomainFieldDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author bai bin
 * @Date 2021/5/13 16:13
 */
@Repository
public interface DomainFieldMapper extends Mapper<DomainFieldDo> {
    /**
     * 查询新增的领域字段类型
     *
     * @return
     */
    List<DomainFieldDo> getNewlyDomainFieldList(@Param("domainId") String domainId);

    /**
     * 修改domainField newlyAdd 为false
     *
     * @param domainFieldDos
     */
    void updateDomainFieldNewly(@Param(value = "list") List<DomainFieldDo> domainFieldDos);

    /**
     * 获取全部domain field name
     * @return
     */
    List<String> getFieldNameByDomainId(@Param("domainId") String domainId);
}
