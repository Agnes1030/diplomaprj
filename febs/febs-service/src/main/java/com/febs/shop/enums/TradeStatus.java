package com.febs.shop.enums;

/**
 * 订单交易状态
 */
public enum TradeStatus {
	trading(1, "交易中"), trade_success(2, "交易完成"), trade_cancel(3, "已取消"), trade_refund(4, "申请退款中"),
	trade_norefund(5, "拒绝退款"), trade_refunding(6, "退款中"), trade_refundsuccess(7, "退款完成"), trade_finish(9, "交易结束");

	private int tradeStatus;
	private String statusText;

	private TradeStatus(int status, String statusText) {
		this.tradeStatus = status;
		this.statusText = statusText;
	}

	public int getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public static String getTradeEnumText(int key) {
		TradeStatus[] tradeStatus = values();
		for (TradeStatus stateEnum : tradeStatus) {
			if (stateEnum.getTradeStatus() == key) {
				return stateEnum.getStatusText();
			}
		}
		return null;
	}
}
