
package com.gongsibao.common.util.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * Created by wk on 2016/3/24
 */
public class OSSClientFactory extends BasePoolableObjectFactory<OSSClient> {

    private String ossEndpoint;

    private String accessId;

    private String accessKey;

    @Override
    public void destroyObject(OSSClient obj) throws Exception {
        super.destroyObject(obj);
        if (null != obj) {
            obj.shutdown();
        }
    }

    public OSSClientFactory(String ossEndpoint, String accessId, String accessKey) {
        this.ossEndpoint = ossEndpoint;
        this.accessId = accessId;
        this.accessKey = accessKey;
    }

    public OSSClient makeObject() throws Exception {
        ClientConfiguration config = new ClientConfiguration();
        OSSClient client = new OSSClient(ossEndpoint, accessId, accessKey, config);
        return client;
    }
}
