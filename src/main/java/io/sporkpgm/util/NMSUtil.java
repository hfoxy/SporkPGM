package io.sporkpgm.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSUtil {

	public static String getPackageName() {
		return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", "," + "" + "").split(",")[3];
	}

	public static Class<?> getClass(String clazz) {
		String pack = getPackageName() + (clazz.startsWith(".") ? clazz : "." + clazz);
		Log.info("Package: " + pack);
		try {
			return Class.forName(pack);
		} catch(ClassNotFoundException ex) {
			return null;
		}
	}

	public static Object getCraftPlayer(Player player) {
		try {
			return player.getClass().getMethod("getHandle").invoke(player);
		} catch(Exception ex) {
			return null;
		}
	}
}
