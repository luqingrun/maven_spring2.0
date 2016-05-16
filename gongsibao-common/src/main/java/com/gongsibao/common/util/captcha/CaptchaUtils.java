package com.gongsibao.common.util.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 验证码生成工具
 */
public class CaptchaUtils {
    private static final int DEFAULT_TEXTLEN = 4;        //验证码默认长度是4
    private static final int DEFAULT_WIDTH = 100;        //验证码默认宽度100
    private static final int DEFAULT_HEIGHT = 50;        //验证码默认高度50

    public static Captcha getImageValidateCode() {
        return getImageValidateCode(DEFAULT_TEXTLEN);
    }

    public static Captcha getImageValidateCode(String text) {
        return getImageValidateCode(text, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static Captcha getImageValidateCode(int textLen) {
        return getImageValidateCode(textLen, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static Captcha getImageValidateCode(int textLen, int width, int height) {
        String text = getKaptcha(textLen, width, height).createText();
        return getImageValidateCode(text, width, height);
    }

    public static Captcha getImageValidateCode(String text, int width, int height) {
        BufferedImage captchaProducerImage = getKaptcha(text.length(), width, height).createImage(text);
        Captcha captcha = new Captcha(text, captchaProducerImage);
        return captcha;
    }


    /**
     * kaptcha.border  是否有边框  默认为true  我们可以自己设置yes，no  
     * kaptcha.border.color   边框颜色   默认为Color.BLACK  
     * kaptcha.border.thickness  边框粗细度  默认为1  
     * kaptcha.producer.impl   验证码生成器  默认为DefaultKaptcha  
     * kaptcha.textproducer.impl   验证码文本生成器  默认为DefaultTextCreator  
     * kaptcha.textproducer.char.string   验证码文本字符内容范围  默认为abcde2345678gfynmnpwx  
     * kaptcha.textproducer.char.length   验证码文本字符长度  默认为5  
     * kaptcha.textproducer.font.names    验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)  
     * kaptcha.textproducer.font.size   验证码文本字符大小  默认为40  
     * kaptcha.textproducer.font.color  验证码文本字符颜色  默认为Color.BLACK  
     * kaptcha.textproducer.char.space  验证码文本字符间距  默认为2  
     * kaptcha.noise.impl    验证码噪点生成对象  默认为DefaultNoise  
     * kaptcha.noise.color   验证码噪点颜色   默认为Color.BLACK  
     * kaptcha.obscurificator.impl   验证码样式引擎  默认为WaterRipple  
     * kaptcha.word.impl   验证码文本字符渲染   默认为DefaultWordRenderer  
     * kaptcha.background.impl   验证码背景生成器   默认为DefaultBackground  
     * kaptcha.background.clear.from   验证码背景颜色渐进   默认为Color.LIGHT_GRAY  
     * kaptcha.background.clear.to   验证码背景颜色渐进   默认为Color.WHITE  
     * kaptcha.image.width   验证码图片宽度  默认为200  
     * kaptcha.image.height  验证码图片高度  默认为50
     */

    private static ThreadLocal<DefaultKaptcha> threadLocal = new ThreadLocal<>();
    private static Map<String, Config> configMap = new HashMap<>();

    private static DefaultKaptcha getKaptcha(int textLen, int width, int height) {
        DefaultKaptcha defaultKaptcha = threadLocal.get();
        if (defaultKaptcha == null) {
            defaultKaptcha = new DefaultKaptcha();
            threadLocal.set(defaultKaptcha);
        }
        defaultKaptcha = threadLocal.get();
        String key = textLen + "_" + width + "_" + height;
        Config config = configMap.get(key);
        if (config == null) {
            Properties properties = new Properties();
            properties.setProperty("kaptcha.textproducer.char.length", "" + textLen);
            properties.setProperty("kaptcha.image.width", "" + width);
            properties.setProperty("kaptcha.image.height", "" + height);
            properties.setProperty("kaptcha.border", "no");
            config = new Config(properties);
            configMap.put(key, config);
        }
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
