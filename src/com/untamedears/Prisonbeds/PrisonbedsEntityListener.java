package com.untamedears.Prisonbeds;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
 
public class PrisonbedsEntityListener implements Listener
{
	public static Prisonbeds plugin;
	
	public PrisonbedsEntityListener(Prisonbeds instance)
	{
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent ede)
	{
		Entity tmp = ede.getEntity();
 
		if ((tmp instanceof Player))
		{
			Player victim = (Player)tmp;
			Player killer = victim.getKiller();
 
			HashMap primedCorrections = plugin.getPrimedCorrections();
			HashMap correctedSpawns = plugin.getCorrectedSpawns();
			if ((killer != null) && (killer.getName() != null) && (primedCorrections.get(killer.getName()) != null))
			{
				// First thing we do, record the imprisonment.
				// We do this before any IO.
				// Hopefully will stop people respawning before imprisonment gets recorded.
				Location prisonLoc = (Location)primedCorrections.get(killer.getName());
				correctedSpawns.put(victim.getName(), prisonLoc);
				
				
				System.out.println("Prisonbeds - " + killer.getDisplayName() + " imprisoned " + victim.getDisplayName());
				victim.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" You were imprisoned by " + killer.getDisplayName());
				
				// Prepare the location for announcement
				String prisionLocName = prisonLoc.getWorld().getName() + " (" + prisonLoc.getX() + ", " + prisonLoc.getY() + ", " + prisonLoc.getZ() + ")";
				
				// Since it's easy to imprison many people at once, let everyone know who gets imprisoned and where, for balance.
				plugin.getServer().broadcastMessage(victim.getDisplayName() + " was imprisoned at " + prisionLocName + " by " + killer.getDisplayName());
			
				// Un-prime the killer's prison (it got used up)
				primedCorrections.remove(killer.getName());
			}
		}
   }
 }