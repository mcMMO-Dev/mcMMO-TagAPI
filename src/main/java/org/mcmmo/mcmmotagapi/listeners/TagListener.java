package org.mcmmo.mcmmotagapi.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.api.PartyAPI;

import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.mcmmo.mcmmotagapi.config.Config;

public class TagListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerReceiveNameTag(PlayerReceiveNameTagEvent event) {
        Player player = event.getPlayer();
        Player namedPlayer = event.getNamedPlayer();

        if (PartyAPI.inParty(player) && PartyAPI.getOnlineMembers(player).contains(namedPlayer)) {
            event.setTag(Config.getInstance().getPartyMemberColor() + namedPlayer.getDisplayName());
        }
        else {
            event.setTag(namedPlayer.getDisplayName());
        }
    }
}
