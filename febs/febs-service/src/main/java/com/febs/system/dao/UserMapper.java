package com.febs.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.febs.common.config.MyMapper;
import com.febs.system.domain.MyUser;
import com.febs.system.domain.UserWithRole;

public interface UserMapper extends MyMapper<MyUser> {
	public static class UserSqlProvider {
		// 2020-06-01 00:00:00
		public String queryDateAfterCount(String dateStr) {
			return "SELECT count(1) FROM t_user where create_time>='" + dateStr + "'";
		}
	}

	// 今日新增用户总量
	@SelectProvider(type = UserSqlProvider.class, method = "queryDateAfterCount")
	public int queryDateAfterCount(String dateStr);

	List<MyUser> findUserWithDept(MyUser user);

	List<UserWithRole> findUserWithRole(Long userId);

	MyUser findUserProfile(MyUser user);
}