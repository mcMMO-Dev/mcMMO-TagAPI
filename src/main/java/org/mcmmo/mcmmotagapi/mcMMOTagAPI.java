package org.mcmmo.mcmmotagapi;

import java.util.logging.Level;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.mcmmo.mcmmotagapi.config.Config;
import org.mcmmo.mcmmotagapi.listeners.McMMOListener;
import org.mcmmo.mcmmotagapi.listeners.TagListener;
import org.mcmmo.mcmmotagapi.util.LogFilter;

public class mcMMOTagAPI extends JavaPlugin {
    public static mcMMOTagAPI p;

    private boolean mcMMOEnabled = false;
    private boolean tagAPIEnabled = false;

    /**
     * Things to be run when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        try {
            p = this;
            getLogger().setFilter(new LogFilter(this));

            setupMcMMO();
            setupTagAPI();

            if (!isMcMMOEnabled()) {
                this.getLogger().log(Level.WARNING, " requires mcMMO to run, please download mcMMO. http://dev.bukkit.org/server-mods/mcmmo/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            if (!isTagAPIEnabled()) {
                this.getLogger().log(Level.WARNING, " requires TagAPI to run, please download TagAPI. http://dev.bukkit.org/server-mods/tag/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            Config.getInstance();

            registerEvents();
        }
        catch (Throwable t) {
            getLogger().severe("There was an error while enabling mcMMO-TagAPI!");

            if (!(t instanceof ExceptionInInitializerError)) {
                t.printStackTrace();
            }
            else {
                getLogger().info("Please do not replace the mcMMO-TagAPI jar while the server is running.");
            }

            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void setupMcMMO() {
        if (getServer().getPluginManager().isPluginEnabled("mcMMO")) {
            mcMMOEnabled = true;
        }
    }

    private void setupTagAPI() {
        if (getServer().getPluginManager().isPluginEnabled("TagAPI")) {
            tagAPIEnabled = true;
        }
    }

    /**
     * Things to be run when the plugin is disabled.
     */
    @Override
    public void onDisable() {
    }

    public void debug(String message) {
        getLogger().info("[Debug] " + message);
    }

    public boolean isMcMMOEnabled() {
        return mcMMOEnabled;
    }

    public boolean isTagAPIEnabled() {
        return tagAPIEnabled;
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Register events
        pluginManager.registerEvents(new McMMOListener(), this);
        pluginManager.registerEvents(new TagListener(), this);
    }
}
