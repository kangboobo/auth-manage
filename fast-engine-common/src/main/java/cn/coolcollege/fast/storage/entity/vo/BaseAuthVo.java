package cn.coolcollege.fast.storage.entity.vo;

/**
 * @author baibin
 * @version 1.0
 * @date 2022/11/11 14:46
 * @description 基础vo
 */
public class BaseAuthVo {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
