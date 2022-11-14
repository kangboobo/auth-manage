package cn.coolcollege.fast.model;

import net.coolcollege.platform.util.constants.CommonConstants;
import net.coolcollege.platform.util.model.BaseResult;

/**
 * @author pk
 * @date 2021-06-22 23:11
 */
public class LoadResourceTypeResp extends BaseResult {
    private String loaded = CommonConstants.TRUE;

    public String getLoaded() {
        return loaded;
    }

    public void setLoaded(String loaded) {
        this.loaded = loaded;
    }
}

