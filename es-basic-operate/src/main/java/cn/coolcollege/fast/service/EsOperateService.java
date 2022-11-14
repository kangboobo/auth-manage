package cn.coolcollege.fast.service;

import cn.coolcollege.fast.dto.EsFieldsDto;
import cn.coolcollege.fast.dto.EsCommonDto;
import cn.coolcollege.fast.dto.EsResourceListDto;
import cn.coolcollege.fast.dto.EsUserIdDto;
import cn.coolcollege.fast.dto.EsUserIdListDto;
import cn.coolcollege.fast.dto.WrongMsgDo;
import cn.coolcollege.fast.entity.UserResourceDo;
import cn.coolcollege.fast.model.ResourceStatusDTO;
import net.coolcollege.platform.util.model.BaseResult;
import java.util.List;

/**
 * @author bai bin
 */
public interface EsOperateService {
    /**
     * 新增文档信息
     *
     * @param resources
     * @return
     */
    int addOrUpdateDocument(List<UserResourceDo> resources, String domainId);

    /**
     * 根据appid,enterpriseId,resourceId,resourceType查询一个资源信息
     *
     * @param esCommonDto
     * @return
     */
    UserResourceDo getOneUserResourceDo(EsCommonDto esCommonDto);

    /**
     * 根据appId,enterpriseId,resourceType和userId查询批量UserResourceDo
     *
     * @param userIdDto
     * @return
     */
    List<UserResourceDo> getResourceListByUserId(EsUserIdDto userIdDto);

    /**
     * 根据appId,enterpriseId,List<String> resourceIds,resourceType 集合查询List<UserResourceDo>
     *
     * @param resourceListDto
     *            必传参数 appId,enterpriseId,resourceType,List<resourceId>
     * @return
     */
    List<UserResourceDo> getResourceListByIds(EsResourceListDto resourceListDto);

    /**
     * 修改es中user_ids
     *
     * @param userIdListDto
     * @return true:修改成功 false:修改失败
     */
    Boolean updateEsUserIds(EsUserIdListDto userIdListDto);

    /**
     * 修改用户学习资源状态
     *
     * @param resourceStatusDTO
     * @return
     */
    Boolean updateResourceStudyStatus(ResourceStatusDTO resourceStatusDTO);

    /**
     * 批量删除ResourceId
     *
     * @param resourceListDto
     * @return true:删除成功,false:删除失败
     */
    Boolean deleteEsResourceInfo(EsResourceListDto resourceListDto);

    /**
     * 修改es data_dict字段mapping类型
     *
     * @return
     */
    BaseResult modifyEsDomainField(String domainId);

    /**
     * 修改业务特有参数
     *
     * @param businessFieldsDto
     *            修改业务特有参数
     * @return
     */
    Boolean updateEsBusinessField(EsFieldsDto businessFieldsDto);

    /**
     * @param fixedFieldsDto
     *            修改业务固定参数
     * @return
     */
    Boolean updateEsResourceFixedField(EsFieldsDto fixedFieldsDto);

    /**
     * 检测es index name是否存在
     * 
     * @param indexName
     *            index名称
     * @return 存在 true,不存在 false
     */
    Boolean checkEsIndexExist(String indexName);

    /**
     * 增加异常消息记录
     *
     * @param wrongMsgDo
     *            异常消息实体
     */
    void addWrongMsgRecord(WrongMsgDo wrongMsgDo);
}
