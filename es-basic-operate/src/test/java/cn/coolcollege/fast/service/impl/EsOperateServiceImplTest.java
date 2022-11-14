package cn.coolcollege.fast.service.impl;

import cn.coolcollege.fast.constants.Constants;
import cn.coolcollege.fast.dto.EsFieldsDto;
import cn.coolcollege.fast.dto.EsCommonDto;
import cn.coolcollege.fast.dto.EsResourceListDto;
import cn.coolcollege.fast.dto.EsUserIdDto;
import cn.coolcollege.fast.dto.EsUserIdListDto;
import cn.coolcollege.fast.entity.UserResourceDo;
import cn.coolcollege.fast.model.ResourceStatusDTO;
import cn.coolcollege.fast.storage.entity.DomainFieldDo;
import cn.coolcollege.fast.storage.mapper.DomainFieldMapper;
import com.google.common.collect.Lists;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Author bai bin
 * @Date 2021/5/24 9:56
 */
@RunWith(MockitoJUnitRunner.class)
public class EsOperateServiceImplTest {

    @InjectMocks
    private EsOperateServiceImpl esOperateService;

    @Mock
    private RestHighLevelClient restHighLevelClient;

    @Mock
    private DomainFieldMapper fieldMapper;

    private static String jsonString;

    @Before
    public void init() {
        jsonString = "{\"app_id\":\"cool\",\"classify_ids\":\"1\",\"create_ts\":1621847643745,\"create_user_id\":\"1\"," +
                "\"create_user_name\":\"刘备\",\"data_dict\":{\"comment_count\":1,\"thumb_up_count\":1,\"sort_no\":-14," +
                "\"course_type\":\"optional\",\"is_recommend\":0,\"lecturer_name\":\"萧何\"," +
                "\"learned_uids\":\"21,22,23,24,25,26,27,28,29,30\",\"learning_uids\":\"11,12,13,14,15,16,17,18,19,20\"," +
                "\"play_count\":1,\"publish_status\":\"unpublished\",\"course_source\":\"not_original\"},\"desc\":\"描述1\"," +
                "\"enterprise_id\":\"123456\",\"resource_id\":\"resourceId1\",\"resource_title\":\"卫青\",\"resource_type\":\"course1\"," +
                "\"user_ids\":\"1\"}";
    }

    @Test
    public void testAddOrUpdateDocument() throws IOException {
        List<UserResourceDo> userResourceDoList = new ArrayList<>();
        UserResourceDo resourceDo = new UserResourceDo();
        resourceDo.setAppId("cool");
        resourceDo.setEnterpriseId("eid");
        resourceDo.setResourceId("resource_1");
        resourceDo.setResourceType("course");
        resourceDo.setUserIds(Arrays.asList("1", "2", "3"));
        resourceDo.setName("name");
        resourceDo.setCreateUserName("test1");
        resourceDo.setCreateUserId("1");
        resourceDo.setCreateTs(System.currentTimeMillis());
        userResourceDoList.add(resourceDo);
        BulkItemResponse bulkItemResponse = mock(BulkItemResponse.class);
        BulkItemResponse[] bulkItemResponses = new BulkItemResponse[]{bulkItemResponse};
        BulkResponse bulkResponse = new BulkResponse(bulkItemResponses, 3L);
        when(restHighLevelClient.bulk(any())).thenReturn(bulkResponse);
        assertThat(esOperateService.addOrUpdateDocument(userResourceDoList,"class")).isEqualTo(1);
    }

    @Test
    public void testGetOneUserResourceDo() throws IOException {
        EsCommonDto esCommonDto = new EsCommonDto();
        esCommonDto.setAppId("cool");
        esCommonDto.setEnterpriseId("eid");
        esCommonDto.setResourceId("resource_1");
        esCommonDto.setResourceType("course");
        esCommonDto.setDomainId("class");
        mockClientGet();
        assertThat(esOperateService.getOneUserResourceDo(esCommonDto).getCreateUserId()).isEqualTo("1");
    }

    @Test
    public void testGetResourceListByUserId() throws IOException {
        EsUserIdDto userIdDto = new EsUserIdDto();
        userIdDto.setAppId("cool");
        userIdDto.setEnterpriseId("eid");
        userIdDto.setResourceType("course");
        userIdDto.setUserId("123456");
        userIdDto.setDomainId("class");
        mockClientSearch();
        assertThat(esOperateService.getResourceListByUserId(userIdDto).get(0).getCreateUserId()).isEqualTo("1");
    }

    @Test
    public void testGetResourceListByIds() throws IOException {
        EsResourceListDto resourceListDto = new EsResourceListDto();
        resourceListDto.setAppId("cool");
        resourceListDto.setEnterpriseId("eid");
        resourceListDto.setDomainId("class");
        resourceListDto.setResourceType("course");
        resourceListDto.setResourceIds(Arrays.asList("123", "234", "456"));
        mockClientSearch();
        assertThat(esOperateService.getResourceListByIds(resourceListDto).get(0).getCreateUserId()).isEqualTo("1");
    }

    @Test
    public void testUpdateEsUserIds() throws IOException {
        EsUserIdListDto userIdListDto = new EsUserIdListDto();
        userIdListDto.setAppId("cool");
        userIdListDto.setEnterpriseId("eid");
        userIdListDto.setDomainId("class");
        userIdListDto.setResourceId("resourceId_1");
        userIdListDto.setResourceType("course");
        userIdListDto.setUserIdList(Arrays.asList("123", "456", "789"));
        mockClientUpdate();
        assertThat(esOperateService.updateEsUserIds(userIdListDto)).isTrue();
    }

    @Test
    public void testUpdateResourceStudyStatusIsStudying() throws IOException {
        ResourceStatusDTO resourceStatusDTO = new ResourceStatusDTO();
        resourceStatusDTO.setStatus(Constants.studying);
        updateResourceStudyStatusParam(resourceStatusDTO);
        assertThat(esOperateService.updateResourceStudyStatus(resourceStatusDTO)).isTrue();
    }

    @Test
    public void testUpdateResourceStudyStatusIsFinished() throws IOException {
        ResourceStatusDTO resourceStatusDTO = new ResourceStatusDTO();
        resourceStatusDTO.setStatus(Constants.finished);
        updateResourceStudyStatusParam(resourceStatusDTO);
        assertThat(esOperateService.updateResourceStudyStatus(resourceStatusDTO)).isTrue();
    }

    @Test
    public void testDeleteEsResourceInfo() throws IOException {
        EsResourceListDto resourceListDto = new EsResourceListDto();
        resourceListDto.setAppId("cool");
        resourceListDto.setEnterpriseId("eid");
        resourceListDto.setDomainId("class");
        resourceListDto.setResourceType("course");
        resourceListDto.setResourceIds(Arrays.asList("123", "456", "789"));
        BulkResponse bulkResponse = mock(BulkResponse.class);
        when(restHighLevelClient.bulk(any())).thenReturn(bulkResponse);
        assertThat(esOperateService.deleteEsResourceInfo(resourceListDto)).isTrue();
    }

    @Test
    public void testModifyEsDomainField() throws IOException {
        List<DomainFieldDo> needModifyList = new ArrayList<>();
        DomainFieldDo fieldDo = new DomainFieldDo();
        fieldDo.setField("learning_uids");
        fieldDo.setAnalyzer(Byte.parseByte("0"));
        fieldDo.setValueType("text");
        needModifyList.add(fieldDo);
        when(fieldMapper.getNewlyDomainFieldList("class")).thenReturn(needModifyList);
        PutMappingResponse putMappingResponse = mock(PutMappingResponse.class);
        when(putMappingResponse.isAcknowledged()).thenReturn(true);
        IndicesClient indicesClient = mock(IndicesClient.class);
        when(restHighLevelClient.indices()).thenReturn(indicesClient);
        when(restHighLevelClient.indices().putMapping(any())).thenReturn(putMappingResponse);
        assertThat(esOperateService.modifyEsDomainField("class").getSuccess()).isTrue();
    }

    @Test
    public void testEsBusinessField() throws IOException {
        EsFieldsDto businessFieldDto = new EsFieldsDto();
        businessFieldDto.setAppId("cool");
        businessFieldDto.setDomainId("class");
        businessFieldDto.setEnterpriseId("eid");
        businessFieldDto.setResourceIds(Lists.newArrayList("123456"));
        businessFieldDto.setResourceType("course");
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("sort_no",-54321);
        paramsMap.put("is_recommend",1);
        businessFieldDto.setParamsMap(paramsMap);
        List<String> fieldList = Arrays.asList("sort_no","is_recommend","public_status");
        when(fieldMapper.getFieldNameByDomainId("class")).thenReturn(fieldList);
        mockClientUpdate();
        assertThat(esOperateService.updateEsBusinessField(businessFieldDto)).isTrue();
    }

    private void updateResourceStudyStatusParam(ResourceStatusDTO resourceStatusDTO) throws IOException {
        resourceStatusDTO.setAppId("cool");
        resourceStatusDTO.setUserId("1001");
        resourceStatusDTO.setResourceId("123456");
        resourceStatusDTO.setResourceType("course");
        resourceStatusDTO.setEId("eid");
        resourceStatusDTO.setDomainId("class");
        mockClientGet();
        mockClientUpdate();
    }

    private void mockClientGet() throws IOException {
        GetResponse getResponse = mock(GetResponse.class);
        when(getResponse.isExists()).thenReturn(true);
        when(getResponse.getSourceAsString()).thenReturn(jsonString);
        when(restHighLevelClient.get(any())).thenReturn(getResponse);
    }

    private void mockClientUpdate() throws IOException {
        UpdateResponse updateResponse = mock(UpdateResponse.class);
        when(restHighLevelClient.update(any())).thenReturn(updateResponse);
    }

    private void mockClientSearch() throws IOException {
        SearchResponse searchResponse = mock(SearchResponse.class);
        SearchHit searchHit = mock(SearchHit.class);
        SearchHit[] hits = new SearchHit[]{searchHit};
        when(hits[0].getSourceAsString()).thenReturn(jsonString);
        SearchHits searchHits = new SearchHits(hits, 5l, 1f);
        when(searchResponse.getHits()).thenReturn(searchHits);
        when(restHighLevelClient.search(any())).thenReturn(searchResponse);
    }
}
