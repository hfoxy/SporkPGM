package io.sporkpgm.filter;

import io.sporkpgm.filter.types.*;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.util.StringUtil;
import io.sporkpgm.util.XMLUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class FilterBuilder {

	public static List<Filter> build(SporkMap map) throws InvalidFilterException {
		Document document = map.getDocument();
		Element root = document.getRootElement();
		List<Filter> filterList = new ArrayList<>();
		filterList.addAll(defaults());

		Element filters = root.element("filters");
		if(filters == null) {
			return filterList;
		}

		for(Element filter : XMLUtil.getElements(filters, "filter")) {
			filterList.add(parseCondition(filter.attributeValue("name"), filter, map));
		}

		return filterList;
	}

	public static Condition parseCondition(String name, Element element, SporkMap map) throws InvalidFilterException {
		List<Condition> conditionList = new ArrayList<>();
		List<Element> conditions = XMLUtil.getElements(element);

		for(Element condition : conditions) {
			Modifier modifier = Modifier.getModifier(condition.getName());
			if(modifier != null) {
				MultiCondition multi = new MultiCondition(null, State.DENY, modifier, parseCondition(name, element, map));
				conditionList.add(multi);
				continue;
			} else if(condition.getName().equalsIgnoreCase("filter")) {
				parseFilter(condition);
			} else if(condition.getName().equalsIgnoreCase("team")) {
				parseTeam(condition, map);
			} else if(condition.getName().equalsIgnoreCase("block")) {
				parseBlock(condition);
			} else if(condition.getName().equalsIgnoreCase("entity")) {
				parseEntity(condition);
			}

			String parents = condition.getParent().attributeValue("parents");
			if(parents != null) {
				String[] split;
				if(parents.contains(" ")) {
					split = parents.split(" ");
				} else {
					split = new String[]{parents};
				}

				for(String parent : split) {
					conditionList.add(new FilterCondition(null, State.ALLOW, parent));
				}
			}

			// TODO: needs spawn condition
		}

		Condition[] cons = new Condition[conditionList.size()];
		for(int i = 0; i < conditionList.size(); i++) {
			cons[i] = conditionList.get(i);
		}

		return new MultiCondition(name, State.DENY, Modifier.ANY, cons);
	}

	public static FilterCondition parseFilter(Element element) {
		String name = element.attributeValue("name");
		return new FilterCondition(null, State.DENY, name);
	}

	public static TeamCondition parseTeam(Element element, SporkMap map) throws InvalidFilterException {
		SporkTeam team = map.getTeam(element.getText());
		if(team == null) {
			throw new InvalidFilterException("'" + element.getText() + "' is not a valid team for TeamCondition");
		}

		return new TeamCondition(null, State.DENY, team);
	}

	public static BlockCondition parseBlock(Element element) throws InvalidFilterException {
		Material material = StringUtil.convertStringToMaterial(element.getText());
		if(material == null) {
			throw new InvalidFilterException("'" + element.getText() + "' is not a valid material for BlockCondition");
		}

		return new BlockCondition(null, State.DENY, material);
	}

	public static EntityCondition parseEntity(Element element) throws InvalidFilterException {
		EntityType type = StringUtil.convertStringToEntityType(element.getText());
		if(type == null) {
			throw new InvalidFilterException("'" + element.getText() + "' is not a valid entity type for EntityCondition");
		}

		return new EntityCondition(null, State.DENY, type);
	}

	private static List<Filter> defaults() {
		List<Filter> defaults = new ArrayList<>();

		AccessCondition allowPlayers = new AccessCondition("allow-players", State.DENY, null);
		defaults.add(allowPlayers);
		MultiCondition denyPlayers = new MultiCondition("deny-players", State.DENY, Modifier.NOT, allowPlayers);
		defaults.add(denyPlayers);

		WorldCondition allowBlocks = new WorldCondition("allow-blocks", State.DENY);
		defaults.add(allowBlocks);
		MultiCondition denyBlocks = new MultiCondition("deny-blocks", State.DENY, Modifier.NOT, allowBlocks);
		defaults.add(denyBlocks);

		BlockCondition allowWorld = new BlockCondition("allow-world", State.DENY, null);
		defaults.add(allowWorld);
		MultiCondition denyWorld = new MultiCondition("deny-world", State.DENY, Modifier.NOT, allowWorld);
		defaults.add(denyWorld);

		EntityCondition allowEntities = new EntityCondition("allow-entities", State.DENY, null);
		defaults.add(allowEntities);
		MultiCondition denyEntities = new MultiCondition("deny-entities", State.DENY, Modifier.NOT, allowEntities);
		defaults.add(denyEntities);

		/*
		EntityCondition allowMobs = new EntityCondition("allow-entities", State.DENY, EntityType);
		defaults.add(allowMobs);
		MultiCondition denyMobs = new MultiCondition("deny-entities", State.ALLOW, Modifier.NOT, allowEntities);
		defaults.add(denyMobs);
		*/

		return defaults;
	}

}
