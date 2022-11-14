package cn.coolcollege.fast.service.impl;

import cn.coolcollege.fast.constants.LogicalRelationEnum;
import cn.coolcollege.fast.entity.EsBaseQueryParam;
import cn.coolcollege.fast.entity.EsSortOrderParam;
import cn.coolcollege.fast.entity.request.EsOutputRequest;
import org.elasticsearch.action.search.SearchResponse;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Author bai bin
 * @Date 2021/5/24 9:56
 */
@RunWith(MockitoJUnitRunner.class)
public class EsOutputServiceImplTest {

//    @InjectMocks
//    private EsOutputServiceImpl esOutputService;

    @Mock
    private RestHighLevelClient restHighLevelClient;

    private EsOutputRequest esOutputRequest = null;
    private static String jsonString;

    @Before
    public void init() {
        jsonString = "{\"create_ts\":1621847643745,\"create_user_id\":\"1\"," +
                "\"create_user_name\":\"刘备\",\"data_dict\":{\"comment_count\":1,\"thumb_up_count\":1,\"sort_no\":-14," +
                "\"course_type\":\"optional\",\"is_recommend\":0,\"lecturer_name\":\"萧何\"," +
                "\"learned_uids\":\"21,22,23,24,25,26,27,28,29,30\",\"learning_uids\":\"11,12,13,14,15,16,17,18,19,20\"," +
                "\"play_count\":1,\"publish_status\":\"unpublished\",\"course_source\":\"not_original\"},\"desc\":\"描述1\"," +
                "\"resource_id\":\"resourceId1\",\"resource_title\":\"卫青\"}";

        esOutputRequest = new EsOutputRequest();
        esOutputRequest.setEnterpriseId(123456L);
        esOutputRequest.setResourceTypes(Arrays.asList("course1","course2","course3"));
        esOutputRequest.setUserId("123");
        esOutputRequest.setOnlyMe(true);
        esOutputRequest.setDomainId("class");
        List<EsBaseQueryParam> andQueryParams = new ArrayList<>();
        EsBaseQueryParam esBaseQueryParamOne = new EsBaseQueryParam();
        esBaseQueryParamOne.setKey("classifyId");
        esBaseQueryParamOne.setLogicalRelation(LogicalRelationEnum.EQ.getLogical());
        esBaseQueryParamOne.setValue("123");
        EsBaseQueryParam esBaseQueryParamTwo = new EsBaseQueryParam();
        esBaseQueryParamTwo.setKey(".studyState");
        esBaseQueryParamTwo.setLogicalRelation(LogicalRelationEnum.EQ.getLogical());
        esBaseQueryParamTwo.setValue("unlearned");
        EsBaseQueryParam esBaseQueryParamThree = new EsBaseQueryParam();
        esBaseQueryParamThree.setKey("createTs");
        esBaseQueryParamThree.setLogicalRelation(LogicalRelationEnum.IN.getLogical());
        esBaseQueryParamThree.setValue("1234567,2345678");
        andQueryParams.add(esBaseQueryParamOne);
        andQueryParams.add(esBaseQueryParamTwo);
        andQueryParams.add(esBaseQueryParamThree);
        List<EsBaseQueryParam> orQueryParams = new ArrayList<>();
        EsBaseQueryParam orQueryParamOne = new EsBaseQueryParam();
        orQueryParamOne.setKey("resourceTitle");
        orQueryParamOne.setLogicalRelation(LogicalRelationEnum.EQ.getLogical());
        orQueryParamOne.setValue("testResourceTitle");
        EsBaseQueryParam orQueryParamTwo = new EsBaseQueryParam();
        orQueryParamTwo.setKey(".lecturerName");
        orQueryParamTwo.setLogicalRelation(LogicalRelationEnum.EQ.getLogical());
        orQueryParamTwo.setValue("testLecturerName");
        orQueryParams.add(orQueryParamOne);
        orQueryParams.add(orQueryParamTwo);
        List<EsSortOrderParam> sortOrderParams = new ArrayList<>();
        EsSortOrderParam esSortOrderParamOne = new EsSortOrderParam();
        esSortOrderParamOne.setSortName("isRecommend");
        esSortOrderParamOne.setSortOrder("asc");
        EsSortOrderParam esSortOrderParamTwo = new EsSortOrderParam();
        esSortOrderParamTwo.setSortName("sortNo");
        esSortOrderParamTwo.setSortOrder("desc");
        sortOrderParams.add(esSortOrderParamOne);
        sortOrderParams.add(esSortOrderParamTwo);
        esOutputRequest.setAndQueryParam(andQueryParams);
        esOutputRequest.setOrQueryParam(orQueryParams);
        esOutputRequest.setSortOrderParam(sortOrderParams);
    }

    @Test
    public void testGetPageEsOutputResourceInfo() throws IOException {
//        SearchResponse searchResponse = mock(SearchResponse.class);
//        SearchHit searchHit = mock(SearchHit.class);
//        SearchHit[] hits = new SearchHit[]{searchHit};
//        when(hits[0].getSourceAsString()).thenReturn(jsonString);
//        SearchHits searchHits = new SearchHits(hits, 5l, 1f);
//        when(searchResponse.getHits()).thenReturn(searchHits);
//        when(restHighLevelClient.search(any())).thenReturn(searchResponse);
//        assertThat(esOutputService.getPageEsOutputResourceInfo(esOutputRequest).getSuccess()).isTrue();
    }
}
