package io.github.sgiobairog.mc_bathy.main;

import io.github.sgiobairog.mc_bathy.main.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBathy implements CommandExecutor {
	
	String bathyName = null;
	Boolean readyState = false;
	Double xMin = 0.0;
	Double yMin = 0.0;
	Double zMin = 0.0;
	Double xMax = 0.0;
	Double yMax = 0.0;
	Double zMax = 0.0;
	Double xSpan = 0.0;
	Double ySpan = 0.0;
	Double zSpan = 0.0;
	List<Hashtable<String, Double>> cells = new ArrayList<Hashtable<String, Double>>();
	
	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args ) {
	
		Player player = (Player) sender;
		File dir = main.dataFolder;
		String method = "";
		
		if( args.length == 0 ) {
			method = "h";
		} else {
			method = args[0];
		}
		
		switch ( method.toLowerCase() ) {
			case "h": case "help":
				return help(player);
				
			case "l": case "list":
				return getBathyList(dir,player);
			
			case "b": case "build":
				bathyName = args[1];
				try {
					preLoadBathyBuild(dir, player, bathyName);
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				
				
			default:
				player.sendMessage("An unspecified error occured.");
				return help(player);
		}
	}
	
	public void preLoadBathyBuild(File dir, Player player, String bathyName) throws IOException {
		player.sendMessage("Pre-loading " + bathyName + "...");
		player.sendMessage("You will be prompted to confirm your actions once we know what you just asked for.");

        FileInputStream inStream = null;
        Scanner sc = null;
        
		try {

			inStream = new FileInputStream(dir.getPath() + "/" + bathyName);
			sc = new Scanner(inStream, "UTF-8");
			
            while (sc.hasNextLine()) {
            	String line = sc.nextLine();
                String[] coords = line.split(",");
                Double x = Double.parseDouble(coords[0]);
                Double y = Double.parseDouble(coords[2]);
                Double z = Double.parseDouble(coords[1]);
                
                xMax = x > xMax ? x : xMax;
                yMax = y > yMax ? y : yMax;
                zMax = z > zMax ? z : zMax;
                xMin = x < xMin ? x : xMin;
                yMin = z < yMin ? z : yMin;
                zMin = z < zMin ? z : zMin;
                
            }
            
            xSpan = xMax - xMin;
            ySpan = (yMax - yMin) - (yMin - yMin);
            zSpan = zMax - zMin;
            
            readyState = true;
            
            player.sendMessage("Bathy is ready to generate.");
            player.sendMessage("This action will replace an area of " + xSpan + " x " + ySpan + " x " + zSpan + " blocks and cannot be undone.");
            player.sendMessage("To confirm this action and loa the bathy to the world enter '/bathy confirm'");
            
            if (sc.ioException() != null) {
    			throw sc.ioException();
    		}
            
        } finally {
        	if (inStream != null) {
        		inStream.close();
        	}
        	if (sc != null) {
        		sc.close();
        	}
        }
	}
	
	public static Hashtable<String, Double> gridHash( double x, double y, double z) {
		Hashtable<String, Double> newCell = new Hashtable<String, Double>();
		newCell.put("x", x);
		newCell.put("y", y);
		newCell.put("z", z);
		
		return newCell;
	}
	
	public Boolean help(Player player) {
		// Provide a manual
		player.sendMessage("You're presently beyond my help.");
		return true;
	}
	
	public Boolean getBathyList(File dir, Player player) {
		// Provide a list of bathy files
		player.sendMessage("Loading list...");
		player.sendMessage("Looking for files in " + dir);
		
    	File[] bags;

    	bags = dir.listFiles(new FilenameFilter() { 
    		public boolean accept(File dir, String filename) {
    			return filename.endsWith(".xyz"); 
    		}
    	});
    	
    	if( bags.length > 0 ) {	
    		// List out bags
    		player.sendMessage("Found " + bags.length + " files:");
    		
	    	for( File bag : bags ) {
	    		player.sendMessage("[]    " + ChatColor.ITALIC + "" + bag.getName());
	    	}
	    	
	    	player.sendMessage("Use /bathy <b | build> <" + ChatColor.ITALIC + "filename" + ChatColor.RESET + ">");
	    	return true;
    	} else {
    		// Respectfully decline to acquiesce
    		player.sendMessage("No .xyz files found; refer to the README for information on where to find new files.");
    		return false;
    	}
	}
}
