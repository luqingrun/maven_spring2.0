package com.gongsibao.common.util.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.gongsibao.common.util.FileUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.security.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.NoSuchElementException;

/**
 * Created by wk on 2016/3/24.
 */
public class OSSFileUtils extends FileUtils {
    private static Log logger = LogFactory.getLog(OSSFileUtils.class);
    public final static String BUCKET_NAME_PRIVATE = "gsb-private";
    public final static String BUCKET_NAME = "gsb-public";

    private final static String accessKeyId = "R1sKwTkL14FfqUUn";
    private final static String accessKeySecret = "NsocI4ld0AGCiuNQfwXLUcOUYfW9vN";

    private final static String HTTP = "http://";
    private final static String DOMAIN = "oss-cn-beijing.aliyuncs.com";
    private final static String endpoint = HTTP + DOMAIN;

    // wk 测试
//    private final static String accessKeyId = "3LFaiQ19j9Blo7uy";
//    private final static String accessKeySecret = "UEMhyKu7qNDSVBKfgyOJwFZMzqbw4o";
//    private final static String BUCKET_NAME = "wk-dev-test";

    /**
     * 最大活跃数
     */
    private static final int maxActive = 50;

    /**
     * 最大等待时间
     */
    private static final long maxWait = 10 * 60 * 1000;

    public static final String FILE_RES_NAME = "fileName";
    public static final String FILE_RES_URL = "url";

    //阿里云OSSClient池
    private static GenericObjectPool<OSSClient> clientpool = null;

    static {
        initClientPool();
    }

    private static void initClientPool() {
        clientpool = new GenericObjectPool<OSSClient>(new OSSClientFactory(endpoint, accessKeyId,
                accessKeySecret), maxActive, GenericObjectPool.WHEN_EXHAUSTED_BLOCK, maxWait);
        // 改为先进先出
        clientpool.setLifo(false);
    }

    /**
     * 文件上传
     *
     * @param folder
     * @param file
     * @return
     */
    public static String uploadFile(String folder, MultipartFile file) {
        if (null == file) {
            return "";
        }
        try {
            InputStream input = file.getInputStream();
            return uploadFile(BUCKET_NAME, folder, input, getExtName(file.getOriginalFilename()), file.getSize(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 文件上传
     *
     * @param folder
     * @param file
     * @return
     */
    public static String uploadFile(String folder, File file) {
        if (null == file) {
            return "";
        }
        try {
            InputStream input = new FileInputStream(file);
            return uploadFile(BUCKET_NAME, folder, input, getExtName(file.getName()), file.length(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 私有文件上传
     *
     * @param folder
     * @param file
     * @return
     */
    public static String uploadFilePrivate(String folder, File file) {
        if (null == file) {
            return "";
        }
        try {
            InputStream input = new FileInputStream(file);
            return uploadFile(BUCKET_NAME_PRIVATE, folder, input, getExtName(file.getName()), file.length(), CannedAccessControlList.Private);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 私有文件上传
     *
     * @param folder
     * @param file
     * @return
     */
    public static String uploadFilePrivate(String folder, MultipartFile file) {
        if (null == file) {
            return "";
        }
        try {
            InputStream input = file.getInputStream();
            return uploadFile(BUCKET_NAME_PRIVATE, folder, input, getExtName(file.getName()), file.getSize(), CannedAccessControlList.Private);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 上传文件
     *
     * @param bucketName
     * @param forder     文件夹
     * @param fileStream 文件流
     * @param extName    文件后缀
     * @param fileSize   文件大小
     * @param acl        访问权限（设置null则继承自bucket）
     * @return
     */
    public static String uploadFile(String bucketName, String forder, InputStream fileStream,
                                    String extName, long fileSize, CannedAccessControlList acl) {
        String key = "";
        OSSClient client = getOSSClient();
        if (client != null) {
            try {
                forder = StringUtils.trimToEmpty(forder);
                if (StringUtils.isNotBlank(forder)) {
                    if (!forder.startsWith("/")) {
                        forder = "/" + forder;
                    }
                    if (!forder.endsWith("/")) {
                        forder = forder + "/";
                    }
                }

                fileStream = new BufferedInputStream(fileStream);
                fileStream.mark((int) (fileSize + 1));  // 记录数量
                String file_name = SecurityUtils.MD5Encode(fileStream);
                fileStream.reset();     // 文件流reset

                // 创建上传Object的Metadata
                ObjectMetadata meta = new ObjectMetadata();
                // 必须设置ContentLength
                meta.setContentLength(fileSize);
                // 可以在metadata中标记文件类型
                extName = StringUtils.trimToEmpty(extName).toLowerCase();
                key = forder + file_name + extName;

                String contentType = null;
                if (StringUtils.isNotBlank(extName)) {
                    if (key.endsWith(".jpg") || key.endsWith(".jpeg")) {
                        contentType = "image/jpeg";
                    } else if (key.endsWith(".gif")) {
                        contentType = "image/gif";
                    } else if (key.endsWith(".ico")) {
                        contentType = "image/x-icon";
                    } else if (key.endsWith(".png")) {
                        contentType = "image/png";
                    } else if (key.endsWith(".tiff")) {
                        contentType = "image/tiff";
                    } else if (key.endsWith(".pdf")) {
                        contentType = "application/pdf";
                    } else if (key.endsWith(".doc") || key.endsWith(".docx") || key.endsWith(".rtf") || key.endsWith(".dot") || key.endsWith(".dotx")) {
                        contentType = "application/msword";
                    }
                }

                if (StringUtils.isNotBlank(contentType)) {
                    meta.setContentType(contentType);
                }

                if (null != acl) {
                    meta.setObjectAcl(acl);
                }

                meta.setCacheControl("max-age=31104000");

                if (key.startsWith("/")) {
                    key = key.replaceFirst("/", "");
                }

                // 上传Object.
                client.putObject(bucketName, key, fileStream, meta);

                if (!key.startsWith("/")) {
                    key = "/" + key;
                }

                if (bucketName.equals(BUCKET_NAME_PRIVATE)) {
                    return key;
                } else {
                    return HTTP + bucketName + "." + DOMAIN + key;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("oss client uploadFile exception", ex);
            } finally {
                releaseOSSClient(client);
            }
            return key;
        } else {
            return key;
        }
    }

    public static void deleteObject(String objectName) {
        deleteObject(BUCKET_NAME, objectName);
    }


    public static void deleteObject(String bucketName, String objectName) {
        OSSClient client = getOSSClient();
        try {
            deleteObject(client, bucketName, objectName);
        } catch (Exception ex) {
            logger.error("oss client deleteObject exception", ex);
        } finally {
            releaseOSSClient(client);
        }
    }

    public static InputStream downloadFile(String key) {
        return downloadFile(BUCKET_NAME, key);
    }

    public static InputStream downloadFile(String bucketName, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        OSSClient client = getOSSClient();
        if (client != null) {
            try {
                if (key.startsWith("/")) {
                    key = key.replaceFirst("/", "");
                }
                OSSObject object = client.getObject(bucketName, key);
                if (object != null) {
                    return object.getObjectContent();
                } else {
                    return null;
                }
            } catch (Exception ex) {
                logger.error("oss client downloadFile exception", ex);
            } finally {
                releaseOSSClient(client);
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Object
     *
     * @param bucketName
     * @param objectName
     */
    private static void deleteObject(OSSClient client, String bucketName, String objectName) {
        if (client != null) {
            if (objectName.startsWith("/")) {
                objectName = objectName.replaceFirst("/", "");
            }
            client.deleteObject(bucketName, objectName);
        }
    }

    /**
     * 从池中获取OssClient
     *
     * @return
     */
    private static OSSClient getOSSClient() {
        OSSClient client = null;
        try {
            client = clientpool.borrowObject();
        } catch (NoSuchElementException e) {
            logger.error("oss client get exception", e);
        } catch (IllegalStateException e) {
            logger.error("oss client get exception", e);
        } catch (Exception e) {
            logger.error("oss client get exception", e);
        }
        return client;
    }

    public static boolean checkFileExist(String key) {
        return checkFileExist(BUCKET_NAME, key);
    }

    public static boolean checkFileExist(String bucketName, String key) {
        OSSClient client = getOSSClient();
        if (client != null) {
            if (key.startsWith("/")) {
                key = key.replaceFirst("/", "");
            }
            try {

                return client.doesObjectExist(bucketName, key);
            } catch (Exception ex) {
                logger.error("oss client downloadFile exception ,key=" + key, ex);
                return false;
            } finally {
                releaseOSSClient(client);
            }
        } else {
            return false;
        }
    }

    /**
     * 释放OssClient
     *
     * @param client
     */
    private static void releaseOSSClient(OSSClient client) {
        if (client != null) {
            try {
                clientpool.returnObject(client);
            } catch (Exception ex) {
                logger.error("oss client release exception", ex);
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            File file = new File("D:/test/image/pic5.jpg");
//            String key = uploadFile("test", file);
//            System.out.println(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
