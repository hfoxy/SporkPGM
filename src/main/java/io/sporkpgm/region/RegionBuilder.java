package io.sporkpgm.region;

import com.google.common.collect.Lists;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.region.types.CuboidRegion;
import io.sporkpgm.region.types.CylinderRegion;
import io.sporkpgm.region.types.SphereRegion;
import io.sporkpgm.region.types.groups.IntersectRegion;
import io.sporkpgm.region.types.groups.UnionRegion;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.XMLUtil;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.List;

public class RegionBuilder {

	public static Region parseRegion(Element ele) throws InvalidRegionException {
		String type = ele.getName();
		if(type.equalsIgnoreCase("block")) {
			return parseBlock(ele);
		} else if(type.equalsIgnoreCase("cuboid")) {
			return parseCuboid(ele);
		} else if(type.equalsIgnoreCase("cylinder")) {
			return parseCylinder(ele);
		} else if(type.equalsIgnoreCase("sphere")) {
			return parseSphere(ele);
		} else if(type.equalsIgnoreCase("region")) {
			return parseMulti(ele);
		}
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
		if(ele.attributeValue("x") == null || ele.attributeValue("x") == null || ele.attributeValue("x") == null) {
			throw new InvalidRegionException("BlockRegion must have X, Y and Z");
		}

		String x = ele.attributeValue("x");
		String y = ele.attributeValue("y");
		String z = ele.attributeValue("z");

		if(isUsable(x) && isUsable(y) && isUsable(z)) {
			return new BlockRegion(name, x, y, z);
		}

		return null;
	}

	public static CuboidRegion parseCuboid(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		List<Region> sub = parseSubRegions(ele);
		if(sub.size() != 2) {
			throw new InvalidRegionException("CuboidRegions can have only 2 BlockRegions");
		}
		if(!(sub.get(0) instanceof BlockRegion || sub.get(1) instanceof BlockRegion)) {
			throw new InvalidRegionException("CuboidRegions can only be of 2 BlockRegions");
		}
		return new CuboidRegion(name, (BlockRegion) sub.get(0), (BlockRegion) sub.get(1));
	}

	public static CylinderRegion parseCylinder(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		List<Region> sub = parseSubRegions(ele);
		if(sub.size() != 1) {
			throw new InvalidRegionException("CylinderRegions can have only 1 BlockRegion");
		}
		if(!(sub.get(0) instanceof BlockRegion)) {
			throw new InvalidRegionException("CylinderRegions can only be of 1 BlockRegion");
		}

		BlockRegion centre = (BlockRegion) sub.get(0);
		boolean hollow = XMLUtil.parseBoolean(ele.attributeValue("hollow"), false);
		double radius = 0;
		double height = 0;
		try {
			radius = XMLUtil.parseDouble(ele.attributeValue("radius"));
		} catch(NumberFormatException e) {
			throw new InvalidRegionException("CylinderRegions must have a suitable radius attribute");
		}

		if(!centre.isYInfinite()) {
			try {
				height = XMLUtil.parseDouble(ele.attributeValue("height"));
			} catch(NumberFormatException nfe1) {
				try {
					height = XMLUtil.parseInteger(ele.attributeValue("height"));
				} catch(NumberFormatException nfe2) {
					nfe2.printStackTrace();

					Log.info(ele.asXML());
					@SuppressWarnings("unchecked")
					List<Attribute> attributes = (List<Attribute>) ele.attributes();
					for(Attribute attr : attributes)
						Log.info(attr.getName() + ": " + attr.getText());

					throw new InvalidRegionException("CylinderRegions must have a suitable height attribute or the block must have an infinite Y value");
				}
			}
		}

		return new CylinderRegion(name, centre, radius, height, hollow);
	}

	public static SphereRegion parseSphere(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		List<Region> sub = parseSubRegions(ele);
		if(sub.size() != 1) {
			throw new InvalidRegionException("CylinderRegions can have only 1 BlockRegion");
		}
		if(!(sub.get(0) instanceof BlockRegion)) {
			throw new InvalidRegionException("CylinderRegions can only be of 1 BlockRegion");
		}

		BlockRegion centre = (BlockRegion) sub.get(0);
		boolean hollow = XMLUtil.parseBoolean(ele.attributeValue("hollow"), false);
		double radius = 0;
		try {
			radius = XMLUtil.parseDouble(ele.attributeValue("radius"));
		} catch(NumberFormatException e) {
			throw new InvalidRegionException("CylinderRegions must have a suitable radius attribute");
		}

		return new SphereRegion(name, centre, radius, hollow);
	}

	public static UnionRegion parseMulti(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		return new UnionRegion(name, parseSubRegions(ele));
	}

	public static IntersectRegion parseIntersect(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		return new IntersectRegion(name, parseSubRegions(ele));
	}

	public static IntersectRegion parseNegative(Element ele) throws InvalidRegionException {
		String name = ele.attributeValue("name");
		return new IntersectRegion(name, parseSubRegions(ele));
	}

	public static boolean isUsable(String value) {
		if(value.equals("@") || value.equals("-@")) {
			return true;
		}

		if(value.contains(".")) {
			try {
				Double.valueOf(value);
				return true;
			} catch(NumberFormatException ignored) {
			}
		}

		try {
			Integer.valueOf(value);
			return true;
		} catch(NumberFormatException ignored) {
		}

		return false;
	}

}