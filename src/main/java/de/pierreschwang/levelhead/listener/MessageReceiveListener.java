package de.pierreschwang.levelhead.listener;

import de.pierreschwang.levelhead.LevelHeadAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.client.chat.MessageReceiveEvent;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageReceiveListener {

    private static final String NEEDS_REGENERATE = "You already have an API Key, are you sure you want to regenerate it?";
    private static final Pattern API_KEY_PATTERN = Pattern.compile("API key is ([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})");

    private final LevelHeadAddon addon;

    public MessageReceiveListener(LevelHeadAddon addon) {
        this.addon = addon;
    }

    @Subscribe
    public void onMessageReceive(MessageReceiveEvent event) {
        if (!addon.isHypixel()) {
            return;
        }
        String raw = event.getComponent().getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
        // If new token needs to be generated, execute the sub command for that
        if (raw.startsWith(NEEDS_REGENERATE)) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendChatMessage("/api new");
            }
            return;
        }
        Matcher matcher;
        if (!(matcher = API_KEY_PATTERN.matcher(raw)).find()) {
            return;
        }
        String apiToken = matcher.group(1);
        System.out.println("Received Hypixel API Token: " + (apiToken.substring(0, 12) + "..."));

        addon.getConfig().addProperty("apiToken", apiToken);
        addon.setApiToken(apiToken);
        addon.saveConfig();
        addon.getCachedPlayers().invalidateAll();
    }

}
