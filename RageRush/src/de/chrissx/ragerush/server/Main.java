package de.chrissx.ragerush.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor, Listener {

	ArrayList<CommandHandler> commandHandlers = new ArrayList<CommandHandler>();
	HashMap<UUID, LocationTuple> locs = new HashMap<UUID, LocationTuple>();

	public void onEnable() {
		getLogger().log(Level.INFO, "RageRush started.");
		
		
		commandHandlers.add(new CommandHandler("rr") {
			@Override
			public boolean handleCommand(CommandSender s, String[] args) {
				s.sendMessage("hi");
				if(s instanceof Player)
					s.sendMessage(locs.get(((Player)s).getUniqueId()).toString());
				return true;
			}
		});


		commandHandlers.add(new CommandHandler("pos1") {
			@Override
			public boolean handleCommand(CommandSender s, String[] args) {
				return setLoc(false, s);
			}
		});


		commandHandlers.add(new CommandHandler("pos2") {
			@Override
			public boolean handleCommand(CommandSender s, String[] args) {
				return setLoc(true, s);
			}
		});


		commandHandlers.add(new CommandHandler("set") {
			@Override
			public boolean handleCommand(CommandSender s, String[] args) {
				if(s instanceof Player && !((Player)s).isOp()) return false;
				Player p = (Player)s;
				LocationTuple lt = locs.get(p.getUniqueId());
				if(lt == null || !lt.isValid()) return false;
				Location l1 = lt.l1, l2 = lt.l2;
				if(l1.getWorld() != l2.getWorld()) return false;
				World w = lt.l1.getWorld();
		        
		        int topBlockX = (l1.getBlockX() < l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());
		        int bottomBlockX = (l1.getBlockX() > l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());
		 
		        int topBlockY = (l1.getBlockY() < l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());
		        int bottomBlockY = (l1.getBlockY() > l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());
		 
		        int topBlockZ = (l1.getBlockZ() < l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());
		        int bottomBlockZ = (l1.getBlockZ() > l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());
		 
		        for(int x = bottomBlockX; x <= topBlockX; x++)
		            for(int z = bottomBlockZ; z <= topBlockZ; z++)
		                for(int y = bottomBlockY; y <= topBlockY; y++)
		                    w.getBlockAt(x, y, z).setType(Material.ENDER_STONE);
				return true;
			}
		});


		for(CommandHandler handler : commandHandlers)
			getCommand(handler.command).setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {
		getLogger().log(Level.INFO, "RageRush stopped.");
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String alias, String[] args) {
		for(CommandHandler handler : commandHandlers)
			if(handler.command.equalsIgnoreCase(c.getName()))
				return handler.handleCommand(s, args);
		s.sendMessage(ChatColor.DARK_RED + "The RageRush-Plugin cannot process your command.");
		return false;
	}
	
	public boolean setLoc(boolean id, CommandSender s) {
		if(!(s instanceof Player)) return false;
		setLoc(id, (Player)s, ((Player)s).getLocation());
		return true;
	}
	
	public void setLoc(boolean id, Player p, Location l) {
		LocationTuple lt = new LocationTuple();
		if(locs.get(p.getUniqueId()) != null) {
			lt = locs.get(p.getUniqueId());
			locs.remove(p.getUniqueId());
		}
		lt.setLocation(id, l);
		locs.put(p.getUniqueId(), lt);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getItem() == null || e.getItem().getType() != Material.WOOD_AXE)
			return;
		Player p = e.getPlayer();
		if(e.getAction() == Action.LEFT_CLICK_BLOCK)
			setLoc(false, p, e.getClickedBlock().getLocation());
		else if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
			setLoc(true, p, e.getClickedBlock().getLocation());
		else return;
		e.setCancelled(true);
	}
}
