package com.febs.shop.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.shop.domain.OrderDelivery;

public interface OrderDeliveryService extends IService<OrderDelivery> {
	public List<OrderDelivery> getDeliveryByOrderId(Long orderId);

}
