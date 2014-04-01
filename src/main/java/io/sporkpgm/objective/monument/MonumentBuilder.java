package io.sporkpgm.objective.monument;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.Region;
import io.sporkpgm.region.RegionBuilder;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.StringUtil;
import io.sporkpgm.util.XMLUtil;
import org.bukkit.Material;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(documentable = false)
public class MonumentBuilder extends Builder {

	public MonumentBuilder(SporkMap map) {
		super(map);
	}

	@Override
	public List<Module> build() throws ModuleLoadException, InvalidRegionException {
		List<Module> modules = new ArrayList<>();
		Element root = getRoot();
		List<Element> destroyablesList = XMLUtil.getElements(root, "destroyables");
		Log.info("Found " + destroyablesList.size() + " different 'destroyables' elements");

		for(Element destroyables : destroyablesList) {
			for(Element destroyable : XMLUtil.getElements(destroyables, "destroyable")) {
				modules.add(parseMonument(map, destroyable));
			}

			for(Element destroyables2 : XMLUtil.getElements(destroyables, "destroyables")) {
				for(Element destroyable : XMLUtil.getElements(destroyables2, "destroyable")) {
					modules.add(parseMonument(map, destroyable));
				}
			}
		}

		return modules;

		/*
		List<Module> modules = new ArrayList<>();
		Element root = getRoot();
		String name = getRoot().element("destroyables").attributeValue("name");
		if(name == null) {
			throw new ModuleLoadException("")
		}

		Material[] materials = Material.getMaterial(getRoot().element("destroyables").attributeValue("materials"));

		for(Element element : XMLUtil.getElements(root, "destroyables")) {
			for(Element element1 : XMLUtil.getElements(element, "destroyable")) {
				String team = element1.attributeValue("owner");
				SporkTeam mapteam = map.getTeam(team);
				Element region = (Element) element1.elements().get(0);
				Region block = RegionBuilder.parseCuboid(region);
				modules.add(new MonumentObjective(name, material, block, mapteam));
			}
		}


		return modules;
		*/
	}

	public MonumentObjective parseMonument(SporkMap map, Element element) throws ModuleLoadException, InvalidRegionException {
		String name = XMLUtil.getElementOrParentValue(element, "name");

		String[] names = new String[0];
		String types = XMLUtil.getElementOrParentValue(element, "materials");
		if(types != null) {
			names = new String[]{types};
			if(types.contains(";")) {
				types.split(";");
			}
		}

		List<Material> materialList = new ArrayList<>();
		for(String type : names) {
			Material material = StringUtil.convertStringToMaterial(type);
			if(material == null) {
				throw new ModuleLoadException("'" + type + "' is not a valid Minecraft material");
			}
			materialList.add(material);
		}

		int i = 0;
		Material[] materials = new Material[materialList.size()];
		for(Material material : materials) {
			materials[i] = material;
			i++;
		}

		int completion = 0;
		String complete = XMLUtil.getElementOrParentValue(element, "completion");
		if(complete != null) {
			if(complete.endsWith("%")) {
				complete = complete.substring(0, complete.length() - 1);
			}

			completion = Integer.parseInt(complete);
		}

		SporkTeam other = null;
		String team = XMLUtil.getElementOrParentValue(element, "owner");
		if(team != null) {
			other = SporkMap.getMap().getTeam(team);
		}

		Region region = RegionBuilder.parseCuboid(((Element) element.elements().get(0)));

		SporkTeam owner;
		for(SporkTeam spork : map.getTeams()) {
			if(!spork.equals(other) && spork.isObservers()) {
				owner = spork;
			}
		}

		if(name == null) {
			throw new ModuleLoadException("A Monument name could not be found");
		} else if(materials.length == 0) {
			throw new ModuleLoadException("No Materials were supplied");
		} else if(materialList.contains(null)) {
			throw new ModuleLoadException("An invalid list of Materials was found");
		} else if(completion <= 0) {
			throw new ModuleLoadException("Completion % must be greater than 0");
		} else if(other == null) {
			throw new ModuleLoadException("The owner of a Monument can't be null");
		} else if(region == null) {
			throw new ModuleLoadException("The region of a Monument can't be null");
		}

		return new MonumentObjective(name, materials, region, other, completion);
	}

}
