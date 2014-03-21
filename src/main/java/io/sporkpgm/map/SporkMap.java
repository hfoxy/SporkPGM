package io.sporkpgm.map;

import io.sporkpgm.Spork;
import io.sporkpgm.map.generator.NullChunkGenerator;
import io.sporkpgm.match.Match;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.module.extras.InitModule;
import io.sporkpgm.module.extras.TaskedModule;
import io.sporkpgm.module.modules.info.Contributor;
import io.sporkpgm.module.modules.info.InfoModule;
import io.sporkpgm.module.modules.timer.TimerBuilder;
import io.sporkpgm.module.modules.timer.TimerModule;
import io.sporkpgm.objective.ObjectiveModule;
import io.sporkpgm.objective.scored.ScoredObjective;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.region.Region;
import io.sporkpgm.rotation.RotationSlot;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.team.SporkTeamBuilder;
import io.sporkpgm.team.spawns.SporkSpawn;
import io.sporkpgm.team.spawns.kits.SporkKit;
import io.sporkpgm.util.FileUtil;
import io.sporkpgm.util.Log;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.dom4j.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SporkMap {

	protected MapBuilder builder;
	protected Document document;
	protected File folder;

	protected TimerModule timer;
	protected List<Module> modules;

	protected SporkTeam observers;
	protected List<SporkTeam> teams;
	protected List<SporkSpawn> spawns;
	protected List<SporkKit> kits;
	protected List<Region> regions;

	protected World world;
	protected Scoreboard scoreboard;
	protected Objective objective;
	protected SporkTeam winner;
	protected boolean ended;

	public SporkMap(MapBuilder builder) throws ModuleLoadException {
		this.builder = builder;
		this.document = builder.getDocument();
		this.folder = builder.getFolder();

		this.scoreboard = Spork.get().getServer().getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective("Objectives", "dummy");
		this.teams = SporkTeamBuilder.build(this);
		this.observers = SporkTeamBuilder.observers(this);
		// this.kits = SporkKitBuilder.build(this);
		this.modules = new ArrayList<>();
		// this.spawns = SporkSpawnBuilder.build(this);
		this.timer = (TimerModule) new TimerBuilder(this).build().get(0);
	}

	public boolean load(Match match) {
		String name = "match-" + match.getID();
		File copyto = new File(name);
		try {
			FileUtil.copy(getFolder(), copyto);
			File sessionLock = new File(copyto.getAbsolutePath() + "/session.lock");
			if(sessionLock.exists()) {
				FileUtil.delete(sessionLock);
			}
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}

		WorldCreator creator = new WorldCreator(name);
		creator.generator(new NullChunkGenerator());
		this.world = creator.createWorld();

		loadModules();
		this.timer.setMatch(match);

		scoreboard();

		return true;
	}

	public boolean hasModule(Class<? extends Module> type) {
		for(Module module : modules)
			if(module.getClass() == type)
				return true;

		return false;
	}

	public List<ObjectiveModule> getObjectives() {
		List<ObjectiveModule> objectives = new ArrayList<>();

		for(Module module : modules) {
			if(module instanceof ObjectiveModule) {
				objectives.add((ObjectiveModule) module);
			}
		}

		return objectives;
	}

	public void scoreboard() {
		this.objective.setDisplayName(ChatColor.GOLD + "Objectives");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		int score = 1;
		for(SporkTeam team : getTeams()) {
			if(hasModule(ScoredObjective.class)) {
				for(ObjectiveModule objective : team.getObjectives()) {
					if(objective.getClass() == ScoredObjective.class) {
						ScoredObjective scored = (ScoredObjective) objective;
						scored.setScore(1, 0);
					}
				}
			} else {
				for(ObjectiveModule objective : team.getObjectives()) {
					this.objective.getScore(objective.getPlayer()).setScore(score);
					score++;
				}
				objective.getScore(team.getPlayer()).setScore(score);
				score++;
			}
		}
	}

	public void start() {
		for(Module module : modules) {
			if(module.getInfo().isListener()) {
				Spork.registerListener(module);
			}

			if(module instanceof TaskedModule) {
				((TaskedModule) module).setTasks(true);
			}

			if(module instanceof InitModule) {
				((InitModule) module).start();
			}
		}
	}

	public boolean unload(Match match) {
		String name = "match-" + match.getID();
		File delete = new File(name);

		World world = Spork.get().getServer().getWorld(name);
		Spork.get().getServer().unloadWorld(world, false);
		FileUtil.delete(delete);

		return true;
	}

	public void stop() {
		for(Module module : modules) {
			if(module.getInfo().isListener()) {
				Spork.unregisterListener(module);
			}

			if(module instanceof TaskedModule) {
				((TaskedModule) module).setTasks(false);
			}

			if(module instanceof InitModule) {
				((InitModule) module).stop();
			}
		}
	}

	public void loadModules() {
		modules.addAll(Spork.get().getModules(this));

		List<Module> remove = new ArrayList<>();
		for(Module module : modules) {
			if(module.getInfo().getRequires().size() > 0) {
				boolean ignore = false;
				for(Class<? extends Module> type : module.getInfo().getRequires()) {
					if(!remove.contains(module) && !ignore && !hasModule(type)) {
						remove.add(module);
						Log.warning("Removing " + module.getInfo().getName() + " because " + type.getName() + " was missing!");
					}
				}
			}
		}

		for(Module toRemove : remove) {
			modules.remove(toRemove);
		}
	}

	public Document getDocument() {
		return document;
	}

	public File getFolder() {
		return folder;
	}

	public InfoModule getInfo() {
		return builder.getInfo();
	}

	public String getName() {
		return getInfo().getName();
	}

	public String getVersion() {
		return getInfo().getVersion();
	}

	public List<Contributor> getAuthors() {
		return getInfo().getAuthors();
	}

	public List<String> getAuthorNames() {
		List<String> names = new ArrayList<>();
		for(Contributor author : getAuthors()) {
			names.add(author.getUsername());
		}

		return names;
	}

	public List<Contributor> getContributors() {
		return getInfo().getContributors();
	}

	public SporkTeam getObservers() {
		return observers;
	}

	public TimerModule getTimer() {
		return timer;
	}

	public World getWorld() {
		return world;
	}

	public Objective getObjective() {
		return objective;
	}

	public List<SporkTeam> getTeams() {
		return teams;
	}

	public List<SporkTeam> getTeams(String string) {
		List<SporkTeam> test = new ArrayList<>();
		test.addAll(getTeams());
		test.add(getObservers());
		List<String> names = new ArrayList<>();
		for(SporkTeam team : getTeams())
			names.add(team.getName());

		List<SporkTeam> teams = new ArrayList<>();
		for(SporkTeam team : test) {
			if(!teams.contains(team)) {
				String name = team.getName().toLowerCase();
				String colour = team.getColor().name().replace("_", " ").toLowerCase();
				if(name.equalsIgnoreCase(string) || colour.equalsIgnoreCase(string))
					teams.add(team);
			}
		}

		for(SporkTeam team : test) {
			if(!teams.contains(team)) {
				String name = team.getName().toLowerCase();
				String colour = team.getColor().name().replace("_", " ").toLowerCase();
				if(name.startsWith(string) || colour.startsWith(string))
					teams.add(team);
			}
		}

		return teams;
	}

	public List<SporkKit> getKits() {
		return kits;
	}

	public SporkTeam getTeam(String string) {
		List<SporkTeam> teams = getTeams(string);
		if(teams.size() == 0)
			return null;
		return teams.get(0);
	}

	public List<Region> getRegions() {
		return regions;
	}

	public Region getRegion(String string) {
		List<String> names = new ArrayList<>();
		for(Region region : getRegions())
			if(region.getName() != null)
				names.add(region.getName());

		for(Region region : regions) {
			String name = region.getName() != null ? region.getName() : "null";
			if(region.getName() != null && name.equalsIgnoreCase(string))
				return region;
		}

		return null;
	}

	public List<SporkPlayer> getPlayers() {
		List<SporkPlayer> players = new ArrayList<>();

		for(SporkTeam team : teams)
			players.addAll(team.getPlayers());

		return players;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public List<SporkSpawn> getSpawns() {
		return spawns;
	}

	public SporkSpawn getSpawn(String name) {
		for(SporkSpawn spawn : getSpawns()) {
			if(spawn.getName().equalsIgnoreCase(name)) {
				return spawn;
			}
		}

		return null;
	}

	public List<Module> getModules() {
		return modules;
	}

	public List<Module> getModules(Class<? extends Module> clazz) {
		List<Module> modules = new ArrayList<>();

		for(Module module : this.modules) {
			if(module.getClass() == clazz) {
				modules.add(module);
			}
		}

		return modules;
	}

	public Module getModule(Class<? extends Module> clazz) {
		return getModules(clazz).get(0);
	}

	public void checkEnded() {
		if(!ended && getWinner() != null && !getWinner().isObservers()) {
			setEnded(true);
		}
	}

	public void setEnded(boolean ended) {
		this.ended = ended;

		if(this.winner == null) {
			this.winner = getObservers();
		}
	}

	public boolean hasEnded() {
		return ended;
	}

	public SporkTeam getWinner() {
		if(winner != null) {
			return winner;
		}

		for(SporkTeam team : getTeams()) {
			if(team.complete()) {
				this.winner = team;
				return team;
			}
		}

		return null;
	}

	public static SporkMap getMap() {
		return RotationSlot.getRotation().getCurrent();
	}

}