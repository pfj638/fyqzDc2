package com.fyqz.dc.common;

import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * 仅针对BaseConfiguration
 * @author 主要用于加载策略
 *
 */
class ConfigFileReloadingStrategy extends
		FileChangedReloadingStrategy {

	BaseConfiguration listener;
	
	public ConfigFileReloadingStrategy(BaseConfiguration listener) {
		this.listener = listener;
	}
	public void reloadingPerformed() {
		super.reloadingPerformed();
		listener.configurationReloaded(this.configuration);
	}
}
