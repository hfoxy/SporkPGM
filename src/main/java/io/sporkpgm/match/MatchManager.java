package io.sporkpgm.match;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.rotation.Rotation;

import java.util.List;

public class MatchManager {

	private List<SporkMap> rotation;

	private int matchId = 0;

	private SporkMap next;
	
	private Match current;

	public MatchManager(Rotation rotation) {
		this.rotation = rotation.getRotation();
	}

	private int index = -1;

	public List<SporkMap> getRotation() {
		return rotation;
	}

	public SporkMap getCurrentMap() {
		return rotation.get(index);
	}

	public SporkMap getNextMap() {
		if (next != null)
			return next;
		int temp = index + 1;
		if (temp >= rotation.size())
			temp = 0;
		return rotation.get(temp);
	}

	public void increment() {
		if (next != null) {
			next = null;
			return;
		}
		index++;
		if (index >= rotation.size())
			index = 0;
	}

	public SporkMap getNextAndIncrement() {
		SporkMap map = getNextMap();
		increment();
		return map;
	}

	public SporkMap getPrevious() {
		int temp = index - 1;
		if (temp < 0)
			temp = rotation.size() - 1;
		return rotation.get(temp);
	}

	public void setNext(SporkMap map) {
		this.next = map;
	}
	
	public Match getCurrentMatch() {
		return current;
	}

}
