package org.mcmmo.mcmmotagapi.config;

import org.bukkit.ChatColor;

public class Config extends AutoUpdateConfigLoader {
    private static Config instance;

    private Config() {
        super("config.yml");
        validate();
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }

    @Override
    protected boolean validateKeys() {
        return true;
    }

    @Override
    protected void loadKeys() {}

    public ChatColor getPartyMemberColor() {
        try {
            return ChatColor.valueOf(config.getString("TagAPI.Party.Member_Color", "AQUA").toUpperCase().trim());
        }
        catch (IllegalArgumentException ex) {
            return ChatColor.AQUA;
        }
    }

    public boolean getStatsTrackingEnabled() { return config.getBoolean("General.Stats_Tracking", true); }
    public boolean getUpdateCheckEnabled() { return config.getBoolean("General.Update_Check", true); }
    public boolean getPreferBeta() { return config.getBoolean("General.Prefer_Beta", false); }
}
