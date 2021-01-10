package com.febs.security.properties;

public class ImageCodeProperties  {
    private int length = 6;
    private int expireIn = 60;
    private int width = 67;
    private int height = 23;
    private String url = "";
    private String createUrl = "";
    // 处理使用短信验证码认证 URL
    // 处理使用图形验证码认证 URL
    private String loginProcessingUrl = "";

    public ImageCodeProperties() {
        setLength(4);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public String getCreateUrl() {
        return createUrl;
    }

    public void setCreateUrl(String createUrl) {
        this.createUrl = createUrl;
    }

    public String getLoginProcessingUrl() {
        return loginProcessingUrl;
    }

    public void setLoginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
    }
}
