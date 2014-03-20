package io.sporkpgm.rotation;

import com.google.common.base.Charsets;
import io.sporkpgm.Spork;
import io.sporkpgm.map.MapBuilder;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.match.Match;
import io.sporkpgm.match.MatchPhase;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.rotation.exceptions.RotationLoadException;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Rotation {

	boolean started;
	boolean cancel;

	int id = 0;
	int list = 0;
	int slot = 0;
	int current = 0;
	List<List<RotationSlot>> slots;

	Rotation(List<RotationSlot> slots) {
		this.slots = new ArrayList<>();
		this.id = 0;
		for(int i = 0; i < 5; i++) {
			List<RotationSlot> slotSet = new ArrayList<>();
			for(RotationSlot slot : slots) {
				id++;
				slotSet.add(new RotationSlot(slot.getLoader(), id));
			}
			this.slots.add(slotSet);
		}
	}

	public static Rotation provide() throws RotationLoadException, IOException {
		File rotation = Config.Rotation.ROTATION;
		if(rotation.isDirectory()) {
			throw new RotationLoadException("Unable to parse '" + rotation.getPath() + "' because it is a directory.");
		}

		if(!rotation.exists()) {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rotation), "UTF8"));
			for(MapBuilder loader : Spork.getMaps()) {
				Log.info("Printing out " + loader.getName() + " into the Rotation file");
				out.write(loader.getName());
			}

			out.close();
		}

		List<MapBuilder> loaders = new ArrayList<>();
		for(String rawLine : Files.readAllLines(rotation.toPath(), Charsets.UTF_8)) {
			if(MapBuilder.getLoader(rawLine) == null) {
				Log.warning("Failed to find a map for '" + rawLine + "' in the rotation file");
				continue;
			}

			loaders.add(MapBuilder.getLoader(rawLine));
		}

		int id = 0;
		List<RotationSlot> slots = new ArrayList<>();
		for(MapBuilder builder : loaders) {
			slots.add(new RotationSlot(builder, id));
			id++;
		}

		return new Rotation(slots);
	}

	public void start() throws ModuleLoadException, RotationLoadException {
		Match match = getCurrentSlot().load();
		this.started = true;
		match.start();
	}

	public void stop() {
		for(SporkPlayer player : SporkPlayer.getPlayers()) {
			player.getPlayer().kickPlayer("Server Restarting!");
		}

		getCurrentSlot().getMatch().stop();
	}

	public List<RotationSlot> getPreviousList() {
		return slots.get(list - 1);
	}

	public List<RotationSlot> getCurrentList() {
		return slots.get(list);
	}

	public List<RotationSlot> getNextList() {
		return slots.get(list + 1);
	}

	public RotationSlot getPreviousSlot() {
		try {
			return getCurrentList().get(slot - 1);
		} catch(IndexOutOfBoundsException e) {
			if(list == 0)
				return null;
			List<RotationSlot> slots = getPreviousList();
			return slots.get(slots.size() - 1);
		}
	}

	public Match getPreviousMatch() {
		return getPreviousSlot().getMatch();
	}

	public SporkMap getPrevious() {
		return getPreviousSlot().getMap();
	}

	public RotationSlot getCurrentSlot() {
		return getCurrentList().get(slot);
	}

	public Match getCurrentMatch() {
		return getCurrentSlot().getMatch();
	}

	public SporkMap getCurrent() {
		return getCurrentSlot().getMap();
	}

	public RotationSlot getNextSlot() {
		try {
			return getCurrentList().get(slot + 1);
		} catch(IndexOutOfBoundsException e) {
			if(list == (slots.size() - 1))
				return null;
			List<RotationSlot> slots = getNextList();
			return slots.get(0);
		}
	}

	public Match getNextMatch() {
		return getNextSlot().getMatch();
	}

	public SporkMap getNext() {
		return getNextSlot().getMap();
	}

	public List<MapBuilder> getMaps() {
		List<MapBuilder> maps = new ArrayList<>();

		for(List<RotationSlot> slots : this.slots)
			for(RotationSlot slot : slots)
				if(!maps.contains(slot.getLoader()))
					maps.add(slot.getLoader());

		return maps;
	}

	public int getMatchId() {
		return current;
	}

	public void cycle() {
		current++;

		try {
			slot++;
			getCurrentList().get(slot);
		} catch(IndexOutOfBoundsException e) {
			if(list == (slots.size() - 1))
				return;
			slot = 0;
			list++;
		}

		SporkTeam obs = getCurrent().getObservers();
		for(SporkPlayer player : SporkPlayer.getPlayers()) {
			player.setTeam(obs, false, true, true);
		}

		getPrevious().unload(getPreviousMatch());
	}

	public void setNext(MapBuilder map) {
		if(getCurrentMatch().getPhase() == MatchPhase.CYCLING) {
			getNextMatch().getMap().unload(getNextMatch());
		}

		id++;
		List<RotationSlot> list = getCurrentList().subList(0, current);
		list.add(new RotationSlot(map, id));
		if(getNext() != null) {
			list.addAll(getCurrentList().subList(current + 1, getCurrentList().size() - 1));
		}
	}

}
