package com.febs.shop.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.OrderMapper;
import com.febs.shop.domain.Order;
import com.febs.shop.service.OrderService;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OrderServiceImpl extends BaseService<Order> implements OrderService {
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public int deleteOrders(String ids) {
		List<String> orderIds = Arrays.asList(ids.split(","));
		return this.batchDelete(orderIds, "id", Order.class);
	}

	@Override
	public int updateRemarks(String remarks, String orderId) {
		return orderMapper.updateRemarks(remarks, orderId);
	}

	@Override
	public List<Order> getListByUser(Long userId) {
		Example example = new Example(Order.class);
		example.createCriteria().andEqualTo("buyerId", userId);
		return this.selectByExample(example);
	}

	@Override
	public int queryDateAfterCount(String dateStr) {
		return orderMapper.queryDateAfterCount(dateStr);
	}

	@Override
	public int queryPayAfterCount(String dateStr) {
		return orderMapper.queryPayAfterCount(dateStr);
	}

}
