package cn.coolcollege.fast.handler;

import cn.coolcollege.fast.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JthBaseHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 课程集团化按钮，默认不开启 开启时则 0:关闭 1:部分开启 2:全部开启
     */
    @Value("${whether.to.turn.on.course.jth.button:0}")
    private int courseJthButton;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据集团化开关过滤resourceType 当集团化课程按钮关闭或部分开启并且企业不在白名单配置内时过滤
     * 
     * @param eid
     * @param resourceTypes
     * @return
     */
    public void filterResourceType(String eid, List<String> resourceTypes) {
        // 集团化开关验证
        String value = (String)stringRedisTemplate.boundHashOps(Constants.GATEWAY_ENV_KEY)
            .get(String.format(Constants.ENTERPRISE_MODULE_FORMAT, eid));
        if (courseJthButton == Constants.JTH_COURSE_BUTTON_CLOSE
            || (courseJthButton == Constants.JTH_COURSE_BUTTON_PART_ON && !Constants.COURSE_JTH.equals(value))) {
            resourceTypes.removeIf(Constants.classDomainResourceTypeLists::contains);
        }
    }

    /**
     * @return
     */
    public boolean whetherOpenCourseJth(String eid) {
        String value = (String)stringRedisTemplate.boundHashOps(Constants.GATEWAY_ENV_KEY)
            .get(String.format(Constants.ENTERPRISE_MODULE_FORMAT, eid));
        if (StringUtils.isBlank(value)) {
            return false;
        }
        // 集团化开关验证
        return courseJthButton == Constants.JTH_COURSE_BUTTON_ALL_ON
            || (courseJthButton == Constants.JTH_COURSE_BUTTON_PART_ON && Constants.COURSE_JTH.equals(value));
    }
}
