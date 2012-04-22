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
		
		// Load corrections (current imprisonments)
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
		
		// Load primed corrections (now persistent)
		try
		{
			Scanner primedCorrectionScanner = new Scanner(new File("plugins/primedcorrections"));
			while (primedCorrectionScanner.hasNext())
			{
				String[] tmp = primedCorrectionScanner.next().split(";");
				this.primedCorrections.put(tmp[0], generateLocation(tmp[1]));
			}
			primedCorrectionScanner.close();
		} 
		catch (FileNotFoundException e)
		{
			System.err.println("Prisonbeds - can't find its primed corrections file!");
			System.err.println("Prisonbeds - Therefore, no prisons are primed!");
		}

		System.out.println("Prisonbeds - Hi folks, prisonbeds are now on :D.");
	}

	public void onDisable()
	{
		try 
		{
			BufferedWriter correctionsWriter = new BufferedWriter(new FileWriter("plugins/corrections"));
			for (String name : this.correctedSpawns.keySet())
			{
				Location tmp = (Location)this.correctedSpawns.get(name);
				correctionsWriter.write(name + ";" + tmp.getWorld().getName() + "," + tmp.getX() + "," + tmp.getY() + "," + tmp.getZ());
			}
			correctionsWriter.close();
			
			System.out.println("Prisonbeds - Corrections written to a file, they're persistent.");
		}
		catch (Exception e)
		{
			System.err.println("Prisonbeds - Cannot write to the corrections file.");
		}
		
		try 
		{
			BufferedWriter primedCorrectionsWriter = new BufferedWriter(new FileWriter("plugins/primedcorrections"));
			for (String name : this.primedCorrections.keySet())
			{
				Location tmp = (Location)this.primedCorrections.get(name);
				primedCorrectionsWriter.write(name + ";" + tmp.getWorld().getName() + "," + tmp.getX() + "," + tmp.getY() + "," + tmp.getZ());
			}
			primedCorrectionsWriter.close();
			
			System.out.println("Prisonbeds - Primed corrections written to a file, they're persistent too.");
		}
		catch (Exception e)
		{
			System.err.println("Prisonbeds - Cannot write to the primed corrections file.");
		}

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
			this.primedCorrections.put(s.getName(), s.getLocation());
			System.out.println("Prisonbeds - " + s.getPlayerListName() + " is primed at " + s.getLocation());
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" You have primed a prison, kill a player to activate it.");
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Prison will remain primed until you /pbunset it.");
			
			return true;
		} else if(cmd.getName().equalsIgnoreCase("pbunset")) {
			Player s = (Player)sender;
			primedCorrections.remove(s.getName());
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Your prison is no longer primed.");
			s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Players you kill will not be imprisoned.");
			
			return true;
		} else if(cmd.getName().equalsIgnoreCase("pbinfo")) {
			Player s = (Player)sender;
			
			if(primedCorrections.get(s.getName()) == null) {
				s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" You currently have no prison set.");
			} else {
				Location prisonLoc = (Location)primedCorrections.get(s.getName());
				String prisionLocName = prisonLoc.getWorld().getName() + " (" + prisonLoc.getX() + ", " + prisonLoc.getY() + ", " + prisonLoc.getZ() + ")";
				
				s.sendMessage(ChatColor.RED+"[Prisonbeds]"+ChatColor.WHITE+" Players you kill will be imprisoned at " + prisionLocName);
			}
			
			return true;
		} else if(cmd.getName().equalsIgnoreCase("pbhelp")) {
			Player s = (Player)sender;
			
			s.sendMessage("Prisonbeds Commands:");
			s.sendMessage("/pbset - Prime a prison at your current location, in which to imprison players you kill.");
			s.sendMessage("/pbunset - Unprime your prison location, so players you kill die normally.");
			s.sendMessage("/pbinfo - See if you have a prison primed, and, if so, where it is.");
			s.sendMessage("/pbhelp - Displays this help message.");
			
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