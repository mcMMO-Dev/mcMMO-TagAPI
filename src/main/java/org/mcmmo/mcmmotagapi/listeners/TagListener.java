package org.mcmmo.mcmmotagapi.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.mcmmo.mcmmotagapi.config.Config;
import org.mcmmo.mcmmotagapi.mcMMOTagAPI;

import com.gmail.nossr50.api.PartyAPI;

import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

public class TagListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAsyncPlayerReceiveNameTag(AsyncPlayerReceiveNameTagEvent event) {
        Player player = event.getPlayer();
        Player namedPlayer = event.getNamedPlayer();
        String partyName = null;

        boolean inParty = PartyAPI.inParty(player);

        if (inParty) {
            partyName = PartyAPI.getPartyName(player);
        }

        if (inParty && PartyAPI.getOnlineMembers(player).contains(namedPlayer)) {
            event.setTag(Config.getInstance().getPartyMemberColor() + namedPlayer.getName());
        }
        else if (mcMMOTagAPI.p.isAllianceEventPresent() && inParty && PartyAPI.hasAlly(partyName) && PartyAPI.getOnlineMembers(PartyAPI.getAllyName(partyName)).contains(namedPlayer)) {
            event.setTag(Config.getInstance().getPartyAllyColor() + namedPlayer.getName());
        }
        else {
            event.setTag(namedPlayer.getName());
        }
    }
}
