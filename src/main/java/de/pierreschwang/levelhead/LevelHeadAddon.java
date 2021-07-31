package de.pierreschwang.levelhead;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.pierreschwang.levelhead.api.HypixelApi;
import de.pierreschwang.levelhead.api.HypixelApiResponse;
import de.pierreschwang.levelhead.api.data.HypixelPlayerData;
import de.pierreschwang.levelhead.listener.MessageReceiveListener;
import de.pierreschwang.levelhead.listener.PostLoginServerListener;
import de.pierreschwang.levelhead.listener.RenderNameTagListener;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.ServerData;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LevelHeadAddon extends LabyModAddon {

    private static final Set<String> HYPIXEL_IPS = Sets.newHashSet(
            "hypixel.net", "mc.hypixel.net"
    );

    private String apiToken;
    private HypixelApi hypixelApi;
    private LoadingCache<UUID, ListenableFuture<HypixelPlayerData>> cachedPlayers;

    @Override
    public void onEnable() {
        this.hypixelApi = new HypixelApi(() -> Optional.ofNullable(apiToken));
        cachedPlayers = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<UUID, ListenableFuture<HypixelPlayerData>>() {
            @Override
            public ListenableFuture<HypixelPlayerData> load(UUID key) {
                return Futures.transform(hypixelApi.getPlayerData(key), HypixelApiResponse::getResponse);
            }
        });
        getApi().getEventService().registerListener(new RenderNameTagListener(this));
        getApi().getEventService().registerListener(new PostLoginServerListener(this));
        getApi().getEventService().registerListener(new MessageReceiveListener(this));
    }

    @Override
    public void loadConfig() {
        this.apiToken = getConfig().has("apiToken") ? getConfig().get("apiToken").getAsString() : null;
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
    }

    public void requestApiKey() {
        if (!isHypixel()) {
            return;
        }
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendChatMessage("/api");
        }
    }

    public boolean isHypixel() {
        return isHypixel(LabyMod.getInstance().getCurrentServerData());
    }

    public boolean isHypixel(ServerData serverData) {
        return HYPIXEL_IPS.contains(serverData.getIp());
    }

    public LoadingCache<UUID, ListenableFuture<HypixelPlayerData>> getCachedPlayers() {
        return cachedPlayers;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

}