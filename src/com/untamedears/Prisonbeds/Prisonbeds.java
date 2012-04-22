package com.untamedears.Prisonbeds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Prisonbeds extends JavaPlugin
{
	private final PrisonbedsPlayerListener playerListener = new PrisonbedsPlayerListener(this);
	private final PrisonbedsEntityListener entityListener = new PrisonbedsEntityListener(this);
	protected HashMap<String, Location> correctedSpawns;
	protected HashMap<String, Location> primedCorrections;

	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.entityListener, this);
		
		this.primedCorrections = new HashMap();
		this.correctedSpawns = new HashMap();
		
		try
		{
			Scanner correctionScanner = new Scanner(new File("plugins/corrections"));
			while (correctionScanner.hasNext())
			{
				String[] tmp = correctionScanner.next().split(";");
				this.correctedSpawns.put(tmp[0], generateLocation(tmp[1]));
			}
			correctionScanner.close();
		} 
		catch (FileNotFoundException e)
		{
			System.err.println("Prisonbeds - can't find its corrected spawns file!");
			System.err.println("Prisonbeds - Therefore, prisoners are not contained!");
		}

		System.out.println("Prisonbeds - Hi folks, prisonbeds are now on :D.");
	}

	public void onDisable()
	{
		try 
		{
			BufferedWriter butthead = new BufferedWriter(new FileWriter("plugins/corrections"));
			for (String name : this.correctedSpawns.keySet())
			{
				Location tmp = (Location)this.correctedSpawns.get(name);
				butthead.write(name + ";" + tmp.getWorld().getName() + "," + tmp.getX() + "," + tmp.getY() + "," + tmp.getZ());
			}
			butthead.close();
		}
		catch (Exception e)
		{
			System.err.println("Prisonbeds - Cannot write to the corrections file.");
		}

		System.out.println("Prisonbeds - Corrections written to a file, they're persistant.");
		System.out.println("Prisonbeds - Prisonbeds are disabled.");
	}

	public Location generateLocation(String loc)
	{
		String[] tmp = loc.split(",");
		return new Location(getServer().getWorld(tmp[0]), 
				Double.parseDouble(tmp[1]), 
				Double.parseDouble(tmp[2]), 
				Double.parseDouble(tmp[3]));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(!(sender instanceof Player)) {
			return false; // Silently fail for console
		}
		if (cmd.getName().equalsIgnoreCase("pbset"))
		{
			Player s = (Player)sender;
			if (s.getWorld().getEnvironment() == World.Environment.THE_END)
			{
				getServer().broadcastMessage(s.getDisplayName() + " was a derp and tried to make an unescapable prison!  FOR SHAME!");
				return false;
			}
			this.primedCorrections.put(s.getPlayerListName(), s.getLocation());
			System.out.println("Prisonbeds - " + s.getPlayerListName() + " is primed at " + s.getLocation());
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" You have primed a prison, kill a player to activate it.");
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Prison will remain primed until you /pbunset it.");
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Or until the server restarts.");
			
			return true;
		} else if(cmd.getName().equalsIgnoreCase("pbunset")) {
			Player s = (Player)sender;
			primedCorrections.remove(s.getName());
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Your prison is no longer primed.");
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Players you kill will not be imprisoned.");
			
			return true;
		}
		return false;
	}

	public HashMap<String, Location> getCorrectedSpawns()
	{
		return this.correctedSpawns;
	}

	public HashMap<String, Location> getPrimedCorrections()
	{
		return this.primedCorrections;
	}
}