package io.github.sgiobairog.mc_bathy.main;



// MC Bathy
// SpigotMC Plugin for displaying bathyscapes from Bathymetric Attributed Grid (BAG) files.
//
// Jason Paul R. Wilson at your service as SgiobairOg
// MIT License

// Importing find goods since 1983
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;


public class main extends JavaPlugin {
	
	public static main instance;
	public static File dataFolder;
	public static String dataFolderPath = "";
	
    public void onEnable() {
		instance = this;
		
		dataFolder = instance.getDataFolder();
		dataFolderPath = dataFolder.toString();
		getCommand("bathy").setExecutor(new CommandBathy());
		getLogger().info("Data folder is: " + dataFolderPath);
    }
    
    public void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
        	Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
	
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    }
}


