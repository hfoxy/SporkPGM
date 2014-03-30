package io.sporkpgm.map.debug;

import io.sporkpgm.region.Region;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.util.NMSUtil;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.lang.reflect.Method;

public class VisibleRegion {

	Region region;
	Material material;
	short damage;

	DyeColor dye;

	public VisibleRegion(Region region, Material material, short damage) {
		this.region = region;
		this.material = material;
		this.damage = damage;
		this.dye = DyeColor.getByWoolData((byte) damage);
	}

	public void set(World world) {
		for(BlockRegion value : region.getValues()) {
			FastBlock.setBlockFast(value.getLocation(world), material, (byte) damage);
			/*
			Block block = value.getBlock(world);
			block.setType(material);
			block.setData((byte) damage);
			*/
		}
	}

	public Region getRegion() {
		return region;
	}

	public Material getMaterial() {
		return material;
	}

	public short getDamage() {
		return damage;
	}

	public DyeColor getDye() {
		return dye;
	}

	public static class FastBlock {

		public static Class<?> NMS_WORLD = NMSUtil.getClassNMS("World");
		public static Class<?> NMS_CHUNK = NMSUtil.getClassNMS("Chunk");
		public static Class<?> NMS_BLOCK = NMSUtil.getClassNMS("Block");

		public static Class<?> CRAFT_WORLD = NMSUtil.getClassBukkit("CraftWorld");

		public static boolean setBlockFast(Location location, Material type, byte data) {
			return setBlockFast(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), type.getId(), data);
		}

		public static boolean setBlockFast(World world, int x, int y, int z, int blockId, byte data) {
			try {
				return setBlockFastThrows(world, x, y, x, blockId, data);
			} catch(Exception e) {
				return false;
			}
		}

		public static boolean setBlockFastThrows(World world, int x, int y, int z, int blockId, byte data) throws Exception {
			/*
			net.minecraft.server.v1_7_R1.World w = ((org.bukkit.craftbukkit.v1_7_R1.CraftWorld) world).getHandle();
			net.minecraft.server.v1_7_R1.Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
			return chunk.a(x & 0x0f, y, z & 0x0f, net.minecraft.server.v1_7_R1.Block.e(blockId), data);
			*/

			Object craft = CRAFT_WORLD.cast(world);
			Method handle = CRAFT_WORLD.getDeclaredMethod("getHandle");
			handle.setAccessible(true);

			Object nmsWorld = handle.invoke(craft);
			Method getChunk = NMS_WORLD.getDeclaredMethod("getChunkAt", int.class, int.class);
			getChunk.setAccessible(true);

			Method e = NMS_BLOCK.getDeclaredMethod("e", int.class);
			e.setAccessible(true);
			Object block = e.invoke(null, blockId);

			Object nmsChunk = getChunk.invoke(nmsWorld, x >> 4, z >> 4);
			Method a = NMS_CHUNK.getDeclaredMethod("a", int.class, int.class, int.class, NMS_BLOCK, int.class);
			a.setAccessible(true);

			Object set = a.invoke(nmsChunk, x & 0x0f, y, z & 0x0f, block, data);
			return (boolean) set;
		}

		/*
		public static void forceBlockLightLevel(World world, int x, int y, int z, int level) {
			net.minecraft.server.v1_7_R2.World w = ((CraftWorld) world).getHandle();
			w.b(EnumSkyBlock.BLOCK, x, y, z, level);
		}

		public static int getBlockLightEmission(int blockId) {
			return Block.e(blockId).m();
		}

		public static int getBlockLightBlocking(int blockId) {
			return Block.e(blockId).k();
		}

		public static void queueChunkForUpdate(Player player, int cx, int cz) {
			((CraftPlayer) player).getHandle().chunkCoordIntPairQueue.add(new ChunkCoordIntPair(cx, cz));
		}

		public static void recalculateBlockLighting(World world, int x, int y, int z) {
			net.minecraft.server.v1_7_R2.World w = ((CraftWorld) world).getHandle();
			w.t(x, y, z);
		}
		*/
	}

}
