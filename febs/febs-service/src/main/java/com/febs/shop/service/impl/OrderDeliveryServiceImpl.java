package com.febs.shop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.domain.OrderDelivery;
import com.febs.shop.service.OrderDeliveryService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OrderDeliveryServiceImpl extends BaseService<OrderDelivery> implements OrderDeliveryService {

	@Override
	public List<OrderDelivery> getDeliveryByOrderId(Long orderId) {
		Example example = new Example(OrderDelivery.class);
		example.createCriteria().andEqualTo("orderId", orderId);
		return this.selectByExample(example);
	}

}
