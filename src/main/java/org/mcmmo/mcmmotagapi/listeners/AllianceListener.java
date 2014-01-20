package org.mcmmo.mcmmotagapi.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.events.party.McMMOPartyAllianceChangeEvent;

import org.kitteh.tag.TagAPI;

public class AllianceListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPartyAllianceChange(McMMOPartyAllianceChangeEvent event) {
        List<Player> toRefresh = new ArrayList<Player>();

        toRefresh.add(event.getPlayer());
        toRefresh.addAll(PartyAPI.getOnlineMembers(event.getNewAlly()));
        toRefresh.addAll(PartyAPI.getOnlineMembers(event.getOldAlly()));

        for (Player player : toRefresh) {
            TagAPI.refreshPlayer(player);
        }
    }
}
