package org.sawiq.whoisit.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.sawiq.whoisit.config.WhoisitConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(
            method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void whoisit$modifyNametagVisibility(LivingEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();

        if (!(entity instanceof PlayerEntity)) return;
        if (networkHandler == null) return;
        if (networkHandler.getPlayerListEntry(entity.getUuid()) == null) return;

        if (WhoisitConfig.enabledOwnName && entity == client.cameraEntity) {
            cir.setReturnValue(MinecraftClient.isHudEnabled());
            return;
        }

        if (WhoisitConfig.enabledOtherPlayersName && entity != client.cameraEntity) {
            // если сущность невидима и тумблер выключен — не трогаем ванильную логику
            if (entity.isInvisible() && !WhoisitConfig.revealInvisiblePlayers) {
                return;
            }

            cir.setReturnValue(MinecraftClient.isHudEnabled());
        }
    }
}
