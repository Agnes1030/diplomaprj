package com.febs.cms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.febs.cms.dao.SettingMapper;
import com.febs.cms.domain.Setting;
import com.febs.cms.service.SettingService;
import com.febs.common.service.impl.BaseService;

import tk.mybatis.mapper.entity.Example;

@Service("settingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SettingServiceImpl extends BaseService<Setting> implements SettingService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SettingMapper settingMapper;
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;

	@Override
	public Setting findById(Long id) {
		return this.selectByKey(id);
	}

	@Override
	public void updateSetting(Setting setting) {
		this.updateNotNull(setting);
	}

	@Override
	public void addSetting(Setting setting) {
		this.save(setting);
	}

	@Override
	public void refreshSetting() {
		Map<String, Object> vars = new HashMap<>(1);
		List<Setting> settings = this.findAllSettings(null);
		for (Setting set : settings) {
			vars.put(set.getKey(), set.getValue());
		}
		thymeleafViewResolver.setStaticVariables(vars);
	}

	@Override
	public List<Setting> findAllSettings(Setting setting) {
		Example example = new Example(Setting.class);
		if (null != setting && null != setting.getKey()) {
			example.createCriteria().andEqualTo("key", setting.getKey());
		}
		return this.selectByExample(example);
	}

	@Override
	public void updateByExample(Setting setting) {
		log.debug("更新设置");
		Example example = new Example(Setting.class);
		example.createCriteria().andEqualTo("key", setting.getKey());
		this.settingMapper.updateByExample(setting, example);
		this.refreshSetting();
	}

	@Override
	public Setting findByKey(String key) {
		Example example = new Example(Setting.class);
		example.createCriteria().andEqualTo("key", key);
		List<Setting> settings = this.selectByExample(example);
		if (null != settings && settings.size() > 0) {
			return settings.get(0);
		}
		return null;
	}

}
