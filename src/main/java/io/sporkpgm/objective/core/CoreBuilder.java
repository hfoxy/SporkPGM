package io.sporkpgm.objective.core;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.util.ItemUtil;
import io.sporkpgm.util.NumberUtil;
import io.sporkpgm.util.XMLUtil;
import org.bukkit.Material;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class CoreBuilder extends Builder {

	public CoreBuilder(SporkMap map) {
		super(map);
	}

	@Override
	public List<Module> build() throws ModuleLoadException, InvalidRegionException {
		List<Module> modules = new ArrayList<>();
		Element root = getRoot();

		for(Element element : XMLUtil.getElements(root, "cores")) {
			int leak = NumberUtil.parseInteger(element.attributeValue("leak"));
			String attrValMat = element.attributeValue("material");
			String attrValTeam = element.attributeValue("team");
			Material material = ItemUtil.stringToMaterial(attrValMat);
			if(material == null && attrValMat != null) throw new ModuleLoadException(attrValMat + " is not a valid material");
			SporkTeam team = map.getTeam(attrValTeam);
			if(team == null && attrValTeam != null) throw new ModuleLoadException(attrValTeam + " is not a valid team");
			for(Element coreElem : XMLUtil.getElements(element, "core")) {

			}
		}
		return modules;
	}
}