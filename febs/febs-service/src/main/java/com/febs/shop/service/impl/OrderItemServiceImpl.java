package com.febs.shop.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.OrderItemMapper;
import com.febs.shop.domain.OrderItem;
import com.febs.shop.service.OrderItemService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OrderItemServiceImpl extends BaseService<OrderItem> implements OrderItemService {
	@Autowired
	private OrderItemMapper orderItemMapper;

	@Override
	public List<OrderItem> getOrderItems(Long orderId) {
		Example example = new Example(OrderItem.class);
		example.createCriteria().andEqualTo("orderId", orderId);
		example.setOrderByClause("id");
		return this.selectByExample(example);
	}

	@Override
	public int deleteOrderItems(Long orderId) {
		Example example = new Example(OrderItem.class);
		example.createCriteria().andEqualTo("orderId", orderId);
		return orderItemMapper.deleteByExample(example);
	}

	@Override
	public int deleteOrderItems(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		return this.batchDelete(list, "id", OrderItem.class);
	}

}
