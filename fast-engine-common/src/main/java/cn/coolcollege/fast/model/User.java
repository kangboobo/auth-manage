package cn.coolcollege.fast.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class User {

    @NotNull
    private String id;

    @JSONField(name = "department_ids")
    private List<String> departmentIds;

    @JSONField(name = "group_ids")
    private List<String> groupIds;

    @JSONField(name = "position_ids")
    private List<String> positionIds;
}
