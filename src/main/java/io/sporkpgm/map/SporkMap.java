package io.sporkpgm.map;

import org.dom4j.Document;

import java.io.File;

public class SporkMap {

	protected MapBuilder builder;
	protected Document document;
	protected File folder;

	public SporkMap(MapBuilder builder) {
		this.builder = builder;
		this.document = builder.getDocument();
		this.folder = builder.getFolder();
	}

	public Document getDocument() {
		return document;
	}

	public File getFolder() {
		return folder;
	}

}