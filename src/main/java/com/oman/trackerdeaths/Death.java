package com.oman.trackerdeaths;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class Death {

	/**
	 * String constant for an empty string.
	 */
	private final String EMPTY = "";
	/**
	 * String constant for a space whitespace character
	 */
	private final String SPACE = " ";
	/**
	 * The Player that was killed
	 */
	private Player victim;
	/**
	 * The Entity that killed the player (if available)
	 */
	private Entity killer;
	/**
	 * The action message for the
	 * death
	 */
	private String action;
	/**
	 * The string that represents
	 * The weapon that was
	 * used to kill the player
	 * <p/>
	 * This can be null if the victim
	 * was killed by no entity
	 */
	private String item;
	/**
	 * The string that tells
	 * where the user was killed
	 * from
	 */
	private String from;
	/**
	 * The string that tells where
	 * the user was killed into
	 * (gravity)
	 */
	private String to;
	/**
	 * The EntityDamageEvent
	 * that was associated with
	 * this death
	 */
	private EntityDamageEvent event;
	/**
	 * Miscellaneous information
	 * Could contain anything,
	 * E.g. contains bow distance records
	 */
	private String misc;

	/**
	 * Initialize the Death
	 *
	 * @param victim The Player who died
	 */
	public Death(Player victim) {
		this.victim = victim;
		this.killer = null;
		this.action = EMPTY;
		this.item = EMPTY;
		this.from = EMPTY;
		this.to = EMPTY;
		this.event = null;
		this.misc = EMPTY;
	}

	/**
	 * Get the victim of the death
	 *
	 * @return the victim
	 */
	public Player getVictim() {
		return victim;
	}

	/**
	 * Get the name of the Victim entity
	 *
	 * @return the name of the victim entity
	 */
	public String getVictimName() {
		if(victim != null) {
			return Util.getEntityString(victim);
		} else {
			return EMPTY;
		}
	}

	/**
	 * Get the killer Entity
	 *
	 * @return the entity that killed the player (nullable)
	 */
	public Entity getKiller() {
		return killer;
	}

	/**
	 * Get the name of the Killer entity
	 *
	 * @return the name of the killer entity
	 */
	public String getKillerName() {
		if(killer != null) {
			return Util.getEntityString(killer);
		} else {
			return EMPTY;
		}
	}

	/**
	 * Get the player credited with the death
	 * This will be null if a player didn't kill
	 * the victim
	 *
	 * @return the credited player
	 */
	public Player getCredited() {
		if(killer instanceof Player) {
			return ((Player) killer);
		} else {
			return null;
		}
	}

	/**
	 * Get the action string
	 *
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Get the weapon used
	 * in the killing
	 *
	 * @return the weapon or item used
	 */
	public String getWeapon() {
		return item;
	}

	/**
	 * Get the from location
	 *
	 * @return the from location
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Get the 'to' location
	 *
	 * @return the to location
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Get the event that caused the death
	 *
	 * @return the player's last injury
	 */
	public EntityDamageEvent getEvent() {
		return event;
	}

	/**
	 * Get the DamageCause of the death
	 *
	 * @return the last injury's DamageCause
	 */
	public EntityDamageEvent.DamageCause getCause() {
		return event.getCause();
	}

	/**
	 * Get the misc info
	 *
	 * @return the miscellaneous info
	 */
	public String getMisc() {
		return misc;
	}

	/**
	 * Set the killer Entity
	 *
	 * @param killer the entity that killed the player (nullable)
	 */
	public void setKiller(Entity killer) {
		this.killer = killer;
	}

	/**
	 * Set the action string
	 *
	 * @param action the action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Set the weapon used
	 * in the killing
	 *
	 * @param item the weapon or item used
	 */
	public void setWeapon(String item) {
		this.item = item;
	}

	/**
	 * Set the from location
	 *
	 * @param from the from location
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Set the 'to' location
	 *
	 * @param to the to location
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Set the event that caused the death
	 *
	 * @return the player's last injury
	 */
	public void setEvent(EntityDamageEvent event) {
		this.event = event;
	}

	/**
	 * Set the miscellaneous information
	 *
	 * @param misc the new misc. information
	 */
	public void setMisc(String misc) {
		this.misc = misc;
	}

	/**
	 * Get the death message
	 * for this death
	 *
	 * @return the death message
	 */
	public String getDeathMessage() {
		return ChatColor.GOLD + getVictimName() + SPACE + ChatColor.GRAY + action + from + to + (killer ==
				null ? EMPTY : " by ") + ChatColor.GOLD + getKillerName() + ChatColor.GRAY + (item == null || item
				.equals(EMPTY) ? EMPTY : "'s " + item) + (misc.equals(EMPTY) || misc == null ? EMPTY : SPACE + misc);
	}

}
