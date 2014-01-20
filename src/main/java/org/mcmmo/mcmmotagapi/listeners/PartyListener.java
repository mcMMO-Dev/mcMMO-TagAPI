package org.mcmmo.mcmmotagapi.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;

import org.kitteh.tag.TagAPI;

public class PartyListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPartyChange(McMMOPartyChangeEvent event) {
        List<Player> toRefresh = new ArrayList<Player>();

        toRefresh.add(event.getPlayer());
        toRefresh.addAll(PartyAPI.getOnlineMembers(event.getNewParty()));
        toRefresh.addAll(PartyAPI.getOnlineMembers(event.getOldParty()));

        for (Player player : toRefresh) {
            TagAPI.refreshPlayer(player);
        }
    }
}
