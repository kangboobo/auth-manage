package cn.coolcollege.fast.service.impl;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.constants.ResourceAuthTypeEnum;
import cn.coolcollege.fast.constants.ResourceVisibleTypeEnum;
import cn.coolcollege.fast.dto.EsBaseResourceDo;
import cn.coolcollege.fast.dto.EsFieldsDto;
import cn.coolcollege.fast.dto.EsCommonDto;
import cn.coolcollege.fast.dto.EsResourceListDto;
import cn.coolcollege.fast.dto.EsUserIdDto;
import cn.coolcollege.fast.dto.EsUserIdListDto;
import cn.coolcollege.fast.dto.WrongMsgDo;
import cn.coolcollege.fast.entity.UserResourceDo;
import cn.coolcollege.fast.model.ResourceStatusDTO;
import cn.coolcollege.fast.storage.entity.DomainFieldDo;
import cn.coolcollege.fast.storage.mapper.DomainFieldMapper;
import cn.coolcollege.fast.util.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.coolcollege.platform.util.model.BaseResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bai bin
 */
@Service
public class EsOperateServiceImpl implements EsOperateService {

    private static final int MAX_CLASSIFY_SIZE = 100000;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private DomainFieldMapper fieldMapper;

    @Override
    public int addOrUpdateDocument(List<UserResourceDo> userResourceOuts, String domainId) {
        if (CollectionUtils.isEmpty(userResourceOuts) || StringUtils.isBlank(domainId)) {
            logger.error("es add or update params invalid");
            return 0;
        }
        return esBatchInsertOrUpdate(userResourceOuts, domainId);
    }

    @Override
    public UserResourceDo getOneUserResourceDo(EsCommonDto esCommonDto) {
        logger.info("es get one user resource params ={}", esCommonDto);
        UserResourceDo result = new UserResourceDo();
        if (esCommonDto == null || StringUtils.isAnyBlank(esCommonDto.getAppId(), esCommonDto.getEnterpriseId(),
            esCommonDto.getDomainId(), esCommonDto.getResourceId(), esCommonDto.getResourceType())) {
            logger.error("es get one user resource params invalid");
            return result;
        }
        String indexName = Constants.domainEsIndexMaps.get(esCommonDto.getDomainId());
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("domainId invalid,{}", esCommonDto.getDomainId());
            return result;
        }
        EsBaseResourceDo esBaseResourceDo =
            getEsSingleDocumentByEsId(indexName, splicingDocumentKey(esCommonDto.getAppId(),
                esCommonDto.getEnterpriseId(), esCommonDto.getResourceId(), esCommonDto.getResourceType()));
        if (esBaseResourceDo != null) {
            result = CommonUtils.stringConversionList(esBaseResourceDo);
        }
        return result;
    }

    @Override
    public List<UserResourceDo> getResourceListByIds(EsResourceListDto resourceListDto) {
        logger.info("es get resource list params={}", resourceListDto);
        List<UserResourceDo> result = new ArrayList<>();
        if (!checkEsResourceListDto(resourceListDto)) {
            logger.error("es get resource list params invalid");
            return result;
        }
        getEsResourceListByIds(resourceListDto, result);
        return result;
    }

    @Override
    public List<UserResourceDo> getResourceListByUserId(EsUserIdDto userIdDto) {
        logger.info("es get resource list by userId params={}", JSON.toJSONString(userIdDto));
        List<UserResourceDo> result = new ArrayList<>();
        if (userIdDto == null || StringUtils.isAnyBlank(userIdDto.getAppId(), userIdDto.getEnterpriseId(),
            userIdDto.getDomainId(), userIdDto.getResourceType(), userIdDto.getUserId())) {
            logger.error("es get resource list by userId params invalid");
            return result;
        }
        String indexName = Constants.domainEsIndexMaps.get(userIdDto.getDomainId());
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("domainId invalid,{}", userIdDto.getDomainId());
            return result;
        }
        ResourceAuthTypeEnum authTypeEnum = ResourceAuthTypeEnum.parseValue(userIdDto.getResourceAuthType());
        String userIdsName;
        String visibleTypeName;
        switch (authTypeEnum) {
            case EDIT:
                userIdsName = Constants.editUserIds;
                visibleTypeName = Constants.editVisibleType;
                break;
            case REFER:
                userIdsName = Constants.referUserIds;
                visibleTypeName = Constants.referVisibleType;
                break;
            default:
                userIdsName = Constants.userIds;
                visibleTypeName = Constants.visibleType;
                break;
        }
        SearchRequest searchRequest = new SearchRequest(indexName).types(Constants.TYPE_NAME);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder =
            QueryBuilders.boolQuery().must(QueryBuilders.termQuery(Constants.appId, userIdDto.getAppId()))
                .must(QueryBuilders.termQuery(Constants.enterpriseId, String.valueOf(userIdDto.getEnterpriseId())))
                .must(QueryBuilders.termQuery(Constants.resourceType, userIdDto.getResourceType()));
        BoolQueryBuilder visibleTypeBuilder = QueryBuilders.boolQuery();
        visibleTypeBuilder.should(QueryBuilders.termQuery(userIdsName, userIdDto.getUserId()))
            .should(QueryBuilders.termQuery(visibleTypeName, ResourceVisibleTypeEnum.ALL.getValue()));
        boolQueryBuilder.must(visibleTypeBuilder);
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.size(MAX_CLASSIFY_SIZE);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            logger.error("es get resource list by userId catch IOException", e);
            return result;
        }
        if (searchResponse != null && searchResponse.getHits() != null) {
            for (SearchHit hit : searchResponse.getHits()) {
                EsBaseResourceDo esBaseResourceDo = JSON.parseObject(hit.getSourceAsString(), EsBaseResourceDo.class);
                result.add(CommonUtils.stringConversionList(esBaseResourceDo));
            }
        }
        return result;
    }

    @Override
    public Boolean updateEsUserIds(EsUserIdListDto userIdListDto) {
        logger.info("es update by userIds params={}", userIdListDto);
        if (userIdListDto == null
            || StringUtils.isAnyBlank(userIdListDto.getAppId(), userIdListDto.getEnterpriseId(),
                userIdListDto.getDomainId(), userIdListDto.getResourceId(), userIdListDto.getResourceType())
            || CollectionUtils.isEmpty(userIdListDto.getUserIdList())) {
            logger.error("es update by userIds params invalid");
            return false;
        }
        String indexName = Constants.domainEsIndexMaps.get(userIdListDto.getDomainId());
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("domainId invalid,{}", userIdListDto.getDomainId());
            return false;
        }
        JSONObject updateJson = new JSONObject();
        updateJson.put(Constants.userIds, String.join(Constants.COMMA, userIdListDto.getUserIdList()));
        UpdateRequest request =
            new UpdateRequest(indexName, Constants.TYPE_NAME, splicingDocumentKey(userIdListDto.getAppId(),
                userIdListDto.getEnterpriseId(), userIdListDto.getResourceId(), userIdListDto.getResourceType()));
        request.doc(updateJson.toJSONString(), XContentType.JSON);
        UpdateResponse updateResponse;
        try {
            updateResponse = restHighLevelClient.update(request);
        } catch (IOException e) {
            logger.error("es update userIds catch IOException", e);
            return false;
        }
        return updateResponse != null;
    }

    @Override
    public Boolean updateResourceStudyStatus(ResourceStatusDTO resourceStatusDTO) {
        logger.info("update user study state params={}", resourceStatusDTO);
        if (resourceStatusDTO == null || StringUtils.isAnyBlank(resourceStatusDTO.getAppId(),
            resourceStatusDTO.getEId(), resourceStatusDTO.getDomainId(), resourceStatusDTO.getResourceType(),
            resourceStatusDTO.getResourceId(), resourceStatusDTO.getStatus(), resourceStatusDTO.getUserId())) {
            logger.info("update user study state params invalid");
            return false;
        }
        String indexName = Constants.domainEsIndexMaps.get(resourceStatusDTO.getDomainId());
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.error("domainId invalid,{}", resourceStatusDTO.getDomainId());
            return false;
        }
        // 先查询出对象资源信息
        String esId = splicingDocumentKey(resourceStatusDTO.getAppId(), resourceStatusDTO.getEId(),
            resourceStatusDTO.getResourceId(), resourceStatusDTO.getResourceType());
        EsBaseResourceDo esBaseResourceDo = getEsSingleDocumentByEsId(indexName, esId);
        if (esBaseResourceDo == null || esBaseResourceDo.getDataDict() == null
            || !esBaseResourceDo.getDataDict().containsKey(Constants.ES_QUERY_LEARNING_UIDS)
            || !esBaseResourceDo.getDataDict().containsKey(Constants.ES_QUERY_LEARNED_UIDS)) {
            logger.error("esBaseResourceDo invalid ={}", esBaseResourceDo);
            return false;
        }
        JSONObject dataDictJson = esBaseResourceDo.getDataDict();
        String subUserIds = null;
        String addUserIds;
        if (Constants.studying.equals(resourceStatusDTO.getStatus())) {
            addUserIds = dataDictJson.getString(Constants.ES_QUERY_LEARNING_UIDS);
        } else {
            subUserIds = dataDictJson.getString(Constants.ES_QUERY_LEARNING_UIDS);
            addUserIds = dataDictJson.getString(Constants.ES_QUERY_LEARNED_UIDS);
        }
        if (StringUtils.isNotBlank(subUserIds)) {
            List<String> subUserIdList = Arrays.asList(subUserIds.split(Constants.COMMA));
            if (subUserIdList.contains(resourceStatusDTO.getUserId())) {
                List<String> filterSubUserIds = subUserIdList.stream()
                    .filter(item -> !resourceStatusDTO.getUserId().equals(item)).collect(Collectors.toList());
                subUserIds = StringUtils.join(filterSubUserIds, Constants.COMMA);
            }
        }
        if (StringUtils.isNotBlank(addUserIds)) {
            List<String> addUserIdList = new ArrayList<>(Arrays.asList(addUserIds.split(Constants.COMMA)));
            if (!addUserIdList.contains(resourceStatusDTO.getUserId())) {
                addUserIdList.add(resourceStatusDTO.getUserId());
                addUserIds = StringUtils.join(addUserIdList, Constants.COMMA);
            }
        } else {
            addUserIds = resourceStatusDTO.getUserId();
        }
        UpdateRequest updateRequest = new UpdateRequest(indexName, Constants.TYPE_NAME, esId);
        Map<String, Object> parameters = new HashMap<>(16);
        // 使用painless语言和上面的参数创建一个内联脚本
        if (Constants.studying.equals(resourceStatusDTO.getStatus())) {
            // 学习中
            parameters.put("learning", addUserIds);
            String studyIdOrCode = "ctx._source.data_dict.learning_uids = params.learning";
            Script studyingScript = new Script(ScriptType.INLINE, "painless", studyIdOrCode, parameters);
            updateRequest.script(studyingScript);
        } else {
            // 已学习
            parameters.put("learning", subUserIds);
            parameters.put("learned", addUserIds);
            String finishedIdOrCode =
                "ctx._source.data_dict.learning_uids=params.learning;ctx._source.data_dict.learned_uids=params.learned";
            Script finishedScript = new Script(ScriptType.INLINE, "painless", finishedIdOrCode, parameters);
            updateRequest.script(finishedScript);
        }
        UpdateResponse updateResponse;
        try {
            /**
             * 当更新冲突时会重试10次
             */
            updateResponse = restHighLevelClient.update(updateRequest.retryOnConflict(10));
        } catch (IOException e) {
            logger.error("update user study state catch IOException", e);
            return false;
        }
        return updateResponse != null;
    }

    @Override
    public Boolean deleteEsResourceInfo(EsResourceListDto resourceListDto) {
        logger.info("es delete resource list params={}", resourceListDto);
        if (!checkEsResourceListDto(resourceListDto)) {
            logger.error("es delete resource list params invalid");
            return false;
        }
        String indexName = Constants.domainEsIndexMaps.get(resourceListDto.getDomainId());
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("domainId invalid,{}", resourceListDto.getDomainId());
            return false;
        }
        // 批量删除
        BulkRequest bulkDeleteRequest = new BulkRequest();
        for (String resourceId : resourceListDto.getResourceIds()) {
            DeleteRequest deleteRequest =
                new DeleteRequest(indexName, Constants.TYPE_NAME, splicingDocumentKey(resourceListDto.getAppId(),
                    resourceListDto.getEnterpriseId(), resourceId, resourceListDto.getResourceType()));
            bulkDeleteRequest.add(deleteRequest);
        }
        BulkResponse bulkDeleteResponse;
        try {
            bulkDeleteResponse = restHighLevelClient.bulk(bulkDeleteRequest);
        } catch (IOException e) {
            logger.error("es delete resource list catch IOException", e);
            return false;
        }
        return bulkDeleteResponse != null && !bulkDeleteResponse.hasFailures();
    }

    @Override
    public void addWrongMsgRecord(WrongMsgDo wrongMsgDo) {
        logger.info("add wrong msg params={}", wrongMsgDo);
        IndexRequest indexRequest = new IndexRequest(Constants.WRONG_MSG_INDEX_NAME, Constants.WRONG_MSG_TYPE_NAME);
        indexRequest.source(JSON.toJSONString(wrongMsgDo), XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest);
        } catch (IOException e) {
            logger.error("add wrong msg catch IOException", e);
        }
    }

    @Override
    public BaseResult modifyEsDomainField(String domainId) {
        logger.info("modify es domainId field start");
        BaseResult result = new BaseResult();
        result.setSuccess(false);
        result.setMessage("es modify domain field failed");
        String indexName = Constants.domainEsIndexMaps.get(domainId);
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("domainId invalid");
            result.setMessage("domainId invalid");
            return result;
        }
        List<DomainFieldDo> needModifyList = fieldMapper.getNewlyDomainFieldList(domainId);
        if (CollectionUtils.isEmpty(needModifyList)) {
            logger.info("not have need modify domain field,{}", needModifyList);
            result.setMessage("not have need modify domain field");
            return result;
        }
        JSONObject settingsJson = new JSONObject();
        JSONObject dataDictJson = new JSONObject();
        JSONObject nestedJson = new JSONObject();
        JSONObject domainJson = new JSONObject();
        for (DomainFieldDo fieldDo : needModifyList) {
            JSONObject fieldJson = new JSONObject();
            fieldJson.put(Constants.ES_TYPE, fieldDo.getValueType());
            if (fieldDo.getAnalyzer() != null) {
                if (fieldDo.getAnalyzer() == 0) {
                    // 英文逗号分词
                    fieldJson.put(Constants.ES_ANALYZER, Constants.ES_COMMA_ANALYZER);
                } else if (fieldDo.getAnalyzer() == 1) {
                    // 空白分词器
                    fieldJson.put(Constants.ES_ANALYZER, Constants.ES_SPACE_ANALYZER);
                }
                // 其他分词器待补充
            }
            domainJson.put(fieldDo.getField(), fieldJson);
        }
        nestedJson.put(Constants.ES_TYPE, Constants.ES_NESTED);
        nestedJson.put(Constants.ES_PROPERTIES, domainJson);
        dataDictJson.put(Constants.ES_DATA_DICT, nestedJson);
        settingsJson.put(Constants.ES_PROPERTIES, dataDictJson);
        PutMappingRequest mappingRequest = new PutMappingRequest(Constants.CLASS_DOMAIN_INDEX_NAME)
            .type(Constants.TYPE_NAME).source(settingsJson.toJSONString(), XContentType.JSON);
        PutMappingResponse putMappingResponse;
        try {
            putMappingResponse = restHighLevelClient.indices().putMapping(mappingRequest);
        } catch (IOException e) {
            logger.error("es update index catch IOException", e);
            result.setMessage("es update index catch IOException");
            return result;
        }
        if (putMappingResponse != null && putMappingResponse.isAcknowledged()) {
            result.setSuccess(true);
            // 修改
            fieldMapper.updateDomainFieldNewly(needModifyList);
        }
        return result;
    }

    @Override
    public Boolean updateEsBusinessField(EsFieldsDto businessFieldsDto) {
        logger.info("update es business field param,{}", businessFieldsDto);
        if (businessFieldsDto == null
            || StringUtils.isAnyBlank(businessFieldsDto.getAppId(), businessFieldsDto.getEnterpriseId(),
                businessFieldsDto.getDomainId(), businessFieldsDto.getResourceType())
            || CollectionUtils.isEmpty(businessFieldsDto.getResourceIds()) || businessFieldsDto.getParamsMap() == null
            || businessFieldsDto.getParamsMap().isEmpty()) {
            logger.info("update es business params is invalid");
            return false;
        }
        String indexName = Constants.domainEsIndexMaps.get(businessFieldsDto.getDomainId());
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("domainId invalid,{}", businessFieldsDto.getDomainId());
            return false;
        }
        // 判断key是否在字典表中
        Map<String, Object> parametersMap = businessFieldsDto.getParamsMap();
        BulkRequest bulkUpdateRequest = new BulkRequest();
        for (String resourceId : businessFieldsDto.getResourceIds()) {
            UpdateRequest updateRequest =
                new UpdateRequest(indexName, Constants.TYPE_NAME, splicingDocumentKey(businessFieldsDto.getAppId(),
                    businessFieldsDto.getEnterpriseId(), resourceId, businessFieldsDto.getResourceType()));
            // 使用painless语言和上面的参数创建一个内联脚本
            StringBuilder finishedIdBuilder = new StringBuilder(StringUtils.EMPTY);
            String template = "ctx._source.data_dict.%s=params.%s;";
            for (Map.Entry<String, Object> paramEntry : parametersMap.entrySet()) {
                finishedIdBuilder.append(String.format(template, paramEntry.getKey(), paramEntry.getKey()));
            }
            Script finishedScript =
                new Script(ScriptType.INLINE, "painless", finishedIdBuilder.toString(), parametersMap);
            updateRequest.script(finishedScript);
            bulkUpdateRequest.add(updateRequest);
        }
        BulkResponse bulkUpdateResponse;
        try {
            bulkUpdateResponse = restHighLevelClient.bulk(bulkUpdateRequest);
        } catch (IOException e) {
            logger.error("es catch IOException", e);
            return false;
        }
        if (bulkUpdateResponse != null && bulkUpdateResponse.hasFailures()) {
            return false;
        }
        return bulkUpdateResponse != null;
    }

    @Override
    public Boolean updateEsResourceFixedField(EsFieldsDto fixedFieldsDto) {
        logger.info("update es fixed field param,{}", fixedFieldsDto);
        if (fixedFieldsDto == null
            || StringUtils.isAnyBlank(fixedFieldsDto.getAppId(), fixedFieldsDto.getEnterpriseId(),
                fixedFieldsDto.getDomainId(), fixedFieldsDto.getResourceType())
                && CollectionUtils.isEmpty(fixedFieldsDto.getResourceIds())
            || fixedFieldsDto.getParamsMap() == null || fixedFieldsDto.getParamsMap().isEmpty()) {
            logger.info("update es fixed params is invalid");
            return false;
        }
        String indexName = Constants.domainEsIndexMaps.get(fixedFieldsDto.getDomainId());
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("domainId invalid,{}", fixedFieldsDto.getDomainId());
            return false;
        }
        BulkRequest bulkUpdateRequest = new BulkRequest();
        for (String resourceId : fixedFieldsDto.getResourceIds()) {
            JSONObject updateJson = new JSONObject();
            for (Map.Entry<String, Object> entry : fixedFieldsDto.getParamsMap().entrySet()) {
                updateJson.put(entry.getKey(), entry.getValue());
            }
            UpdateRequest updateRequest =
                new UpdateRequest(indexName, Constants.TYPE_NAME, splicingDocumentKey(fixedFieldsDto.getAppId(),
                    fixedFieldsDto.getEnterpriseId(), resourceId, fixedFieldsDto.getResourceType()));
            updateRequest.doc(updateJson.toJSONString(), XContentType.JSON);
            bulkUpdateRequest.add(updateRequest);
        }
        BulkResponse bulkUpdateResponse;
        try {
            bulkUpdateResponse = restHighLevelClient.bulk(bulkUpdateRequest);
        } catch (IOException e) {
            logger.error("es update fixed params catch IOException", e);
            return false;
        }
        if (bulkUpdateResponse != null && bulkUpdateResponse.hasFailures()) {
            return false;
        }
        return bulkUpdateResponse != null;
    }

    @Override
    public Boolean checkEsIndexExist(String indexName) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        try {
            return restHighLevelClient.indices().exists(request);
        } catch (IOException e) {
            logger.error("check index exist catch Exception,{}", e.getMessage());
            return false;
        }
    }

    /**
     * es批量新增或修改文档
     *
     * @param userResourceOutList
     *            批量新增或修改参数集合
     * @return 批量添加或修改成功返回 true,失败返回false
     */
    private int esBatchInsertOrUpdate(List<UserResourceDo> userResourceOutList, String domainId) {
        String indexName = Constants.domainEsIndexMaps.get(domainId);
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("es domainId invalid,{}", domainId);
            return -4;
        }
        BulkRequest bulkInsertOrUpdateRequest = new BulkRequest();
        for (UserResourceDo userResourceDo : userResourceOutList) {
            if (!checkUserResourceOutParam(userResourceDo)) {
                logger.info("es batch insert or update params = {}", userResourceDo);
                continue;
            }
            IndexRequest indexRequest = new IndexRequest(indexName, Constants.TYPE_NAME)
                .id(splicingDocumentKey(userResourceDo.getAppId(), userResourceDo.getEnterpriseId(),
                    userResourceDo.getResourceId(), userResourceDo.getResourceType()));
            // CommonUtils.JsonFormatChange(resource) 将对象中字段驼峰命名转为下划线命名，时间格式转换
            indexRequest.source(CommonUtils.jsonFormatChange(CommonUtils.listConversionString(userResourceDo)),
                XContentType.JSON);
            bulkInsertOrUpdateRequest.add(indexRequest);
        }
        if (bulkInsertOrUpdateRequest.numberOfActions() <= 0) {
            logger.info("this batch params invalid,{}", userResourceOutList);
            return -3;
        }
        BulkResponse bulkInsertOrUpdateResponse;
        try {
            bulkInsertOrUpdateResponse = restHighLevelClient.bulk(bulkInsertOrUpdateRequest);
        } catch (IOException e) {
            logger.error("Es batch add or update catch IOException", e);
            return -2;
        }
        if (bulkInsertOrUpdateResponse != null && bulkInsertOrUpdateResponse.hasFailures()) {
            for (BulkItemResponse bulkItemResponse : bulkInsertOrUpdateResponse) {
                if ((bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                    || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE
                    || bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) && bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    logger.error("batch add or update failed= {}", failure);
                }
            }
            return -1;
        } else if (bulkInsertOrUpdateResponse == null) {
            logger.error("batch add or update result is null");
            return -1;
        }
        return bulkInsertOrUpdateResponse.getItems().length;
    }

    /**
     * 根据批量es id 查询es resource info
     *
     * @param resourceListDto
     * @param userResourceDos
     */
    private void getEsResourceListByIds(EsResourceListDto resourceListDto, List<UserResourceDo> userResourceDos) {
        String indexName = Constants.domainEsIndexMaps.get(resourceListDto.getDomainId());
        if (StringUtils.isBlank(indexName) || !checkEsIndexExist(indexName)) {
            logger.info("domainId invalid,{}", resourceListDto.getDomainId());
            return;
        }
        List<String> stringList = new ArrayList<>();
        for (String resourceId : resourceListDto.getResourceIds()) {
            if (StringUtils.isBlank(resourceId)) {
                logger.error("resourceId is empty,{}", resourceId);
                return;
            }
            stringList.add(splicingDocumentKey(resourceListDto.getAppId(), resourceListDto.getEnterpriseId(),
                resourceId, resourceListDto.getResourceType()));
        }
        SearchRequest searchRequest = new SearchRequest(indexName).types(Constants.TYPE_NAME);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery().addIds(stringList.toArray(new String[0]));
        sourceBuilder.query(idsQueryBuilder);
        sourceBuilder.size(stringList.size());
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            logger.error("es get resourceId and userIdList catch IOException", e);
            return;
        }
        if (searchResponse != null && searchResponse.getHits() != null && searchResponse.getHits().getTotalHits() > 0) {
            for (SearchHit hit : searchResponse.getHits()) {
                EsBaseResourceDo esBaseResourceDo = JSON.parseObject(hit.getSourceAsString(), EsBaseResourceDo.class);
                if (esBaseResourceDo != null) {
                    userResourceDos.add(CommonUtils.stringConversionList(esBaseResourceDo));
                }
            }
        }
    }

    /**
     * 根据esId查询单条document信息
     *
     * @param esId
     * @return
     */
    private EsBaseResourceDo getEsSingleDocumentByEsId(String indexName, String esId) {
        GetRequest request = new GetRequest(indexName, Constants.TYPE_NAME, esId);
        EsBaseResourceDo result = null;
        try {
            GetResponse response = restHighLevelClient.get(request);
            if (response != null && response.isExists()) {
                result = JSON.parseObject(response.getSourceAsString(), EsBaseResourceDo.class);
            }
        } catch (IOException e) {
            logger.error("es get resourceDo catch IOException", e);
        }
        return result;
    }

    /**
     * 检测参数
     *
     * @param userResourceDo
     * @return success : true, failed : false
     */
    private boolean checkUserResourceOutParam(UserResourceDo userResourceDo) {
        return userResourceDo != null && StringUtils.isNoneEmpty(userResourceDo.getAppId(),
            userResourceDo.getEnterpriseId(), userResourceDo.getResourceId(), userResourceDo.getResourceType());
    }

    private boolean checkEsResourceListDto(EsResourceListDto resourceListDto) {
        return resourceListDto != null
            && StringUtils.isNoneBlank(resourceListDto.getAppId(), resourceListDto.getEnterpriseId(),
                resourceListDto.getDomainId(), resourceListDto.getDomainId(), resourceListDto.getResourceType())
            && CollectionUtils.isNotEmpty(resourceListDto.getResourceIds());
    }

    /**
     * 拼接document key
     *
     * @param appId
     *            appId
     * @param enterpriseId
     *            企业Id
     * @param resourceId
     *            资源id
     * @param resourceType
     *            资源类型
     * @return 拼接成es index 主键值
     */
    private String splicingDocumentKey(String appId, String enterpriseId, String resourceId, String resourceType) {
        return appId + "-" + enterpriseId + "-" + resourceId + "-" + resourceType;
    }
}
