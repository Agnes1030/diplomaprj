package com.febs.shop.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.febs.common.domain.ResponseBo;
import com.febs.shop.domain.CartItem;
import com.febs.shop.domain.ShoppingCart;
import com.febs.system.domain.MyUser;

public interface ShoppingCartService {
	public String getKey(HttpServletRequest req, HttpServletResponse resp, MyUser myUser);

	public ShoppingCart mergeCart(String tempKey, MyUser myUser);

	public ResponseBo addCart(HttpServletRequest req, HttpServletResponse resp, MyUser myUser, CartItem item);

	public ResponseBo removeCart(HttpServletRequest req, HttpServletResponse resp, MyUser myUser, CartItem item);
	public ResponseBo clearCart(HttpServletRequest req,HttpServletResponse resp,MyUser myUser);
}
