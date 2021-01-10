package com.febs.shop.enums;

/**
 * 订单支付状态
 */
public enum PayStatus {
	no_pay(1, "未支付"), zfb_pay(2, "支付宝"), wx_pay(3, "微信支付"), bank_pay(4, "网银支付"),
	amount_pay(5, "余额支付");

	private int payStatus;
	private String statusText;

	private PayStatus(int status, String statusText) {
		this.payStatus = status;
		this.statusText = statusText;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public static String getTradeEnumText(int key) {
		PayStatus[] tradeStatus = values();
		for (PayStatus stateEnum : tradeStatus) {
			if (stateEnum.getPayStatus() == key) {
				return stateEnum.getStatusText();
			}
		}
		return null;
	}
}
