package com.untamedears.Prisonbeds;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PrisonbedsPlayerListener implements Listener
{
	public static Prisonbeds plugin;

	public PrisonbedsPlayerListener(Prisonbeds instance)
	{
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent pre)
	{
		Player p = pre.getPlayer();
		Location loc = (Location)plugin.getCorrectedSpawns().get(p.getName());
		if (loc != null)
		{
			pre.setRespawnLocation(loc);
			pre.getPlayer().sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" You are imprisoned.");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent pje) {
		// When an imprisoned player logs in, put them at their imprisonment location.
		// Don't log out while escaping!
		// Should fix the log-out-teleport-up glitch
		
		Player p = pje.getPlayer();
		Location loc = (Location)plugin.getCorrectedSpawns().get(p.getName());
		if (loc != null)
		{
			pje.getPlayer().teleport(loc);
			pje.getPlayer().sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" You are imprisoned.");
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBedEnter(PlayerBedEnterEvent pbee)
	{
		HashMap correctedSpawns = plugin.getCorrectedSpawns();

		correctedSpawns.remove(pbee.getPlayer().getName());
		
		pbee.getPlayer().sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Congratulations! You have escaped from prison!");
		plugin.getServer().broadcastMessage(pbee.getPlayer().getDisplayName() + " has escaped from prison!");
	}
	
	// Primed corrections no longer removed on logout
	
}