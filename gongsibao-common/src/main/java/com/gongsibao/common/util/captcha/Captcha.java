package com.gongsibao.common.util.captcha;

import java.awt.image.BufferedImage;

/**
 * Created by luqingrun on 16/3/29.
 */
public class Captcha {
    private String text;
    private BufferedImage image;

    public Captcha(String text,BufferedImage image) {
        this.image = image;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
