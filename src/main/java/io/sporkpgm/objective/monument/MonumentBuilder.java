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
import org.dom4j.Document;
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
			String name = destroyables.attributeValue("name");

			String types = destroyables.attributeValue("materials");
			String[] names = new String[]{};
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
			}

			String complete = destroyables.attributeValue("completion");
			int completion = 100;

			if(complete != null) {
				if(complete.endsWith("%")) {
					complete = complete.substring(0, complete.length() - 1);
				}

				completion = Integer.parseInt(complete);
			}

			String team = destroyables.attributeValue("owner");
			SporkTeam owner = null;
			if(team != null) {
				owner = SporkMap.getMap().getTeam(team);
			}

			Log.info("Found " + XMLUtil.getElements(destroyables, "destroyable").size() + " different 'destroyable' elements");
			for(Element destroyable : XMLUtil.getElements(destroyables, "destroyable")) {
				name = (destroyable.attributeValue("name") != null ? destroyable.attributeValue("name") : name);

				types = destroyable.attributeValue("materials");
				if(types != null) {
					names = new String[]{types};
					if(types.contains(";")) {
						types.split(";");
					}
				}

				materialList = new ArrayList<>();
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

				complete = destroyable.attributeValue("completion");

				if(complete != null) {
					if(complete.endsWith("%")) {
						complete = complete.substring(0, complete.length() - 1);
					}

					completion = Integer.parseInt(complete);
				}

				team = destroyable.attributeValue("owner");
				if(team != null) {
					owner = SporkMap.getMap().getTeam(team);
				}

				Region region = RegionBuilder.parseCuboid(((Element) destroyable.elements().get(0)));

				if(name == null) {
					throw new ModuleLoadException("A Monument name could not be found");
				} else if(materials.length == 0) {
					throw new ModuleLoadException("No Materials were supplied");
				} else if(materialList.contains(null)) {
					throw new ModuleLoadException("An invalid list of Materials was found");
				} else if(completion <= 0) {
					throw new ModuleLoadException("Completion % must be greater than 0");
				} else if(owner == null) {
					throw new ModuleLoadException("The owner of a Monument can't be null");
				} else if(region == null) {
					throw new ModuleLoadException("The region of a Monument can't be null");
				}

				modules.add(new MonumentObjective(name, materials, region, owner, completion));
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

}
