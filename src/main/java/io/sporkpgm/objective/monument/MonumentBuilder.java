package io.sporkpgm.objective.monument;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.RegionBuilder;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.region.types.CuboidRegion;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.util.XMLUtil;
import org.bukkit.Material;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(documentable = false)
public class MonumentBuilder extends Builder{
	public MonumentBuilder(Document document){
		super(document);
	}

	public MonumentBuilder(SporkMap map){
		super(map);
	}

	@Override
	public List<Module> build() throws ModuleLoadException, InvalidRegionException{
		List<Module> modules = new ArrayList<>();
		Element root = getRoot();
		String name = getRoot().element("destroyables").attributeValue("name");
		Material material = Material.getMaterial(getRoot().element("destroyables").attributeValue("materials"));
		for (Element element : XMLUtil.getElements(root, "destroyables")){
			for (Element element1 : XMLUtil.getElements(element, "destroyable")){
				String team = element1.attributeValue("owner");
				SporkTeam mapteam = map.getTeam(team);
				CuboidRegion block = RegionBuilder.parseCuboid(element1.element("cuboid"));
				modules.add(new MonumentObjective(name, material, block, mapteam));
			}
		}


		return modules;
	}
}
