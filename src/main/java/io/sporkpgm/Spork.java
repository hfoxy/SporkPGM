package io.sporkpgm;

import io.sporkpgm.commands.MapCommands;
import io.sporkpgm.commands.MatchCommands;
import io.sporkpgm.commands.RotationCommands;
import io.sporkpgm.listeners.BlockListener;
import io.sporkpgm.listeners.ConnectionListener;
import io.sporkpgm.listeners.EntityListener;
import io.sporkpgm.listeners.MapListener;
import io.sporkpgm.listeners.PlayerListener;
import io.sporkpgm.map.MapBuilder;
import io.sporkpgm.map.MapLoader;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.match.Match;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderAbout;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.rotation.Rotation;
import io.sporkpgm.rotation.exceptions.RotationLoadException;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Config.Map;
import io.sporkpgm.util.Log;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dom4j.Document;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;

import static io.sporkpgm.Spork.StartupType.*;

public class Spork extends JavaPlugin {

	protected static Spork spork;

	protected CommandsManager<CommandSender> commands;
	protected List<Class<? extends Builder>> builders;
	protected List<MapBuilder> maps;
	protected File repository;

	protected Rotation rotation;

	@Override
	public void onEnable() {
		spork = this;
		saveDefaultConfig();

		Config.init();
		Log.setDebugging(Config.General.DEBUG);
		if(getConfig().getString("settings.maps.repository") == null) {
			getConfig().set("settings.maps.repository", "maps/");
		}

		builders();
		repository = Map.DIRECTORY;
		maps = new ArrayList<>();

		StartupType type = Map.STARTUP;

		List<MapBuilder> all = MapLoader.build(repository);
		List<MapBuilder> load = new ArrayList<>();
		if(type == ALL) {
			load.addAll(all);
		} else if(type == SPECIFIED) {
			load.clear();
			List<String> specified = getConfig().getStringList("settings.maps.load");
			if(specified != null) {
				for(MapBuilder map : all) {
					if(contains(specified, "map name")) {
						load.add(map);
					}
				}
			}
		}

		if(load.isEmpty() && type != SPECIFIED) {
			Log.warning("No maps were available from the specified list - loading all maps");
			load = new ArrayList<>();
			load.addAll(all);
		}

		this.maps = load;

		if(load.isEmpty()) {
			Log.severe("Unable to find any maps inside the repository! Disabling plugin...");
			setEnabled(false);
			return;
		}

		boolean failed = false;
		String reason = "";
		try {
			this.rotation = Rotation.provide();
		} catch(RotationLoadException rle) {
			Log.severe(rle.getClass().getSimpleName() + ": " + rle.getMessage());
			Log.severe("Could not create a suitable Rotation! Disabling plugin...");
			setEnabled(false);
			return;
		} catch(IOException ioe) {
			Log.severe(ioe.getClass().getSimpleName() + ": " + ioe.getMessage());
			failed = true;
			reason = "Unable to save new Rotation";
		}

		registerCommands();
		registerListeners();

		Log.info(rotation.toString());

		failed = true;
		try {
			rotation.start();
			failed = false;
		} catch(RotationLoadException e) {
			Log.severe(e.getMessage());
			reason = e.getMessage();
		} catch(ModuleLoadException e) {
			Log.severe(e.getMessage());
			reason = e.getMessage();
		} catch(InvalidRegionException e) {
			Log.severe(e.getMessage());
			reason = e.getMessage();
		}

		if(failed) {
			Log.severe(reason + "! Disabling plugin...");
			setEnabled(false);
			return;
		}
	}

	private void registerCommands() {
		this.commands = new CommandsManager<CommandSender>() {
			public boolean hasPermission(CommandSender sender, String perm) {
				return sender.hasPermission(perm);
			}
		};
		CommandsManagerRegistration cmr = new CommandsManagerRegistration(this, this.commands);

		this.commands.register(MapCommands.class);
		this.commands.register(MatchCommands.class);
		this.commands.register(RotationCommands.class);
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

	private boolean contains(List<String> strings, String search) {
		for(String string : strings) {
			if(string.equalsIgnoreCase(search)) {
				return true;
			}
		}

		return false;
	}

	public List<Module> getModules(Document document) {
		return getModules(document, builders);
	}

	public List<Module> getModules(SporkMap map) {
		return getModules(map, builders);
	}

	public List<Module> getModules(Document document, List<Class<? extends Builder>> builders) {
		List<Module> modules = new ArrayList<>();

		for(Class<? extends Builder> clazz : builders) {
			try {
				Constructor constructor = clazz.getConstructor(Document.class);
				constructor.setAccessible(true);
				Builder builder = (Builder) constructor.newInstance(document);
				modules.addAll(builder.build());
			} catch(Exception e) {
				getLogger().warning("Error when loading '" + clazz.getSimpleName() + "' due to " + e.getClass().getSimpleName());
			}
		}

		return modules;
	}

	public List<Module> getModules(SporkMap map, List<Class<? extends Builder>> builders) {
		List<Module> modules = new ArrayList<>();

		for(Class<? extends Builder> clazz : builders) {
			try {
				Constructor constructor = clazz.getConstructor(SporkMap.class, Document.class);
				constructor.setAccessible(true);
				Builder builder = (Builder) constructor.newInstance(map, map.getDocument());
				modules.addAll(builder.build());
			} catch(Exception e) {
				getLogger().warning("Error when loading '" + clazz.getSimpleName() + "' due to " + e.getClass().getSimpleName());
			}
		}

		return modules;
	}

	public boolean isDocumentable(Class<? extends Builder> clazz) {
		return new BuilderAbout(clazz).isDocumentable();
	}

	public List<Class<? extends Builder>> getBuilders() {
		return builders;
	}

	protected void builders() {
		builders = new ArrayList<>();
	}

	public Rotation getRotation() {
		return rotation;
	}

	public static Spork get() {
		return spork;
	}

	public Match getMatch() {
		return rotation.getCurrentMatch();
	}

	public static List<MapBuilder> getMaps() {
		return get().maps;
	}

	public MapBuilder getMap(String name) {
		for(MapBuilder builder : getMaps()) {
			if(builder.getName().equalsIgnoreCase(name)) {
				return builder;
			}
		}

		for(MapBuilder builder : getMaps()) {
			if(builder.getName().toLowerCase().startsWith(name.toLowerCase())) {
				return builder;
			}
		}

		return null;
	}

	private void registerListeners() {
		Log.info("Loading Listeners...");
		registerListener(new ConnectionListener());
		registerListener(new BlockListener());
		registerListener(new EntityListener());
		registerListener(new MapListener());
		registerListener(new PlayerListener());
	}

	public static void registerListeners(Listener... listeners) {
		for(Listener listener : listeners) {
			registerListener(listener);
		}
	}

	public static void registerListener(Listener listener) {
		get().getServer().getPluginManager().registerEvents(listener, get());
	}

	public static void unregisterListeners(Listener... listeners) {
		for(Listener listener : listeners) {
			unregisterListener(listener);
		}
	}

	public static void unregisterListener(Listener listener) {
		HandlerList.unregisterAll(listener);
	}

	public static void callEvents(Event... events) {
		for(Event event : events) {
			get().getServer().getPluginManager().callEvent(event);
		}
	}

	public static void callEvent(Event event) {
		get().getServer().getPluginManager().callEvent(event);
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean hasPlugin(String name) {
		return get().getServer().getPluginManager().getPlugin(name) != null;
	}

	public static Plugin getPlugin(String name) {
		if(!hasPlugin(name)) {
			return null;
		}
		return get().getServer().getPluginManager().getPlugin(name);
	}

	public enum StartupType {

		ALL(new String[]{"all"}),
		SPECIFIED(new String[]{"specified", "specify", "listed"});

		String[] names;

		StartupType(String[] names) {
			this.names = names;
		}

		public String[] getNames() {
			return names;
		}

		public boolean matches(String name) {
			for(String value : names) {
				if(value.equalsIgnoreCase(name)) {
					return true;
				}
			}

			return false;
		}

		public static StartupType getType(String name) {
			if(name != null) {
				for(StartupType type : values()) {
					if(type.matches(name)) {
						return type;
					}
				}
			}

			return ALL;
		}

	}

}
