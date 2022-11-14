package cn.coolcollege.fast.storage.mapper;

import cn.coolcollege.fast.storage.entity.AppResourceDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author bai bin
 */
@Repository
public interface AppResourceMapper extends Mapper<AppResourceDo> {
    /**
     * 根据domain查询resourceType信息
     *
     * @param appId
     * @param domainId
     * @return
     */
    List<String> getAppResourceType(@Param("appId") String appId, @Param("domainId") String domainId);

    /**
     * 查询所有domain
     */
    List<String> getAppAllDomain(@Param("appId") String appId);

    /**
     * 查询包含分类的资源类型
     */
    List<String> getIncludeClassifyResourceType(String appId,Boolean flag);
}
