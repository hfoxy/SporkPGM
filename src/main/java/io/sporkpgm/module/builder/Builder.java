package io.sporkpgm.module.builder;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import org.dom4j.Document;

import java.lang.reflect.Constructor;
import java.util.List;

public abstract class Builder {

	SporkMap map;
	Document document;

	public Builder(Document document) {
		this.document = document;
	}

	public Builder(SporkMap map) {
		this(map.getDocument());
		this.map = map;
	}

	public abstract List<Module> build();

	public BuilderAbout getInfo() {
		return new BuilderAbout(this.getClass());
	}

	public static boolean isDocumentable(Class<? extends Builder> clazz) throws Exception {
		Document document = null;
		Constructor constructor = clazz.getConstructor(Document.class);
		constructor.setAccessible(true);
		Builder builder = (Builder) constructor.newInstance(document);
		BuilderAbout about = builder.getInfo();
		return about.isDocumentable();
	}

}