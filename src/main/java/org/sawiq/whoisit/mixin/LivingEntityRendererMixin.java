package org.sawiq.whoisit.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.sawiq.whoisit.config.WhoisitConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(
            method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void whoisit$modifyNametagVisibility(LivingEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        Entity camera = client.getCameraEntity();
        ClientPacketListener connection = client.getConnection();

        if (camera == null) return;
        if (!(entity instanceof Player)) return;
        if (connection == null) return;
        if (connection.getPlayerInfo(entity.getUUID()) == null) return;

        if (WhoisitConfig.enabledOwnName && entity == camera) {
            cir.setReturnValue(!client.gui.hud.isHidden());
            return;
        }

        if (WhoisitConfig.enabledOtherPlayersName && entity != camera) {
            // Не палим невидимых, если тумблер выключен
            if (entity.isInvisible() && !WhoisitConfig.revealInvisiblePlayers) {
                return; // оставляем ванильную логику
            }

            cir.setReturnValue(!client.gui.hud.isHidden());
        }
    }
}
