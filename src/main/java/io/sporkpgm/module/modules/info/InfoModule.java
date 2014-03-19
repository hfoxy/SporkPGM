package io.sporkpgm.module.modules.info;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;

@ModuleInfo(name = "InfoModule", description = "Contains information about the map", listener = false, multiple = false)
public class InfoModule extends Module {

	String name;

	public String getName() {
		return name;
	}

	public Class<? extends ModuleBuilder> builder() {
		return InfoBuilder.class;
	}

}
