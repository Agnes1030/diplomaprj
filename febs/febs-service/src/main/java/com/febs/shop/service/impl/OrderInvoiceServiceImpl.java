package com.febs.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.service.impl.BaseService;
import com.febs.shop.dao.OrderInvoiceMapper;
import com.febs.shop.domain.OrderInvoice;
import com.febs.shop.service.OrderInvoiceService;
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OrderInvoiceServiceImpl extends BaseService<OrderInvoice> implements OrderInvoiceService {
	@Autowired
	private OrderInvoiceMapper orderInvoiceMapper;




}
