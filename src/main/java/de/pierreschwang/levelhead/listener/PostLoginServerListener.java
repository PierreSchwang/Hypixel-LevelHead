package de.pierreschwang.levelhead.listener;

import de.pierreschwang.levelhead.LevelHeadAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.network.PayloadMessageEvent;
import net.labymod.api.event.events.network.server.PostLoginServerEvent;

public class PostLoginServerListener {

    private final LevelHeadAddon addon;

    public PostLoginServerListener(LevelHeadAddon addon) {
        this.addon = addon;
    }

    @Subscribe
    public void onPostLoginServer(PostLoginServerEvent event) {
        if (!addon.isHypixel(event.getServerData())) {
            return;
        }
        if (addon.getApiToken() != null && !addon.getApiToken().isEmpty()) {
            return;
        }
        System.out.println("Request API Key");
        addon.requestApiKey();
    }

}
