package com.febs.shop.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.febs.common.config.MyMapper;
import com.febs.shop.domain.Order;

public interface OrderMapper extends MyMapper<Order> {
	public static class OrderSqlProvider {
		public static String updateRemarks(String remarks, String orderId) {
			return "UPDATE t_user_order SET remarks = #{remarks} WHERE id = #{orderId}";
		}

		// 2020-06-01 00:00:00
		public String queryDateAfterCount(String dateStr) {
			return "SELECT count(1) FROM t_user_order where create_time>='" + dateStr + "'";
		}
		public String queryPayAfterCount(String dateStr) {
			return "SELECT count(1) FROM t_user_order where create_time>='"+dateStr+"' and pay_status >1;";
		}
	}

	@UpdateProvider(type = OrderSqlProvider.class, method = "updateRemarks")
	public int updateRemarks(@Param("remarks") String remarks, @Param("orderId") String orderId);
    // 今日订单总量
	@SelectProvider(type = OrderSqlProvider.class, method = "queryDateAfterCount")
	public int queryDateAfterCount(String dateStr);
	// 今日成交订单总量
	@SelectProvider(type = OrderSqlProvider.class, method = "queryPayAfterCount")
	public int queryPayAfterCount(String dateStr);

}
