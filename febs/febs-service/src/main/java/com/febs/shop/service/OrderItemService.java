package com.febs.shop.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.shop.domain.OrderItem;

public interface OrderItemService extends IService<OrderItem> {
	public List<OrderItem> getOrderItems(Long orderId);
	public int deleteOrderItems(Long orderId);
    public int deleteOrderItems(String ids);

}
