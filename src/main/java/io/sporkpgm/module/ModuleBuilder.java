package io.sporkpgm.module;

import io.sporkpgm.map.SporkMap;
import org.dom4j.Document;

import java.util.List;

public abstract class ModuleBuilder {

	Document document;

	public ModuleBuilder(Document document) {
		this.document = document;
	}

	public abstract List<Module> build();

}
