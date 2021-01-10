package com.febs.shop.enums;

/**
 * 物流状态
 */
public enum DeliveryStatus {
	shipped(1, "已发货(待运输)"), transit(2, "运输中"), distribution(3, "配送中"), signed(4, "已签收");

	private int deliveryStatus;
	private String statusText;

	private DeliveryStatus(int status, String statusText) {
		this.deliveryStatus = status;
		this.statusText = statusText;
	}


	public int getDeliveryStatus() {
		return deliveryStatus;
	}


	public void setDeliveryStatus(int deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}


	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public static String getTradeEnumText(int key) {
		DeliveryStatus[] tradeStatus = values();
		for (DeliveryStatus stateEnum : tradeStatus) {
			if (stateEnum.getDeliveryStatus() == key) {
				return stateEnum.getStatusText();
			}
		}
		return null;
	}
}
