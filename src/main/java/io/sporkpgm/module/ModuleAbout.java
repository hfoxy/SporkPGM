package io.sporkpgm.module;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleAbout {

	BuilderInfo info;
	List<Class<? extends Module>> requires = new ArrayList<>();

	public ModuleAbout(Class<? extends Module> module) {
		Preconditions.checkArgument(module.isAnnotationPresent(BuilderInfo.class), "Module must have a BuilderInfo annotation");
		this.info = module.getAnnotation(BuilderInfo.class);
		this.requires.addAll(Arrays.asList(info.requires()));
	}

	public BuilderInfo getInfo() {
		return info;
	}

	public String getName() {
		return info.name();
	}

	public String getDescription() {
		return info.description();
	}

	public boolean isListener() {
		return info.listener();
	}

	public List<Class<? extends Module>> getRequires() {
		return requires;
	}

}
