package cn.coolcollege.fast.storage.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author bai bin
 */
@Data
@Table(name = "fast_engine_app_resource")
public class AppResourceDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "domain_id")
    private String domainId;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "es_index")
    private String esIndex;

    @Column(name = "include_classify")
    private Boolean includeClassify;

    @Column(name = "create_time")
    private Date createTime;
}
