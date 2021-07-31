package de.pierreschwang.levelhead.listener;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import de.pierreschwang.levelhead.LevelHeadAddon;
import de.pierreschwang.levelhead.api.data.HypixelPlayerData;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.client.renderer.RenderNameTagEvent;
import net.labymod.main.LabyMod;
import net.labymod.user.User;
import net.labymod.user.group.EnumGroupDisplayType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;

public class RenderNameTagListener {

    private final LevelHeadAddon levelHeadAddon;

    public RenderNameTagListener(LevelHeadAddon levelHeadAddon) {
        this.levelHeadAddon = levelHeadAddon;
    }

    @Subscribe
    public void onRenderTag(RenderNameTagEvent event) {
        if (event.getPosition() != RenderNameTagEvent.Position.AFTER_NAME_TAG) {
            return;
        }
        if (event.getEntity().getUniqueID().equals(LabyMod.getInstance().getPlayerUUID())) {
            return;
        }
        if (!LabyMod.getInstance().getCurrentServerData().getIp().contains("hypixel.net")) {
            return;
        }
        try {
            Futures.addCallback(levelHeadAddon.getCachedPlayers().get(event.getEntity().getUniqueID()), new FutureCallback<HypixelPlayerData>() {
                @Override
                public void onSuccess(@Nullable HypixelPlayerData result) {
                    if (result == null || !result.isSuccess() || result.getPlayer() == null)
                        return;
                    renderLevel(event, result.getPlayer().getNetworkLevel());
                }

                @Override
                public void onFailure(@NotNull Throwable t) {
                    t.printStackTrace();
                }
            }, Minecraft.getInstance());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Based on https://github.com/MineFlash07/friendtags-addon-1.16/blob/master/src/main/java/de/raik/friendtags/NameTagRenderer.java
    private void renderLevel(RenderNameTagEvent event, int networkLevel) {
        event.getMatrixStack().push();
        User user = LabyMod.getInstance().getUserManager().getUser(event.getEntity().getUniqueID());
        if (user.getGroup() != null && user.getGroup().getDisplayType() == EnumGroupDisplayType.ABOVE_HEAD) {
            event.getMatrixStack().translate(0.0D, 9.0F * 1.15 * 0.01666668F * 0.8F, 0.0D);
        }
        event.getMatrixStack().translate(0.0D, 0.15F, 0.0D);
        float scale = 0.01666668F * 1.8F;
        event.getMatrixStack().rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
        event.getMatrixStack().scale(-scale, -scale, scale);
        String tag = "§6Level: §e" + networkLevel;
        float x = -Minecraft.getInstance().fontRenderer.getStringPropertyWidth(new StringTextComponent(tag)) / 2.0F;
        Minecraft.getInstance().fontRenderer.renderString(
                tag, x, 0.0F, -1,
                false, event.getMatrixStack().getLast().getMatrix(), event.getBuffer(),
                false, 0, event.getPackedLight()
        );
        event.getMatrixStack().pop();
    }

}
