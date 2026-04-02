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
            method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modifyNametagVisibility(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
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
            // если игрок невидим и тумблер выключен — не вмешиваемся (оставляем ванильную логику)
            if (entity.isInvisible() && !WhoisitConfig.revealInvisiblePlayers) {
                return;
            }

            cir.setReturnValue(MinecraftClient.isHudEnabled());
        }
    }
}
