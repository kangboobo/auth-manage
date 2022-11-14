package cn.coolcollege.fast.storage.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author bai bin
 * @Date 2021/5/13 15:59
 */
@Data
@Table(name = "fast_engine_domain_field")
public class DomainFieldDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain_id")
    private String domainId;

    private String field;

    private String description;

    /**
     * 值类型，根据此修改es index 值类型
     */
    @Column(name = "value_type")
    private String valueType;

    @Column(name = "value_type")
    private String valueEnum;

    /**
     * 分词器
     * 0:英文逗号分词 默认逗号分词
     * 1:空白分词
     * 2:其他待定义分词
     */
    private Byte analyzer;

    /**
     * true:新增
     * false:不是新增
     * 是新增时才去修改index mapping
     */
    @Column(name = "newly_add")
    private Boolean newlyAdd;

    @Column(name = "create_time")
    private Date createTime;
}
