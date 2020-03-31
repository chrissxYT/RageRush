package de.chrissx.ragerush.server;

import org.bukkit.Location;

public class LocationTuple {

	Location l1, l2;

	public LocationTuple(Location l1, Location l2) {
		this.l1 = l1;
		this.l2 = l2;
	}

	public LocationTuple() {
		l1 = l2 = null;
	}

	public void setLocation(boolean id, Location l) {
		if(id)
			l2 = l;
		else
			l1 = l;
	}

	public Location getLocation(boolean id) {
		return id ? l2 : l1;
	}
	
	public static String location2jsonBlock(Location l) {
		if(l == null) return "NULL";
		return "{" + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "}";
	}

	@Override
	public String toString() {
		return "{"+ location2jsonBlock(l1) + "," + location2jsonBlock(l2) +"}";
	}
	
	public boolean isValid() {
		return l1 != null && l2 != null;
	}
}
