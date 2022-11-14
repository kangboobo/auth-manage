package cn.coolcollege.fast.controller;

import cn.coolcollege.fast.model.LoadResourceTypeReq;
import cn.coolcollege.fast.service.ResourceServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pk
 * @date 2021-06-22 23:04
 */
@RestController
public class ResourceTypeController {

    @Autowired
    private ResourceServiceProxy resourceServiceProxy;

    /**
     * 动态加载某个resourceType
     *
     * @param req
     * @return
     */
    @PostMapping(path = "/resource_type/load")
    public Object loadResourceType(@RequestBody LoadResourceTypeReq req) {
        return resourceServiceProxy.loadResourceType(req);
    }
}
