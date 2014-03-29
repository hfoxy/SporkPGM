package io.sporkpgm.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import io.sporkpgm.Spork;
import io.sporkpgm.map.MapBuilder;
import io.sporkpgm.rotation.RotationSlot;
import io.sporkpgm.util.PaginatedResult;
import io.sporkpgm.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MapCommands {

	public static void sendInfo(CommandSender sender, MapBuilder loader) {
		String bar = ChatColor.RED + "" + ChatColor.STRIKETHROUGH + " ----------- ";
		sender.sendMessage(bar + ChatColor.GOLD + loader.getName() + bar);
		sender.sendMessage(ChatColor.DARK_AQUA + "Version: " + loader.getVersion());
		sender.sendMessage(ChatColor.DARK_AQUA + "Authors: " + StringUtil.listToEnglishCompound(
				loader.getAuthorNames(), ChatColor.RED.toString(), ChatColor.DARK_PURPLE.toString()));
		sender.sendMessage(ChatColor.DARK_AQUA + "Contributors: " + StringUtil.listToEnglishCompound(
				loader.getAuthorNames(), ChatColor.RED.toString(), ChatColor.DARK_PURPLE.toString()));
	}

	@Command(aliases = {"map", "mapinfo"}, desc = "View information on the current map or map supplied", usage = "[map name]", max = 1)
	public static void map(CommandContext cmd, CommandSender sender) throws CommandException {
		if(cmd.argsLength() == 0) {
			MapBuilder currentmap = RotationSlot.getRotation().getCurrent().getBuilder();
			sendInfo(sender, currentmap);
		} else {
			MapBuilder result = Spork.get().getMap(cmd.getJoinedStrings(0));
			if(result == null)
				throw new CommandException("No map matched query!");
			sendInfo(sender, result);
		}
	}

	@Command(aliases = {"maps", "maplist"}, desc = "View all the currently loaded maps", usage = "[page]", max = 1)
	public static void maps(CommandContext cmd, CommandSender sender) throws CommandException {
		String bar = ChatColor.RED + "" + ChatColor.STRIKETHROUGH + " ----------- ";
		String loaded = ChatColor.DARK_AQUA + "Loaded Maps (" + ChatColor.AQUA + "[page]" + ChatColor.DARK_AQUA + " " +
				"of" + " " + ChatColor.AQUA + "[pages]" + ChatColor.DARK_AQUA + ")";
		String header = bar + loaded + bar;
		List<String> rows = new ArrayList<>();
		for(MapBuilder loader : Spork.get().getMaps()) {
			rows.add(ChatColor.GOLD + loader.getName() + " " + ChatColor.DARK_PURPLE + "by " + StringUtil
					.listToEnglishCompound(loader.getAuthorNames(), ChatColor.RED.toString(), ChatColor.DARK_PURPLE
							.toString()));
		}

		int results = 8;

		PaginatedResult result = new PaginatedResult(header, rows, results, true);
		result.display(sender, cmd.getInteger(0, 1));
	}

}
