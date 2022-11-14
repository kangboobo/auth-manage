package cn.coolcollege.fast.service;

import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import org.springframework.stereotype.Component;

/**
 * @author pk
 * @date 2021-06-22 16:28
 */
@Component
public class ClientFactoryBean implements ClientFactoryAware {

    private ClientFactory clientFactory;

    @Override
    public void setClientFactory(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public ClientFactory getClientFactory() {
        return clientFactory;
    }
}
