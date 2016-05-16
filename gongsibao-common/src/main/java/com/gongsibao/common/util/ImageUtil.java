package com.gongsibao.common.util;

import com.alibaba.simpleimage.ImageFormat;
import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.ImageWrapper;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.*;
import com.alibaba.simpleimage.util.ImageUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片处理工具类(基于阿里开源simpleimage)
 */
public class ImageUtil {

    static Log log = LogFactory.getLog(ImageUtil.class);

    private static String[] VALID_IMAGE_TYPE = {".jpg", ".png", ".gif", ".bmp", ".tif"};

    @SuppressWarnings("serial")
    private static final Map<String, String> types = new HashMap<String, String>() {
        {
            put("image/gif", ".gif");
            put("image/jpeg", ".jpg");
            put("image/jpg", ".jpg");
            put("image/png", ".png");
            put("image/bmp", ".bmp");
        }
    };

    public static String getSuffix(String mime) {
        return types.get(mime);
    }

    /**
     * 图片尺寸等比压缩,
     *
     * @param oldFile
     * @param width
     * @param height
     */
    public static ByteArrayOutputStream resizeImage(InputStream oldFile, int width, int height) {
        return resizeImage(oldFile, width, height, 1.0f);
    }

    public static ByteArrayOutputStream resizeImage(InputStream oldFile, int width, int height,
                                                    float quality) {
        ByteArrayOutputStream outPutStream = null;
        ImageRender wr = null;
        try {
            //图片格式
            ImageFormat imageFormat = null;
            try {
                imageFormat = ImageUtils.identifyFormat(oldFile);
            } catch (Exception ex) {
            }
            if (imageFormat == null) {
                imageFormat = ImageFormat.JPEG;
            }
            //压缩
            ScaleParameter scaleParam = new ScaleParameter(width, height);
            ImageRender sr = new ScaleRender(oldFile, scaleParam);
            WriteParameter writeParam = new WriteParameter();
            writeParam.setDefaultQuality(quality);
            outPutStream = new ByteArrayOutputStream();
            wr = new WriteRender(sr, outPutStream, imageFormat, writeParam);
            wr.render();

        } catch (Exception e) {
            outPutStream = null;
            log.error("resizeImage exception", e);
        } finally {
            IOUtils.closeQuietly(oldFile);
            if (wr != null) {
                try {
                    wr.dispose();
                } catch (SimpleImageException ignore) {
                }
            }
        }
        return outPutStream;
    }

    public static File resizeImage(InputStream oldFile, String newFilePathName, int width,
                                   int height, float quality) {
        ImageRender wr = null;
        File file = null;
        try {
            //图片格式
            ImageFormat imageFormat = null;
            try {
                imageFormat = ImageUtils.identifyFormat(oldFile);
            } catch (Exception ex) {
            }
            if (imageFormat == null) {
                imageFormat = ImageFormat.JPEG;
            }
            //压缩
            ScaleParameter scaleParam = new ScaleParameter(width, height);
            ImageRender sr = new ScaleRender(oldFile, scaleParam);
            WriteParameter writeParam = new WriteParameter();
            writeParam.setDefaultQuality(quality);
            file = new File(newFilePathName);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            wr = new WriteRender(sr, new File(newFilePathName), imageFormat, writeParam);
            wr.render();
        } catch (Exception e) {
            file = null;
            log.error("resizeImage exception", e);
        } finally {
            IOUtils.closeQuietly(oldFile);
            if (wr != null) {
                try {
                    wr.dispose();
                } catch (SimpleImageException ignore) {
                }
            }
        }
        return file;
    }

    /**
     * 图片尺寸等比压缩
     *
     * @param oldFile
     * @param newFilePathName 完整文件路径名称 例如：/homw/images/test.jpg
     * @param width
     * @param height
     */
    public static File resizeImage(InputStream oldFile, String newFilePathName, int width,
                                   int height) {
        return resizeImage(oldFile, newFilePathName, width, height, 1.0f);
    }

    /**
     * 生成方形缩略图
     *
     * @param oldFile
     * @param newFilePathName 完整文件路径名称 例如：/homw/images/test.jpg
     * @param width
     * @param height
     */
    public static File createImageThumb(InputStream oldFile, String newFilePathName, int width,
                                        int height) {
        ImageRender wr = null;
        File file = null;
        try {
            //图片格式
            ImageFormat imageFormat = null;
            try {
                imageFormat = ImageUtils.identifyFormat(oldFile);
            } catch (Exception ex) {
            }
            if (imageFormat == null) {
                imageFormat = ImageFormat.JPEG;
            }

            //裁剪
            ImageRender rr = new ReadRender(oldFile);
            ImageWrapper iw = rr.render();
            int sourceWidth = iw.getWidth();
            int sourceHeight = iw.getHeight();
            int newSize = sourceWidth > sourceHeight ? sourceHeight : sourceWidth;
            CropParameter cropParam = new CropParameter((sourceWidth - newSize) / 2,
                    (sourceHeight - newSize) / 2, newSize, newSize);
            CropRender cropRender = new CropRender(iw, cropParam);

            //压缩
            ScaleParameter scaleParam = new ScaleParameter(width, height);
            ImageRender sr = new ScaleRender(cropRender, scaleParam);
            WriteParameter writeParam = new WriteParameter();
            writeParam.setDefaultQuality(1.0f);
            file = new File(newFilePathName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            wr = new WriteRender(sr, file, imageFormat, writeParam);
            wr.render();
        } catch (Exception e) {
            file = null;
            log.error("resizeImage exception", e);
        } finally {
            IOUtils.closeQuietly(oldFile); //图片文件输入输出流必须记得关闭
            if (wr != null) {
                try {
                    wr.dispose(); //释放simpleImage的内部资源
                } catch (SimpleImageException ignore) {
                }
            }
        }
        return file;
    }

    /**
     * 生成缩略图
     */
    public static ByteArrayOutputStream createImageThumb(InputStream oldFile, int x, int y,
                                                         int width, int height) {
        ImageRender wr = null;
        ByteArrayOutputStream outputStream = null;
        try {
            //图片格式
            ImageFormat imageFormat = null;
            try {
                imageFormat = ImageUtils.identifyFormat(oldFile);
            } catch (Exception ex) {
            }
            if (imageFormat == null) {
                imageFormat = ImageFormat.JPEG;
            }

            //裁剪
            ImageRender rr = new ReadRender(oldFile);
            ImageWrapper iw = rr.render();
            CropParameter cropParam = new CropParameter(x, y, width, height);
            CropRender cropRender = new CropRender(iw, cropParam);

            //压缩
            ScaleParameter scaleParam = new ScaleParameter(width, height);
            ImageRender sr = new ScaleRender(cropRender, scaleParam);
            WriteParameter writeParam = new WriteParameter();
            writeParam.setDefaultQuality(1.0f);
            outputStream = new ByteArrayOutputStream();
            wr = new WriteRender(sr, outputStream, imageFormat, writeParam);
            wr.render();
        } catch (Exception e) {
            outputStream = null;
            log.error("createImageThumb exception", e);
        } finally {
            IOUtils.closeQuietly(oldFile); //图片文件输入输出流必须记得关闭
            if (wr != null) {
                try {
                    wr.dispose(); //释放simpleImage的内部资源
                } catch (SimpleImageException ignore) {
                }
            }
        }
        return outputStream;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getImageExtName(String fileName) {
        if (fileName.contains(".")) {
            String extName = fileName.substring(fileName.lastIndexOf("."), fileName.length())
                    .toLowerCase();
            if (ArrayUtils.contains(VALID_IMAGE_TYPE, extName)) {
                return extName;
            } else {
                return ".jpg";
            }
        } else {
            return ".jpg";
        }
    }

    /**
     * 生成小图片文件名（头像50*50）
     *
     * @param originalImageName
     * @return
     */
    public static String generateSmallPicName(String originalImageName) {
        if (originalImageName.contains(".")) {
            int index = originalImageName.lastIndexOf(".");
            String extName = originalImageName.substring(originalImageName.lastIndexOf("."),
                    originalImageName.length()).toLowerCase();
            String filename = originalImageName.substring(0, index);
            return filename + "_s" + extName;
        } else {
            return originalImageName + "_s";
        }
    }

    /**
     * 生成小图片文件名（尺寸）
     *
     * @param originalImageName
     * @return
     */
    public static String generateSmallPicName(String originalImageName, int width, int height) {
        if (originalImageName.contains(".")) {
            int index = originalImageName.lastIndexOf(".");
            String extName = originalImageName.substring(originalImageName.lastIndexOf("."),
                    originalImageName.length()).toLowerCase();
            String filename = originalImageName.substring(0, index);
            return filename + "_" + width + "_" + height + "_s" + extName;
        } else {
            return originalImageName + "_" + width + "_" + height + "_s";
        }
    }

    /**
     * 生成缩略图文件名
     *
     * @param originalImageName
     * @return
     */
    public static String generateThumbImgageName(String originalImageName) {
        if (originalImageName.contains(".")) {
            int index = originalImageName.lastIndexOf(".");
            String extName = originalImageName.substring(originalImageName.lastIndexOf("."),
                    originalImageName.length()).toLowerCase();
            String filename = originalImageName.substring(0, index);
            return filename + "_thumb" + extName;
        } else {
            return originalImageName + "_thumb";
        }
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
        } else {
            return "";
        }
    }

    /**
     * 图片本地临时目录
     *
     * @return
     */
    public static String getPicTempDirPath() {
        String tmpPath = "/tmp/";
        File dir = new File(tmpPath);
        if (!dir.exists()) {
            dir = new File(System.getProperty("user.home") + "/temp/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (dir.exists()) {
                tmpPath = dir.getAbsolutePath() + "/";
            }
        }
        return tmpPath;
    }

    public static void main(String[] args) {
        File dir = new File("D:/test/image/");
        File[] files = dir.listFiles();
        for (File img : files) {
            try {
                if (img.getName().startsWith("pp") || !img.getName().startsWith("p")) {
                    continue;
                }
                System.out.println(img.getName());
                createImageThumb(new FileInputStream(img), "D:/test/images/p" + img.getName(), 150, 150);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
