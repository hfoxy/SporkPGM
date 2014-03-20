package io.sporkpgm.module.modules.info;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class InfoBuilder extends Builder {

	public InfoBuilder(Document document) {
		super(document);
	}

	public InfoBuilder(SporkMap map) {
		super(map);
	}

	public List<Module> build() throws ModuleLoadException {
		List<Module> modules = new ArrayList<>();

		Element root = document.getRootElement();
		String name = root.element("name").getText();
		if(name == null) {
			throw new ModuleLoadException("Map names can't be null");
		}

		String version = root.element("version").getText();
		if(version == null) {
			throw new ModuleLoadException("Map versions can't be null");
		}

		String objective = root.element("objective").getText();
		if(objective == null) {
			throw new ModuleLoadException("Map objectives can't be null");
		}

		List<Contributor> authors = new ArrayList<>();
		Element authorsElement = root.element("authors");
		for(Element author : XMLUtil.getElements(authorsElement, "author")) {
			if(author.getText() == null)
				continue;
			authors.add(new Contributor(author.getText(), author.attributeValue("contribution")));
		}

		if(authors.size() == 0) {
			throw new ModuleLoadException("Maps must have at least 1 author");
		}

		List<Contributor> contributors = new ArrayList<>();
		Element contributorsElement = root.element("contributors");
		if(contributorsElement != null) {
			for(Element contributor : XMLUtil.getElements(contributorsElement, "contributor")) {
				if(contributor.getText() == null)
					continue;
				contributors.add(new Contributor(contributor.getText(), contributor.attributeValue("contribution")));
			}
		}

		boolean friendlyFire = XMLUtil.parseBoolean(root.elementText("friendlyfire"), false);

		modules.add(new InfoModule(name, version, objective, authors, contributors, friendlyFire));
		return modules;
	}

}
