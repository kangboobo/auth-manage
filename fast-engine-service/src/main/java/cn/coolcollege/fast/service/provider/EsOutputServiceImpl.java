package cn.coolcollege.fast.service.provider;

import java.util.*;
import java.util.stream.Collectors;

import cn.coolcollege.fast.dto.EsCommonDto;
import com.google.common.collect.Maps;
import net.coolcollege.authority.facade.constants.RangeTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.runtime.api.annotation.SofaService;
import com.alipay.sofa.runtime.api.annotation.SofaServiceBinding;
import com.google.common.collect.Sets;

import cn.coolcollege.fast.constants.*;
import cn.coolcollege.fast.dto.EsUserIdDto;
import cn.coolcollege.fast.util.CommonUtils;
import net.coolcollege.platform.util.ErrMsgUtil;
import net.coolcollege.platform.util.annonation.MonitorLog;
import net.coolcollege.platform.util.constants.CommonConstants;

/**
 * @Author bai bin
 * @Date 2021/5/8 10:21
 */
@Service
@SofaService(uniqueId = FastEngineUniqueIdConstants.ES_OUTPUT_UNIQUE_ID, interfaceType = EsOutputService.class,
    bindings = {@SofaServiceBinding(bindingType = CommonConstants.SOFA_BINDING_TYPE)})
public class EsOutputServiceImpl implements EsOutputService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private EsOperateService esOperateService;

    @Autowired
    private CalculateServiceHelper calculateServiceHelper;

    @MonitorLog
    @Override
    public EsOutputResult getPageEsOutputResourceInfo(EsOutputRequest esOutputRequest) {
        if (logger.isInfoEnabled()) {
            logger.info("esOutputRequest,{}", JSON.toJSONString(esOutputRequest));
        }
        EsOutputResult result = new EsOutputResult();
        if (!checkEsOutputRequestParam(esOutputRequest)) {
            ErrMsgUtil.setErrMsg(result, FastEngineErrConstants.INVALID_PARAM_ERR);
            return result;
        }
        queryEsPageBySort(esOutputRequest, result, null);
        return result;
    }

    @Override
    @MonitorLog
    public EsOutGroupCountResult getResourceGroupCount(EsOutputRequest esOutputRequest) {
        if (logger.isInfoEnabled()) {
            logger.info("getResourceGroupCount,{}", JSON.toJSONString(esOutputRequest));
        }
        EsOutGroupCountResult outCountResult = new EsOutGroupCountResult();
        if (!checkEsOutputRequestParam(esOutputRequest)) {
            ErrMsgUtil.setErrMsg(outCountResult, FastEngineErrConstants.INVALID_PARAM_ERR);
            return outCountResult;
        }
        queryEsPageBySort(esOutputRequest, null, outCountResult);
        return outCountResult;
    }

    @Override
    public GetUserVisibleClassifyIdsResult
        getUserVisibleClassifyIds(GetUserVisibleClassifyIdsRequest userVisibleClassifyIdsRequest) {
        logger.info("userVisibleClassifyIdsRequest,{}", JSON.toJSONString(userVisibleClassifyIdsRequest));
        GetUserVisibleClassifyIdsResult result = new GetUserVisibleClassifyIdsResult();
        if (userVisibleClassifyIdsRequest == null || StringUtils.isAnyBlank(userVisibleClassifyIdsRequest.getAppId(),
            userVisibleClassifyIdsRequest.getDomainId(), userVisibleClassifyIdsRequest.getResourceType(),
            userVisibleClassifyIdsRequest.getUserId()) || userVisibleClassifyIdsRequest.getEnterpriseId() == null) {
            ErrMsgUtil.setErrMsg(result, FastEngineErrConstants.INVALID_PARAM_ERR);
            return result;
        }
        // 查询es
        EsUserIdDto esUserIdDto = new EsUserIdDto();
        esUserIdDto.setAppId(userVisibleClassifyIdsRequest.getAppId());
        esUserIdDto.setEnterpriseId(String.valueOf(userVisibleClassifyIdsRequest.getEnterpriseId()));
        esUserIdDto.setDomainId(userVisibleClassifyIdsRequest.getDomainId());
        esUserIdDto.setResourceType(userVisibleClassifyIdsRequest.getResourceType());
        esUserIdDto.setUserId(userVisibleClassifyIdsRequest.getUserId());
        if (StringUtils.isBlank(userVisibleClassifyIdsRequest.getResourceAuthType())) {
            esUserIdDto.setResourceAuthType(ResourceAuthTypeEnum.QUERY.getValue());
        } else {
            esUserIdDto.setResourceAuthType(userVisibleClassifyIdsRequest.getResourceAuthType());
        }
        // 调用es根据用户id查询资源详情接口
        List<UserResourceDo> userResourceDoList = esOperateService.getResourceListByUserId(esUserIdDto);
        Set<String> classifyIds = Sets.newHashSet();
        Set<String> parentClassifyIds = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(userResourceDoList)) {
            userResourceDoList.forEach(userResourceDo -> {
                classifyIds.add(userResourceDo.getResourceId());
                if (CollectionUtils.isNotEmpty(userResourceDo.getClassifyIds())) {
                    Set<String> resourceDoClassifyIds =
                        userResourceDo.getClassifyIds().stream().map(String::valueOf).collect(Collectors.toSet());
                    if (CollectionUtils.isNotEmpty(resourceDoClassifyIds)) {
                        parentClassifyIds.addAll(resourceDoClassifyIds);
                    }
                }
            });
        }
        result.setClassifyIds(classifyIds);
        result.setParentClassifyIds(parentClassifyIds);
        return result;
    }

    @Override
    public CheckResourceVisibleResult checkResourceVisible(CheckResourceVisibleRequest request) {
        logger.info("checkResourceVisible request={}", JSON.toJSONString(request));
        CheckResourceVisibleResult result = new CheckResourceVisibleResult();
        result.setChecked(CommonConstants.FALSE);
        String rangeType = calculateServiceHelper.getAuthorityRangeTypeByUserId(request.getAppId(),
            request.getEnterpriseId(), Long.valueOf(request.getUserId()));

        if (RangeTypeEnum.ALL.getValue().equals(rangeType)) {
            result.setChecked(CommonConstants.TRUE);
            return result;
        }

        String indexName = Constants.domainEsIndexMaps.get(request.getDomainId());
        if (StringUtils.isEmpty(indexName) || !esOperateService.checkEsIndexExist(indexName)) {
            ErrMsgUtil.setErrMsg(result, FastEngineErrConstants.DOMAIN_ID_ERR);
            return result;
        }
        EsCommonDto esCommonDto = new EsCommonDto();
        esCommonDto.setAppId(request.getAppId());
        esCommonDto.setEnterpriseId(String.valueOf(request.getEnterpriseId()));
        esCommonDto.setDomainId(request.getDomainId());
        esCommonDto.setResourceType(request.getResourceType());
        esCommonDto.setResourceId(request.getResourceId());
        UserResourceDo oneUserResourceDo = esOperateService.getOneUserResourceDo(esCommonDto);
        if (oneUserResourceDo != null) {
            if (ResourceVisibleTypeEnum.ALL.getValue().equals(oneUserResourceDo.getVisibleType())) {
                result.setChecked(CommonConstants.TRUE);
            } else {
                List<String> queryUserIds = oneUserResourceDo.getUserIds();
                if (CollectionUtils.isNotEmpty(queryUserIds) && queryUserIds.contains(request.getUserId())) {
                    result.setChecked(CommonConstants.TRUE);
                }
            }
        }
        return result;
    }

    /**
     * 分页查询es相关信息
     *
     * @param esOutputRequest
     *            es 请求参数信息
     * @param esOutputResult
     *            es 结果详情信息
     * @param outCountResult
     *            es 结果数量
     */
    private void queryEsPageBySort(EsOutputRequest esOutputRequest, EsOutputResult esOutputResult,
        EsOutGroupCountResult outCountResult) {
        try {
            String indexName = Constants.domainEsIndexMaps.get(esOutputRequest.getDomainId());
            if (StringUtils.isEmpty(indexName) || !esOperateService.checkEsIndexExist(indexName)) {
                if (esOutputResult != null) {
                    ErrMsgUtil.setErrMsg(esOutputResult, FastEngineErrConstants.DOMAIN_ID_ERR);
                } else {
                    ErrMsgUtil.setErrMsg(outCountResult, FastEngineErrConstants.DOMAIN_ID_ERR);
                }
                return;
            }
            SearchRequest searchRequest = new SearchRequest(indexName).types(Constants.TYPE_NAME);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            // 处理es查询参数
            dealEsCommonParameter(esOutputRequest, sourceBuilder);
            if (outCountResult != null && StringUtils.isNotBlank(esOutputRequest.getGroupFiled())) {
                Map<String, Long> esOutCountMap = Maps.newHashMap();
                // 分组统计类型count
                String groupValue = esOutputRequest.getGroupFiled();
                if (groupValue.contains(Constants.FILL_POINT)) {
                    AggregationBuilder nestedAggregationBuilder =
                        AggregationBuilders.nested(Constants.NESTED_AGG, Constants.DATA_DICT);
                    TermsAggregationBuilder termsBuilder = AggregationBuilders.terms(Constants.GROUP_FIELD)
                        .field(stitchFuzzyMatchesKey(Constants.ES_DATA_DICT + groupValue));
                    termsBuilder.size(Constants.GROUP_BUCKET_COUNT);
                    nestedAggregationBuilder.subAggregation(termsBuilder);
                    sourceBuilder.aggregation(nestedAggregationBuilder);
                } else {
                    TermsAggregationBuilder termsBuilder =
                        AggregationBuilders.terms(Constants.GROUP_FIELD).field(stitchFuzzyMatchesKey(groupValue));
                    termsBuilder.size(Constants.GROUP_BUCKET_COUNT);
                    sourceBuilder.aggregation(termsBuilder);
                }
                sourceBuilder.size(CommonConstants.ZERO_VALUE_INT);
                searchRequest.source(sourceBuilder);
                SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
                if (searchResponse == null || searchResponse.getAggregations() == null) {
                    logger.info("query es count group by {} response is null", groupValue);
                    outCountResult.setGroupCountMap(esOutCountMap);
                    outCountResult.setResultCode(FastEngineErrConstants.ES_RESULT_IS_NULL.getErrCode());
                    outCountResult.setMessage(FastEngineErrConstants.ES_RESULT_IS_NULL.getErrMessage());
                } else {
                    if (groupValue.contains(Constants.FILL_POINT)) {
                        Nested nestedAgg = searchResponse.getAggregations().get(Constants.NESTED_AGG);
                        Terms nestedName = nestedAgg.getAggregations().get(Constants.GROUP_FIELD);
                        for (Terms.Bucket bucket : nestedName.getBuckets()) {
                            esOutCountMap.put(bucket.getKeyAsString(), bucket.getDocCount());
                        }
                    } else {
                        Terms baseTerms = searchResponse.getAggregations().get(Constants.GROUP_FIELD);
                        for (Terms.Bucket entry : baseTerms.getBuckets()) {
                            esOutCountMap.put(entry.getKeyAsString(), entry.getDocCount());
                        }
                    }
                    if (searchResponse.getHits() != null) {
                        outCountResult.setTotal(searchResponse.getHits().getTotalHits());
                    }
                    outCountResult.setGroupCountMap(esOutCountMap);
                }
            } else {
                List<EsOutputDto> resourceOuts = new ArrayList<>();
                // 正常列表分页查询
                // 如果pageSize为-1时代表查询全部id，此时不需分页，不用传from和size
                if (!Constants.MINUS_ONE_VALUE.equals(esOutputRequest.getPageSize())) {
                    // es此处需要传入游标，游标 = (pageNum - 1) * pageSize
                    int esPageFrom = (esOutputRequest.getPageNum() - 1) * esOutputRequest.getPageSize();
                    sourceBuilder.from(esPageFrom);
                    sourceBuilder.size(esOutputRequest.getPageSize());
                } else {
                    sourceBuilder.size(Constants.MAX_RESOURCE_SIZE);
                }
                // 处理es排序参数
                dealEsSortOrderParam(esOutputRequest, sourceBuilder);
                // 第一个是获取字段，第二个是过滤的字段，默认获取全部
                sourceBuilder.fetchSource(new String[] {Constants.resourceId, Constants.resourceTitle, Constants.desc,
                    Constants.createUserId, Constants.createUserName, Constants.createTs, Constants.ES_DATA_DICT},
                    new String[] {});
                searchRequest.source(sourceBuilder);
                SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
                if (searchResponse == null || searchResponse.getHits() == null
                    || searchResponse.getHits().getHits().length <= 0) {
                    logger.info("query results,{}", resourceOuts);
                    esOutputResult.setEsOutputDtoList(resourceOuts);
                    esOutputResult.setResultCode(FastEngineErrConstants.ES_RESULT_IS_NULL.getErrCode());
                    esOutputResult.setMessage(FastEngineErrConstants.ES_RESULT_IS_NULL.getErrMessage());
                } else {
                    for (SearchHit hit : searchResponse.getHits()) {
                        EsOutputDto esOutputDto = JSON.parseObject(hit.getSourceAsString(), EsOutputDto.class);
                        resourceOuts.add(esOutputDto);
                    }
                    esOutputResult.setEsOutputDtoList(resourceOuts);
                    if (!Constants.MINUS_ONE_VALUE.equals(esOutputRequest.getPageSize())) {
                        // 结果总数
                        long total = searchResponse.getHits().getTotalHits();
                        esOutputResult.setTotal(total);
                        // 本次返回真实总数
                        esOutputResult.setActualSize(searchResponse.getHits().getHits().length);
                        esOutputResult.setPageNum(esOutputRequest.getPageNum());
                        esOutputResult.setPageSize(esOutputRequest.getPageSize());
                        // 总页数
                        int pages = (int)(total % esOutputRequest.getPageSize() == 0
                            ? total / esOutputRequest.getPageSize() : total / esOutputRequest.getPageSize() + 1);
                        esOutputResult.setPages(pages);
                        // 请求游标
                        int requestCursor = esOutputRequest.getPageNum() * esOutputRequest.getPageSize();
                        if (total > requestCursor) {
                            esOutputResult.setHasNextPage(true);
                            esOutputResult.setNextPage(esOutputRequest.getPageNum() + 1);
                        } else {
                            esOutputResult.setHasNextPage(false);
                            esOutputResult.setNextPage(0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("es query index catch exception,{}", e.getMessage());
            ErrMsgUtil.setErrMsg(esOutputResult, FastEngineErrConstants.ES_CATCH_EXCEPTION);
        }
    }

    /**
     * 处理es公共参数
     *
     * @param esOutputRequest
     * @param sourceBuilder
     */
    private void dealEsCommonParameter(EsOutputRequest esOutputRequest, SearchSourceBuilder sourceBuilder) {
        BoolQueryBuilder baseQueryBuilder =
            QueryBuilders.boolQuery().must(QueryBuilders.termQuery(Constants.appId, esOutputRequest.getAppId())).must(
                QueryBuilders.termQuery(Constants.enterpriseId, String.valueOf(esOutputRequest.getEnterpriseId())));
        // 如果resourceTypes不为空时,resourceType为List,需使用termsQuery,跟mysql中in功能相似
        if (CollectionUtils.isNotEmpty(esOutputRequest.getResourceTypes())) {
            baseQueryBuilder.must(QueryBuilders.termsQuery(Constants.resourceType, esOutputRequest.getResourceTypes()));
        }
        // 当inResourceIds不为空时
        if (CollectionUtils.isNotEmpty(esOutputRequest.getInResourceIds())) {
            baseQueryBuilder.must(QueryBuilders.termsQuery(Constants.resourceId, esOutputRequest.getInResourceIds()));
        }
        // 当excludeResourceIds不为空时
        if (CollectionUtils.isNotEmpty(esOutputRequest.getExcludeResourceIds())) {
            baseQueryBuilder
                .mustNot(QueryBuilders.termsQuery(Constants.resourceId, esOutputRequest.getExcludeResourceIds()));
        }
        if (esOutputRequest.getOnlyMe()) {
            // 只查询我的资源时，用户id为create_user_id,该查询会把userId查询和资源可见过滤都短路掉
            baseQueryBuilder.must(QueryBuilders.termQuery(Constants.createUserId, esOutputRequest.getUserId()));
        }
        if (StringUtils.isNotBlank(esOutputRequest.getUserId())
            && Constants.SUPER_USER_ID.equals(esOutputRequest.getUserId())) {
            esOutputRequest.setQueryAll(true);
        }
        // 管理员可以看见全部资源，不需要考虑资源是全部可见还是部分可见
        if (!esOutputRequest.getQueryAll()) {
            // 普通用户查询时，其可见资源范围为 全部可见资源 OR 其有权限可见的资源
            BoolQueryBuilder visibleTypeBuilder = QueryBuilders.boolQuery();
            ResourceAuthTypeEnum authTypeEnum = ResourceAuthTypeEnum.parseValue(esOutputRequest.getResourceAuthType());
            String visibleTypeName;
            String userIdName;
            switch (authTypeEnum) {
                case EDIT:
                    visibleTypeName = Constants.editVisibleType;
                    userIdName = Constants.editUserIds;
                    break;
                case REFER:
                    visibleTypeName = Constants.referVisibleType;
                    userIdName = Constants.referUserIds;
                    break;
                default:
                    visibleTypeName = Constants.visibleType;
                    userIdName = Constants.userIds;
                    break;
            }
            visibleTypeBuilder.should(QueryBuilders.termQuery(visibleTypeName, ResourceVisibleTypeEnum.ALL.getValue()))
                .should(QueryBuilders.termQuery(userIdName, esOutputRequest.getUserId()));
            baseQueryBuilder.must(visibleTypeBuilder);
        }

        BoolQueryBuilder andBoolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder orBoolQueryBuilder = QueryBuilders.boolQuery();
        // 处理and查询参数请求
        dealEsAndQueryParam(esOutputRequest, andBoolQueryBuilder);
        // 处理or查询参数请求
        dealEsOrQueryParam(esOutputRequest, orBoolQueryBuilder);
        baseQueryBuilder.must(andBoolQueryBuilder).must(orBoolQueryBuilder);
        sourceBuilder.query(baseQueryBuilder);
    }

    /**
     * 处理andQueryParam
     *
     * @param esOutputRequest
     *            es查询参数
     * @param andBoolQueryBuilder
     *            andQueryBuilder
     */
    private void dealEsAndQueryParam(EsOutputRequest esOutputRequest, BoolQueryBuilder andBoolQueryBuilder) {
        if (CollectionUtils.isNotEmpty(esOutputRequest.getAndQueryParam())) {
            BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
            for (EsBaseQueryParam baseQueryParam : esOutputRequest.getAndQueryParam()) {
                if (checkEsBaseQueryParam(baseQueryParam)) {
                    String key = baseQueryParam.getKey();
                    String andQueryKey;
                    if (key.contains(Constants.FILL_POINT)) {
                        // 如果是查询学习状态
                        if (Constants.ES_QUERY_STUDY_STATE.equals(key)) {
                            String learningField =
                                CommonUtils.splicingEsDataDictField(Constants.ES_QUERY_LEARNING_UIDS);
                            String learnedField = CommonUtils.splicingEsDataDictField(Constants.ES_QUERY_LEARNED_UIDS);
                            if (Constants.ES_QUERY_UNLEARNED.equals(baseQueryParam.getValue())) {
                                // 未学习之这个用户在user_ids中存在，但在学习中和为学习中不存在
                                nestedBoolQueryBuilder
                                    .mustNot(QueryBuilders.termQuery(learningField, esOutputRequest.getUserId()))
                                    .mustNot(QueryBuilders.termQuery(learnedField, esOutputRequest.getUserId()));
                            } else {
                                if (Constants.ES_QUERY_LEARNING.equals(baseQueryParam.getValue())) {
                                    // 学习中
                                    nestedBoolQueryBuilder
                                        .must(QueryBuilders.termQuery(learningField, esOutputRequest.getUserId()));
                                } else {
                                    nestedBoolQueryBuilder
                                        .must(QueryBuilders.termQuery(learnedField, esOutputRequest.getUserId()));
                                }
                            }
                        } else {
                            andQueryKey = Constants.ES_DATA_DICT + key;
                            if (Constants.ES_QUERY_SHARE_STATUS.equals(key)
                                && !Constants.ES_QUERY_SHARING.equals(baseQueryParam.getValue())) {
                                continue;
                            }
                            if (LogicalRelationEnum.EQ.getLogical().equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .must(QueryBuilders.termQuery(andQueryKey, baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.LIKE.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .must(QueryBuilders.matchPhrasePrefixQuery(andQueryKey, baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.NEQ.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .mustNot(QueryBuilders.termQuery(andQueryKey, baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.NOT_LIKE.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder.mustNot(
                                    QueryBuilders.matchPhrasePrefixQuery(andQueryKey, baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.IN.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .must(QueryBuilders.termsQuery(andQueryKey, baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.NOT_IN.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .mustNot(QueryBuilders.termsQuery(andQueryKey, baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.BETWEEN.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                // 取出value的值
                                List<Long> valueList =
                                    JSON.parseArray(String.valueOf(baseQueryParam.getValue()), Long.class);
                                if (CollectionUtils.isNotEmpty(valueList)
                                    && Objects.equals(Constants.BETWEEN_VALUE_SIZE, valueList.size())) {
                                    Long minValue = Math.min(valueList.get(CommonConstants.ZERO_VALUE_INT),
                                        valueList.get(CommonConstants.ONE_VALUE_INTEGER));
                                    Long maxValue = Math.max(valueList.get(CommonConstants.ZERO_VALUE_INT),
                                        valueList.get(CommonConstants.ONE_VALUE_INTEGER));
                                    nestedBoolQueryBuilder
                                        .must(QueryBuilders.rangeQuery(andQueryKey).from(minValue).to(maxValue));
                                }
                            } else if (LogicalRelationEnum.GT.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .must(QueryBuilders.rangeQuery(andQueryKey).from(baseQueryParam.getValue(), false));
                            } else if (LogicalRelationEnum.GEQ.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .must(QueryBuilders.rangeQuery(andQueryKey).from(baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.LT.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .must(QueryBuilders.rangeQuery(andQueryKey).to(baseQueryParam.getValue(), false));
                            } else if (LogicalRelationEnum.LEQ.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .must(QueryBuilders.rangeQuery(andQueryKey).to(baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.CONTAINS.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                nestedBoolQueryBuilder
                                    .must(QueryBuilders.termsQuery(andQueryKey, baseQueryParam.getValue()));
                            } else if (LogicalRelationEnum.AND_CONTAINS_OR.getLogical()
                                .equals(baseQueryParam.getLogicalRelation())) {
                                List<EsBaseQueryParam> andContainsOrList =
                                    JSON.parseArray(String.valueOf(baseQueryParam.getValue()), EsBaseQueryParam.class);
                                if (CollectionUtils.isNotEmpty(andContainsOrList)) {
                                    BoolQueryBuilder nestedBoolOrBuilder = QueryBuilders.boolQuery();
                                    for (EsBaseQueryParam subQueryParam : andContainsOrList) {
                                        if (checkEsBaseQueryParam(subQueryParam)) {
                                            String subKey = subQueryParam.getKey();
                                            String subOrQueryKey;
                                            if (subKey.contains(Constants.FILL_POINT)) {
                                                subOrQueryKey = Constants.ES_DATA_DICT + subKey;
                                                if (LogicalRelationEnum.EQ.getLogical()
                                                    .equals(subQueryParam.getLogicalRelation())) {
                                                    nestedBoolOrBuilder.should(QueryBuilders.termQuery(subOrQueryKey,
                                                        subQueryParam.getValue()));
                                                } else if (LogicalRelationEnum.GT.getLogical()
                                                    .equals(subQueryParam.getLogicalRelation())) {
                                                    nestedBoolOrBuilder.should(QueryBuilders.rangeQuery(subOrQueryKey)
                                                        .from(subQueryParam.getValue(), false));
                                                }

                                            }
                                        }
                                    }
                                    nestedBoolQueryBuilder.must(nestedBoolOrBuilder);
                                }
                            }
                        }
                        NestedQueryBuilder nestedQueryBuilder =
                            QueryBuilders.nestedQuery(Constants.ES_DATA_DICT, nestedBoolQueryBuilder, ScoreMode.None);
                        andBoolQueryBuilder.must(nestedQueryBuilder);

                    } else {
                        if (LogicalRelationEnum.EQ.getLogical().equals(baseQueryParam.getLogicalRelation())) {
                            // eq关系
                            if (Constants.ES_QUERY_CLASSIFY_ID.equals(key)) {
                                andBoolQueryBuilder
                                    .must(QueryBuilders.termQuery(Constants.classifyIds, baseQueryParam.getValue()));
                            } else {
                                andBoolQueryBuilder.must(QueryBuilders.termQuery(key, baseQueryParam.getValue()));
                            }
                        } else if (LogicalRelationEnum.LIKE.getLogical().equals(baseQueryParam.getLogicalRelation())) {
                            // like关系
                            andBoolQueryBuilder.must(QueryBuilders.wildcardQuery(stitchFuzzyMatchesKey(key),
                                stitchFuzzyMatchesValue(String.valueOf(baseQueryParam.getValue()))));
                        } else if (LogicalRelationEnum.BETWEEN.getLogical()
                            .equals(baseQueryParam.getLogicalRelation())) {
                            // 取出value的值
                            List<Long> valueList =
                                JSON.parseArray(String.valueOf(baseQueryParam.getValue()), Long.class);
                            if (CollectionUtils.isNotEmpty(valueList)
                                && Objects.equals(Constants.BETWEEN_VALUE_SIZE, valueList.size())) {
                                Long minValue = Math.min(valueList.get(CommonConstants.ZERO_VALUE_INT),
                                    valueList.get(CommonConstants.ONE_VALUE_INTEGER));
                                Long maxValue = Math.max(valueList.get(CommonConstants.ZERO_VALUE_INT),
                                    valueList.get(CommonConstants.ONE_VALUE_INTEGER));
                                andBoolQueryBuilder.must(QueryBuilders.rangeQuery(key).from(minValue).to(maxValue));
                            }
                        } else if (LogicalRelationEnum.CONTAINS.getLogical()
                            .equals(baseQueryParam.getLogicalRelation())) {
                            andBoolQueryBuilder.must(QueryBuilders.termsQuery(key, baseQueryParam.getValue()));
                        } else {
                            // 不是eq关系并且key是createTs
                            if (Constants.ES_QUERY_CREATE_TS.equals(key)) {
                                RangeQueryBuilder rangequerybuilder = null;
                                if (LogicalRelationEnum.GT.getLogical().equals(baseQueryParam.getLogicalRelation())) {
                                    // 大于该时间戳
                                    rangequerybuilder =
                                        QueryBuilders.rangeQuery(key).from(baseQueryParam.getValue(), false);
                                } else if (LogicalRelationEnum.GEQ.getLogical()
                                    .equals(baseQueryParam.getLogicalRelation())) {
                                    // 大于等于该时间戳
                                    rangequerybuilder = QueryBuilders.rangeQuery(key).from(baseQueryParam.getValue());
                                } else if (LogicalRelationEnum.LT.getLogical()
                                    .equals(baseQueryParam.getLogicalRelation())) {
                                    // 小于该时间戳
                                    rangequerybuilder =
                                        QueryBuilders.rangeQuery(key).to(baseQueryParam.getValue(), false);
                                } else if (LogicalRelationEnum.LEQ.getLogical()
                                    .equals(baseQueryParam.getLogicalRelation())) {
                                    // 小于等于该时间戳
                                    rangequerybuilder = QueryBuilders.rangeQuery(key).to(baseQueryParam.getValue());
                                } else if (LogicalRelationEnum.IN.getLogical()
                                    .equals(baseQueryParam.getLogicalRelation())) {
                                    String timeStampValue = baseQueryParam.getValue().toString().trim();
                                    // 该字符串长度需要大于2,使用英文逗号分隔，前端为起始值，后段为结束值
                                    if (timeStampValue.contains(Constants.COMMA)) {
                                        String[] timeStampArr =
                                            baseQueryParam.getValue().toString().split(Constants.COMMA);
                                        if (timeStampArr.length > 1) {
                                            long startTs = Long.parseLong(timeStampArr[0].trim());
                                            long endTs = Long.parseLong(timeStampArr[1].trim());
                                            rangequerybuilder = QueryBuilders.rangeQuery(key).from(startTs).to(endTs);
                                        }
                                    }
                                }
                                if (rangequerybuilder != null) {
                                    andBoolQueryBuilder.must(rangequerybuilder);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理orQueryParam
     *
     * @param esOutputRequest
     *            es查询参数
     * @param orBoolQueryBuilder
     *            orQueryBuilder
     */
    private void dealEsOrQueryParam(EsOutputRequest esOutputRequest, BoolQueryBuilder orBoolQueryBuilder) {
        if (CollectionUtils.isNotEmpty(esOutputRequest.getOrQueryParam())) {
            BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
            for (EsBaseQueryParam baseQueryParam : esOutputRequest.getOrQueryParam()) {
                if (checkEsBaseQueryParam(baseQueryParam)) {
                    String key = baseQueryParam.getKey();
                    String orQueryKey;
                    if (key.contains(Constants.FILL_POINT)) {
                        orQueryKey = Constants.ES_DATA_DICT + key;
                        if (LogicalRelationEnum.EQ.getLogical().equals(baseQueryParam.getLogicalRelation())) {
                            nestedBoolQueryBuilder
                                .should(QueryBuilders.termQuery(orQueryKey, baseQueryParam.getValue()));
                        } else if (LogicalRelationEnum.LIKE.getLogical().equals(baseQueryParam.getLogicalRelation())) {
                            nestedBoolQueryBuilder
                                .should(QueryBuilders.matchPhrasePrefixQuery(orQueryKey, baseQueryParam.getValue()));
                        } else if (LogicalRelationEnum.CONTAINS.getLogical()
                            .equals(baseQueryParam.getLogicalRelation())) {
                            nestedBoolQueryBuilder
                                .should(QueryBuilders.termsQuery(orQueryKey, baseQueryParam.getValue()));
                        } else if (LogicalRelationEnum.OR_CONTAINS_AND.getLogical()
                            .equals(baseQueryParam.getLogicalRelation())) {
                            List<EsBaseQueryParam> orContainsAndList =
                                JSON.parseArray(String.valueOf(baseQueryParam.getValue()), EsBaseQueryParam.class);
                            if (CollectionUtils.isNotEmpty(orContainsAndList)) {
                                BoolQueryBuilder nestedBoolAndBuilder = QueryBuilders.boolQuery();
                                for (EsBaseQueryParam subQueryParam : orContainsAndList) {
                                    if (checkEsBaseQueryParam(subQueryParam)) {
                                        String subKey = subQueryParam.getKey();
                                        String subAndQueryKey;
                                        if (subKey.contains(Constants.FILL_POINT)) {
                                            subAndQueryKey = Constants.ES_DATA_DICT + subKey;
                                            if (LogicalRelationEnum.EQ.getLogical()
                                                .equals(subQueryParam.getLogicalRelation())) {
                                                nestedBoolAndBuilder.must(
                                                    QueryBuilders.termQuery(subAndQueryKey, subQueryParam.getValue()));
                                            } else if (LogicalRelationEnum.GT.getLogical()
                                                .equals(subQueryParam.getLogicalRelation())) {
                                                nestedBoolAndBuilder.must(QueryBuilders.rangeQuery(subAndQueryKey)
                                                    .from(subQueryParam.getValue(), false));
                                            }

                                        }
                                    }
                                }
                                nestedBoolQueryBuilder.should(nestedBoolAndBuilder);
                            }
                        }
                        NestedQueryBuilder nestedQueryBuilder =
                            QueryBuilders.nestedQuery(Constants.ES_DATA_DICT, nestedBoolQueryBuilder, ScoreMode.None);
                        orBoolQueryBuilder.should(nestedQueryBuilder);
                    } else {
                        if (LogicalRelationEnum.EQ.getLogical().equals(baseQueryParam.getLogicalRelation())) {
                            if (Constants.ES_QUERY_CLASSIFY_ID.equals(key)) {
                                orBoolQueryBuilder
                                    .should(QueryBuilders.termQuery(Constants.classifyIds, baseQueryParam.getValue()));
                            } else {
                                orBoolQueryBuilder.should(QueryBuilders.termQuery(key, baseQueryParam.getValue()));
                            }
                        } else if (LogicalRelationEnum.LIKE.getLogical().equals(baseQueryParam.getLogicalRelation())) {
                            orBoolQueryBuilder.should(QueryBuilders.wildcardQuery(stitchFuzzyMatchesKey(key),
                                stitchFuzzyMatchesValue(String.valueOf(baseQueryParam.getValue()))));
                        } else if (LogicalRelationEnum.CONTAINS.getLogical()
                            .equals(baseQueryParam.getLogicalRelation())) {
                            orBoolQueryBuilder.should(QueryBuilders.termsQuery(key, baseQueryParam.getValue()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理Es排序参数
     *
     * @param esOutputRequest
     * @param sourceBuilder
     */
    private void dealEsSortOrderParam(EsOutputRequest esOutputRequest, SearchSourceBuilder sourceBuilder) {
        // sort排序
        if (CollectionUtils.isNotEmpty(esOutputRequest.getSortOrderParam())) {
            for (EsSortOrderParam sortOrderParam : esOutputRequest.getSortOrderParam()) {
                if (sortOrderParam == null
                    || StringUtils.isAnyBlank(sortOrderParam.getSortName(), sortOrderParam.getSortOrder())) {
                    continue;
                }
                if (Constants.ES_QUERY_CREATE_TS.equals(sortOrderParam.getSortName())) {
                    sourceBuilder.sort(sortOrderParam.getSortName(), chooseSortOrder(sortOrderParam.getSortOrder()));
                } else {
                    sourceBuilder
                        .sort(SortBuilders.fieldSort(CommonUtils.splicingEsDataDictField(sortOrderParam.getSortName()))
                            .order(chooseSortOrder(sortOrderParam.getSortOrder()))
                            .setNestedSort(new NestedSortBuilder(Constants.ES_DATA_DICT)));
                }
            }
        } else {
            // 如果排序字段为空，默认按照创建时间倒叙排列
            sourceBuilder.sort(Constants.ES_QUERY_CREATE_TS, SortOrder.DESC);
        }
        sourceBuilder.sort(Constants.resourceId, SortOrder.DESC);
    }

    /**
     * 检测es请求消息参数
     *
     * @param outputRequest
     * @return false 消息错误，true 消息正确
     */
    private boolean checkEsOutputRequestParam(EsOutputRequest outputRequest) {
        return outputRequest != null && outputRequest.getEnterpriseId() != null
            && StringUtils.isNoneBlank(outputRequest.getAppId(), outputRequest.getDomainId(), outputRequest.getUserId())
            && outputRequest.getPageNum() >= 1 && outputRequest.getPageSize() >= -1 && outputRequest.getPageSize() != 0;
    }

    /**
     * 检测EsBaseQueryParam
     *
     * @param baseQueryParam
     * @return
     */
    private boolean checkEsBaseQueryParam(EsBaseQueryParam baseQueryParam) {
        return baseQueryParam != null
            && StringUtils.isNoneBlank(baseQueryParam.getKey(), baseQueryParam.getLogicalRelation())
            && baseQueryParam.getValue() != null;
    }

    /**
     * 0 : asc 1: desc
     *
     * @param order
     * @returno
     */
    private SortOrder chooseSortOrder(String order) {
        if (Constants.ORDER_ASC.equals(order)) {
            return SortOrder.ASC;
        } else {
            return SortOrder.DESC;
        }
    }

    /**
     * 匹配模糊匹配key
     */
    private String stitchFuzzyMatchesKey(String key) {
        return key + Constants.FILL_POINT + Constants.KEYWORD;
    }

    /**
     * 匹配模糊匹配value
     */
    private String stitchFuzzyMatchesValue(String value) {
        return "*" + value + "*";
    }
}
