package io.sporkpgm.module.modules.info;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;

@ModuleInfo(name = "InfoModule", description = "Contains information about the map", listener = false, multiple = false)
public class InfoModule extends Module {

	String name;

	public String getName() {
		return name;
	}

	public Class<? extends Builder> builder() {
		return InfoBuilder.class;
	}

}
