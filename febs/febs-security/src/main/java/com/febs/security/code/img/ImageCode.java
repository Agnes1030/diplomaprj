package com.febs.security.code.img;


import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import com.febs.security.code.ValidateCode;

public class ImageCode extends ValidateCode {

    private static final long serialVersionUID = 1111544715170749226L;

    private BufferedImage image;


    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code, expireTime);
        this.image = image;
    }


    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
