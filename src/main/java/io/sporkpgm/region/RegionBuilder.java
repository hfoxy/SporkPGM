package io.sporkpgm.region;

import com.google.common.collect.Lists;
import io.sporkpgm.filter.Filter;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.region.FilteredRegion.AppliedValue;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.region.types.CuboidRegion;
import io.sporkpgm.region.types.CylinderRegion;
import io.sporkpgm.region.types.SphereRegion;
import io.sporkpgm.region.types.groups.ComplementRegion;
import io.sporkpgm.region.types.groups.IntersectRegion;
import io.sporkpgm.region.types.groups.NegativeRegion;
import io.sporkpgm.region.types.groups.UnionRegion;
import io.sporkpgm.team.spawns.kits.SporkKit;
import io.sporkpgm.util.XMLUtil;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RegionBuilder {

	public static Region parseRegion(Element ele) throws InvalidRegionException {
		String type = ele.getName();

		if(type.equalsIgnoreCase("block") || type.equalsIgnoreCase("point")) {
			return parseBlock(ele);
		} else if(type.equalsIgnoreCase("rectange")) {
			return parseRectange(ele);
		} else if(type.equalsIgnoreCase("cuboid")) {
			return parseCuboid(ele);
		} else if(type.equalsIgnoreCase("circle")) {
			return parseCircle(ele);
		} else if(type.equalsIgnoreCase("cylinder")) {
			return parseCylinder(ele);
		} else if(type.equalsIgnoreCase("sphere")) {
			return parseSphere(ele);
		} else if(type.equalsIgnoreCase("negative")) {
			return parseNegative(ele);
		} else if(type.equalsIgnoreCase("union")) {
			return parseUnion(ele);
		} else if(type.equalsIgnoreCase("complement")) {
			return parseComplement(ele);
		} else if(type.equalsIgnoreCase("intersect")) {
			return parseIntersect(ele);
		} else if(type.equalsIgnoreCase("region")) {
			String name = ele.attributeValue("name");
			return new SearchRegion(null, name);
		} /* else if(type.equalsIgnoreCase("apply")) {
			return parseFiltered(ele); // can't parse filtered regions here because they require Filters
		} */

		return null;
	}

	public static List<Region> parseSubRegions(Element ele) throws InvalidRegionException {
		List<Region> regions = Lists.newArrayList();

		for(Element element : XMLUtil.getElements(ele)) {
			Region region = parseRegion(element);
			if(region != null) {
				regions.add(region);
			}
		}
		return regions;
	}

	public static BlockRegion parseBlock(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		if(ele.getText() == null) {
			throw new InvalidRegionException(ele, "BlockRegions must have an X, a Y and a Z in the Element text");
		}

		String[] split = ele.getText().split(",");
		if(split.length != 3) {
			throw new InvalidRegionException(ele, "BlockRegions must have an X, a Y and a Z");
		}

		String x = split[0];
		String y = split[1];
		String z = split[2];

		if(isUsable(x) && isUsable(y) && isUsable(z)) {
			return new BlockRegion(name, x, y, z);
		}

		return null;
	}

	public static CuboidRegion parseRectange(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		if(ele.attributeValue("min") == null || ele.attributeValue("max") == null) {
			throw new InvalidRegionException(ele, "Both the minimum and the maximum values can't be null");
		}

		String[] minS = ele.attributeValue("min").split(",");
		String[] maxS = ele.attributeValue("max").split(",");
		if(minS.length != 2 || maxS.length != 2) {
			throw new InvalidRegionException(ele, "Both the minimum and maximum values should have an X and a Y");
		}

		String y = "oo"; // infinite y
		BlockRegion min = new BlockRegion(minS[0], "-" + y, minS[1]);
		BlockRegion max = new BlockRegion(maxS[0], y, maxS[1]);
		return new CuboidRegion(name, min, max);
	}

	public static CuboidRegion parseCuboid(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		List<BlockRegion> blocks = new ArrayList<>();

		String[] values = new String[]{"min", "max"};
		for(String attr : values) {
			Attribute attribute = ele.attribute(attr);
			if(attribute == null) {
				throw new InvalidRegionException(ele, "The " + attr + "imum value can't be null");
			}

			String[] split = attribute.getText().split(",");
			if(split.length != 3) {
				throw new InvalidRegionException(ele, "BlockRegions must have an X, a Y and a Z ('" + attr + "')");
			}

			String x = split[0];
			String y = split[1];
			String z = split[2];

			if(isUsable(x) && isUsable(y) && isUsable(z)) {
				blocks.add(new BlockRegion(name, x, y, z));
			} else {
				throw new InvalidRegionException(ele, "Unsupported X, Y or Z value for '" + attr + "'");
			}
		}

		if(blocks.size() != 2) {
			throw new InvalidRegionException(ele, "CuboidRegions require a minimum and a maximum value");
		}

		return new CuboidRegion(name, blocks.get(0), blocks.get(1));
	}

	public static CylinderRegion parseCircle(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		double radius;
		try {
			radius = Double.parseDouble(ele.attributeValue("radius"));
		} catch(Exception e) {
			throw new InvalidRegionException(ele, "Radius was not a valid double");
		}

		if(ele.attributeValue("center") == null) {
			throw new InvalidRegionException(ele, "The center point of a circle can't be null");
		}

		String[] split = ele.attributeValue("center").split(",");
		if(split.length != 2) {
			throw new InvalidRegionException(ele, "The center point of a circle only accepts X and Z values");
		}

		String x = split[0];
		String y = "oo";
		String z = split[1];
		BlockRegion center = new BlockRegion(x, y, z);

		return new CylinderRegion(name, center, radius, 1, false);
	}

	public static CylinderRegion parseCylinder(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		double radius;
		try {
			radius = Double.parseDouble(ele.attributeValue("radius"));
		} catch(Exception e) {
			throw new InvalidRegionException(ele, "Radius was not a valid double");
		}

		double height;
		try {
			height = Double.parseDouble(ele.attributeValue("height"));
		} catch(Exception e) {
			throw new InvalidRegionException(ele, "Height was not a valid double");
		}

		if(ele.attributeValue("base") == null) {
			throw new InvalidRegionException(ele, "The base point of a cylinder can't be null");
		}

		String[] split = ele.attributeValue("base").split(",");
		if(split.length != 3) {
			throw new InvalidRegionException(ele, "The base point of a cylinder requires X, Y and Z values");
		}

		String x = split[0];
		String y = split[1];
		String z = split[2];
		BlockRegion center = new BlockRegion(x, y, z);

		return new CylinderRegion(name, center, radius, height, false);
	}

	public static SphereRegion parseSphere(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		double radius;
		try {
			radius = Double.parseDouble(ele.attributeValue("radius"));
		} catch(Exception e) {
			throw new InvalidRegionException(ele, "Radius was not a valid double");
		}

		if(ele.attributeValue("center") == null) {
			throw new InvalidRegionException(ele, "The center point of a sphere can't be null");
		}

		String[] split = ele.attributeValue("center").split(",");
		if(split.length != 3) {
			throw new InvalidRegionException(ele, "The center point of a sphere requires X, Y and Z values");
		}

		String x = split[0];
		String y = split[1];
		String z = split[2];
		BlockRegion center = new BlockRegion(x, y, z);

		return new SphereRegion(name, center, radius, false);
	}

	public static NegativeRegion parseNegative(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		return new NegativeRegion(name, parseSubRegions(ele));
	}

	public static UnionRegion parseUnion(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		return new UnionRegion(name, parseSubRegions(ele));
	}

	public static ComplementRegion parseComplement(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		List<Region> regions = parseSubRegions(ele);
		return new ComplementRegion(name, regions.get(0), regions.subList(1, regions.size()));
	}

	public static IntersectRegion parseIntersect(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		return new IntersectRegion(name, parseSubRegions(ele));
	}

	public static FilteredRegion parseFiltered(SporkMap map, Element element) throws InvalidRegionException {
		String name = element.attributeValue("name");
		List<Region> regions = parseSubRegions(element);
		List<Filter> filters = new ArrayList<>();

		Map<AppliedValue, Object> hash = new HashMap<>();
		for(AppliedValue key : AppliedValue.values()) {
			Object value = null;
			if(key.getReturns() == Filter.class) {
				Filter filter = map.getFilter(element.attributeValue(key.getAttribute()));
				value = filter;
				if(!filters.contains(filter)) {
					filters.add(filter);
				}
			} else if(key.getReturns() == SporkKit.class) {
				value = map.getKit(element.attributeValue(key.getAttribute()));
			} else if(key.getReturns() == String.class) {
				value = element.attributeValue(key.getAttribute());
			}

			hash.put(key, value);
		}

		return new FilteredRegion(name, hash, regions, filters);
	}

	public static boolean isUsable(String value) {
		if(value.equals("oo") || value.equals("-oo")) {
			return true;
		}

		try {
			Double.valueOf(value);
			return true;
		} catch(NumberFormatException ignored) {
		}

		return false;
	}

}