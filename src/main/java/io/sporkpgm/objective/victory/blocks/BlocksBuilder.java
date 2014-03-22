package io.sporkpgm.objective.victory.blocks;

import com.mcath.commons.util.string.StringUtil;
import com.mcath.manager.map.AthenaMap;
import com.mcath.manager.map.exception.ModuleLoadException;
import com.mcath.manager.module.skyfall.SkyfallModule;
import com.mcath.manager.objective.victory.VictoryObjective;
import com.mcath.manager.region.Region;
import com.mcath.manager.team.AthenaTeam;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class BlocksBuilder {

	public static List<BlocksObjective> build(AthenaMap map) throws ModuleLoadException {
		Element root = map.getDocument().getRootElement();
		List<BlocksObjective> blocks = new ArrayList<>();
		Element blocksElement = root.element("blocks");
		if(blocksElement == null) return new ArrayList<>();

		for(Object object : blocksElement.elements("block")) {
			if(!(object instanceof Element)) {
				continue;
			}

			Element block = (Element) object;

			String[] required = {"name", "team", "material", "region", "place", "color"};
			for(String req : required) {
				if(block.element(req) == null || block.element(req).getText() == null) {
					throw new ModuleLoadException("Failed to find value for '" + req + "' when parsing BlocksObjective");
				}
			}

			String name = block.element("name").getText();
			AthenaTeam team = map.getTeam(block.element("team").getText());
			Material material = StringUtil.convertStringToMaterial(block.element("material").getText());
			Region region = map.getRegion(block.element("region").getText());
			Region place = map.getRegion(block.element("place").getText());
			ChatColor color = StringUtil.convertStringToChatColor(block.element("color").getText());
			int damage = StringUtil.convertStringToInteger(block.element("material").attributeValue("damage"), 0);
			int fall = StringUtil.convertStringToInteger(block.attributeValue("fall"), 2);

			if(name == null)
				throw new ModuleLoadException("Value for 'name' is null");
			else if(team == null)
				throw new ModuleLoadException("Value for 'team' is null");
			else if(material == null)
				throw new ModuleLoadException("Value for 'material' is null");
			else if(region == null)
				throw new ModuleLoadException("Value for 'region' is null");
			else if(place == null)
				throw new ModuleLoadException("Value for 'place' is null");
			else if(color == null)
				throw new ModuleLoadException("Value for 'color' is null");

			SkyfallModule module = new SkyfallModule(map, material, damage, region, fall);
			VictoryObjective objective = new VictoryObjective(name, team, material, damage, place, color);
			blocks.add(new BlocksObjective(objective, module));
		}

		return blocks;
	}

}
