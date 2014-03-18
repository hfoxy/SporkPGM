package io.sporkpgm.map;

import org.dom4j.Document;

import java.io.File;

public class SporkMap {

	protected Document document;
	protected File folder;

	public SporkMap(Document document, File folder) {
		this.document = document;
		this.folder = folder;
	}

	public Document getDocument() {
		return document;
	}

	public File getFolder() {
		return folder;
	}

}
