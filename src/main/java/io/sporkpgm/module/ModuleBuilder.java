package io.sporkpgm.module;

import io.sporkpgm.map.SporkMap;

import java.util.List;

public abstract class ModuleBuilder {

	SporkMap map;

	public ModuleBuilder(SporkMap map) {
		this.map = map;
	}

	public abstract List<Module> build();

}
