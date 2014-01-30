package org.mcmmo.mcmmotagapi;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.mcmmo.mcmmotagapi.config.Config;
import org.mcmmo.mcmmotagapi.listeners.AllianceListener;
import org.mcmmo.mcmmotagapi.listeners.PartyListener;
import org.mcmmo.mcmmotagapi.listeners.TagListener;
import org.mcmmo.mcmmotagapi.util.LogFilter;

public class mcMMOTagAPI extends JavaPlugin {
    public static mcMMOTagAPI p;

    private boolean mcMMOEnabled = false;
    private boolean tagAPIEnabled = false;

    private boolean allianceEventPresent;

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
                this.getLogger().warning("mcMMO-TagAPI requires mcMMO to run, please download mcMMO. http://dev.bukkit.org/server-mods/mcmmo/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            if (!isTagAPIEnabled()) {
                this.getLogger().warning("mcMMO-TagAPI requires TagAPI to run, please download TagAPI. http://dev.bukkit.org/server-mods/tag/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            if (!checkTagVersion()) {
                this.getLogger().warning("You need a newer version of TagAPI! Get it at http://dev.bukkit.org/server-mods/tag/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            allianceEventPresent = checkAllianceEventEnabled();

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
     * Check mcMMO version
     * McMMOPartyAllianceChangeEvent was added in 1.4.08-dev-b3452
     */
    private boolean checkAllianceEventEnabled() {
        String[] version = getServer().getPluginManager().getPlugin("mcMMO").getDescription().getVersion().split("-");

        try {
            if (Integer.parseInt(version[0].replaceAll("[.]", "")) > 1408) {
                debug("mcMMO version 1.4.08+ found!");
                return true;
            }
        }
        catch (NumberFormatException ignored) {
            getLogger().warning("Could not determine mcMMO version (are you using a custom one?), assuming that it's outdated.");
            return false;
        }

        try {
            if (Integer.parseInt(version[(version.length - 1)].replace("b", "")) >= 3452) {
                debug("mcMMO build 3452+ found!");
                return true;
            }
        }
        catch (NumberFormatException ignored) {
            getLogger().warning("Could not determine mcMMO build (are you using a custom one?), assuming that it's outdated.");
            return false;
        }

        debug("This mcMMO version does not have McMMOPartyAllianceChangeEvent");
        return false;
    }

    /**
     * Check TagAPI version
     * AsyncPlayerReceiveNameTagEvent was added in 3.0
     */
    private boolean checkTagVersion() {
        try {
            Class.forName("org.kitteh.tag.AsyncPlayerReceiveNameTagEvent");
        }
        catch (final ClassNotFoundException e) {
            debug("This TagAPI version does not have AsyncPlayerReceiveNameTagEvent");
            return false;
        }

        return true;
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
        pluginManager.registerEvents(new PartyListener(), this);
        pluginManager.registerEvents(new TagListener(), this);

        if (allianceEventPresent) {
            pluginManager.registerEvents(new AllianceListener(), this);
        }
    }
}
