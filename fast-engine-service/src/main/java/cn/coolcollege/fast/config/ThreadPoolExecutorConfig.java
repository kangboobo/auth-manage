package cn.coolcollege.fast.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.zip.ZipInputStream;

/**
 * @author pk
 * @date 2021-06-23 22:45
 */
@Component
@ConfigurationProperties(prefix = "thread.pool.executor")
public class ThreadPoolExecutorConfig {

    /**
     * 计算资源id默认线程池配置
     */
    private DefaultResourceIdConfig defaultResourceIdConfig;
    /**
     * bulkLoad计算资源id线程池配置
     */
    private BulkLoadResourceIdConfig bulkLoadResourceIdConfig;
    /**
     * 计算直属于分类且继承了分类的可见性的资源id线程池配置
     */
    private DirectExtendResourceIdConfig directExtendResourceIdConfig;
    /**
     * 用户新增计算资源id线程池配置
     */
    private UserAddResourceIdConfig userAddResourceIdConfig;
    /**
     * 用户修改计算资源id线程池配置
     */
    private UserModifyOrgResourceIdConfig userModifyOrgResourceIdConfig;
    /**
     * 组织修改事件计算资源id线程池配置
     */
    private OrgModifyResourceIdConfig orgModifyResourceIdConfig;
    /**
     * 用户修改管辖范围计算资源id线程池配置
     */
    private AuthorityRangeModifyResourceIdConfig authModifyResourceIdConfig;
    /**
     * 根据资源id计算可见性线程池配置
     */
    private ResourceDetailConfig resourceDetailConfig;

    public DefaultResourceIdConfig getDefaultResourceIdConfig() {
        return defaultResourceIdConfig;
    }

    public void setDefaultResourceIdConfig(DefaultResourceIdConfig defaultResourceIdConfig) {
        this.defaultResourceIdConfig = defaultResourceIdConfig;
    }

    public AuthorityRangeModifyResourceIdConfig getAuthModifyResourceIdConfig() {
        return authModifyResourceIdConfig;
    }

    public void setAuthModifyResourceIdConfig(AuthorityRangeModifyResourceIdConfig authModifyResourceIdConfig) {
        this.authModifyResourceIdConfig = authModifyResourceIdConfig;
    }

    public BulkLoadResourceIdConfig getBulkLoadResourceIdConfig() {
        return bulkLoadResourceIdConfig;
    }

    public void setBulkLoadResourceIdConfig(BulkLoadResourceIdConfig bulkLoadResourceIdConfig) {
        this.bulkLoadResourceIdConfig = bulkLoadResourceIdConfig;
    }

    public DirectExtendResourceIdConfig getDirectExtendResourceIdConfig() {
        return directExtendResourceIdConfig;
    }

    public void setDirectExtendResourceIdConfig(DirectExtendResourceIdConfig directExtendResourceIdConfig) {
        this.directExtendResourceIdConfig = directExtendResourceIdConfig;
    }

    public UserAddResourceIdConfig getUserAddResourceIdConfig() {
        return userAddResourceIdConfig;
    }

    public void setUserAddResourceIdConfig(UserAddResourceIdConfig userAddResourceIdConfig) {
        this.userAddResourceIdConfig = userAddResourceIdConfig;
    }

    public UserModifyOrgResourceIdConfig getUserModifyOrgResourceIdConfig() {
        return userModifyOrgResourceIdConfig;
    }

    public void setUserModifyOrgResourceIdConfig(UserModifyOrgResourceIdConfig userModifyOrgResourceIdConfig) {
        this.userModifyOrgResourceIdConfig = userModifyOrgResourceIdConfig;
    }

    public OrgModifyResourceIdConfig getOrgModifyResourceIdConfig() {
        return orgModifyResourceIdConfig;
    }

    public void setOrgModifyResourceIdConfig(OrgModifyResourceIdConfig orgModifyResourceIdConfig) {
        this.orgModifyResourceIdConfig = orgModifyResourceIdConfig;
    }

    public AuthorityRangeModifyResourceIdConfig getAuthorityRangeModifyResourceIdConfig() {
        return authModifyResourceIdConfig;
    }

    public void setAuthorityRangeModifyResourceIdConfig(AuthorityRangeModifyResourceIdConfig authorityRangeModifyResourceIdConfig) {
        this.authModifyResourceIdConfig = authorityRangeModifyResourceIdConfig;
    }

    public ResourceDetailConfig getResourceDetailConfig() {
        return resourceDetailConfig;
    }

    public void setResourceDetailConfig(ResourceDetailConfig resourceDetailConfig) {
        this.resourceDetailConfig = resourceDetailConfig;
    }

    public static class BulkLoadResourceIdConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int queueSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }
    }

    public static class DefaultResourceIdConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int queueSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }
    }

    public static class UserAddResourceIdConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int queueSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }
    }

    public static class UserModifyOrgResourceIdConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int queueSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }
    }

    public static class OrgModifyResourceIdConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int queueSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }
    }

    public static class DirectExtendResourceIdConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int queueSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }
    }

    public static class AuthorityRangeModifyResourceIdConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int queueSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }
    }

    public static class ResourceDetailConfig {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int queueSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }
    }
}