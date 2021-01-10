package com.febs.shop.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.shop.domain.Order;

public interface OrderService extends IService<Order> {
	public List<Order> getListByUser(Long userId);
	public int deleteOrders(String ids);

	public int updateRemarks(String remarks, String orderId);
	public int queryDateAfterCount(String dateStr);
	public int queryPayAfterCount(String dateStr);
}
