package com.febs.cms.service;

import java.util.List;

import com.febs.cms.domain.Setting;
import com.febs.common.service.IService;

public interface SettingService extends IService<Setting> {
	public Setting findByKey(String key);
	Setting findById(Long id);
	public void updateSetting(Setting setting);
	public void updateByExample(Setting setting);
	public void addSetting(Setting setting);
	public List<Setting> findAllSettings(Setting setting);
	 public void refreshSetting();
}
