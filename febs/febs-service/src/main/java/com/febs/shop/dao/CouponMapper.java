package com.febs.shop.dao;

import org.apache.ibatis.annotations.Update;

import com.febs.common.config.MyMapper;
import com.febs.shop.domain.Coupon;

public interface CouponMapper extends MyMapper<Coupon> {
	// 这种方式不能动态拼接sql进行判断，provider方式可以判断参数是否为空进行动态拼接
	@Update("UPDATE t_coupon SET take_count = #{takeCount} WHERE id = #{couponId}")
	public Integer updateTakeCount(Integer takeCount, Long couponId);

	@Update("UPDATE t_coupon SET used_count = #{usedCount} WHERE id = #{couponId}")
	public Integer updateUsedCount(Integer usedCount, Long couponId);
}
