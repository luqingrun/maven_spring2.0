package com.gongsibao.sys.controllers.captcha;

import com.gongsibao.captcha.service.CaptchaService;
import com.gongsibao.common.util.captcha.Captcha;
import com.gongsibao.common.util.captcha.CaptchaUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码action
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    Log log = LogFactory.getLog(CaptchaController.class);

    @Autowired
    private CaptchaService captchaService;

    @RequestMapping(value = "randomkey.json")
    public Map<String, Object> randomKey() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("randomKey", RandomStringUtils.randomAlphanumeric(32));
        return result;
    }

    @RequestMapping(value = "")
    public void captcha(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam("randomKey") String randomKey) {
        /*
         * 4个字节、宽100高50
         */
        Captcha captcha = CaptchaUtils.getImageValidateCode();

        ServletOutputStream so = null;
        try {
            so = response.getOutputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(captcha.getImage(), "PNG", bos);
            byte[] buf = bos.toByteArray();
            so.write(buf);

            captchaService.setCaptchaText(randomKey, captcha.getText());
            log.info("captcha=" + captcha.getText() + "| randomkey : " + randomKey);
        } catch (IOException e) {
            log.error("验证码response错误", e);
        } finally {
            if (null != so) {
                try {
                    so.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
