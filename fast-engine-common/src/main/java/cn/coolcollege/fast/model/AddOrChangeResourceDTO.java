package cn.coolcollege.fast.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddOrChangeResourceDTO extends ResourceCommonDTO {

    private List<String> resourceIds;

    @NotNull
    private String resourceType;

}
