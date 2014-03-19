package io.sporkpgm;

import io.sporkpgm.commands.AdminCommands;
import io.sporkpgm.commands.UserCommands;
import io.sporkpgm.map.MapLoader;
import io.sporkpgm.map.MapManager;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.match.MatchManager;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderAbout;
import io.sporkpgm.rotation.Rotation;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.dom4j.Document;
import org.joda.time.Duration;
import org.joda.time.Instant;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;

public class Spork extends JavaPlugin {

	private static Spork spork;

	private CommandsManager<CommandSender> commands;

	private MapManager mapManager;
	private MatchManager matchManager;

	protected List<Class<? extends Builder>> builders;

	@Override
	public void onEnable() {
		Instant start = Instant.now();
		spork = this;

		Config.init();
		Log.setDebugging(Config.General.DEBUG);

		Log.info("Loading maps...");
		MapLoader loader = new MapLoader();
		File mapDir = new File(Config.Map.DIRECTORY);
		Collection<SporkMap> maps = loader.loadMaps(mapDir);

		if (maps.size() < 1) {
			Log.warning("I need at least 1 (one) map in order to function!");
			Log.warning("Disabling...");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		this.mapManager = new MapManager(maps);

		Log.info("Loading rotation...");
		File rotationFile = new File(this.getDataFolder(), Config.Rotation.ROTATION);
		Rotation rotation = new Rotation(mapManager, rotationFile);
		this.matchManager = new MatchManager(rotation);

		Log.info("Loading commands...");
		this.registerCommands();
		
		Log.info("Successfully enabled in " + new Duration(start, Instant.now()).getMillis() + "ms!");
	}

	private void registerCommands() {
		this.commands = new CommandsManager<CommandSender>() {
			public boolean hasPermission(CommandSender sender, String perm) {
				return sender.hasPermission(perm);
			}
		};
		CommandsManagerRegistration cmr = new CommandsManagerRegistration(this, this.commands);
		cmr.register(UserCommands.class);
		cmr.register(AdminCommands.class);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			this.commands.execute(cmd.getName(), args, sender, sender);
		} catch (CommandPermissionsException e) {
			sender.sendMessage(ChatColor.RED + "You don't have permission.");
		} catch (MissingNestedCommandException e) {
			sender.sendMessage(ChatColor.RED + e.getUsage());
		} catch (CommandUsageException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
			sender.sendMessage(ChatColor.RED + e.getUsage());
		} catch (WrappedCommandException e) {
			if (e.getCause() instanceof NumberFormatException) {
				sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
			} else {
				sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
				e.printStackTrace();
			}
		} catch (CommandException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
		}
		return true;
	}

	public List<Module> getModules(Document document) {
		return getModules(document, builders);
	}

	public List<Module> getModules(Document document, List<Class<? extends Builder>> builders) {
		List<Module> modules = new ArrayList<>();

		for (Class<? extends Builder> clazz : builders) {
			try {
				Constructor<? extends Builder> constructor = clazz.getConstructor(Document.class);
				constructor.setAccessible(true);
				Builder builder = (Builder) constructor.newInstance(document);
				BuilderAbout about = builder.getInfo();

				if (!about.isDocumentable()) {
					constructor = clazz.getConstructor(SporkMap.class);
					builder = (Builder) constructor.newInstance(document);
				}
			} catch (Exception e) {
				Log.warning("Error when loading '" + clazz.getSimpleName() + "' due to " + e.getClass().getSimpleName());
				continue;
			}
		}

		return modules;
	}

	public List<Class<? extends Builder>> getBuilders() {
		return builders;
	}

	public List<Class<? extends Builder>> getBuilders(List<Class<? extends Builder>> builders, boolean documentable) {
		List<Class<? extends Builder>> classes = new ArrayList<>();

		for (Class<? extends Builder> clazz : builders) {
			try {
				if (documentable && Builder.isDocumentable(clazz)) {
					classes.add(clazz);
				} else {
					classes.add(clazz);
				}
			} catch (Exception e) {
				Log.warning("Error when loading '" + clazz.getSimpleName() + "' due to " + e.getClass().getSimpleName());
				continue;
			}
		}

		return classes;
	}

	protected void builders() {
		builders = new ArrayList<>();
	}

	public static Spork get() {
		return spork;
	}

}
