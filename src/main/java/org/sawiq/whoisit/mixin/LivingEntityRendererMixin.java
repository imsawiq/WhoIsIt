package org.sawiq.whoisit.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
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
        Entity camera = client.getCameraEntity(); // вместо client.cameraEntity
        if (camera == null) return;

        if (WhoisitConfig.enabledOwnName && entity == camera) {
            cir.setReturnValue(MinecraftClient.isHudEnabled());
        } else if (WhoisitConfig.enabledOtherPlayersName && entity != camera) {
            cir.setReturnValue(MinecraftClient.isHudEnabled());
        }
    }
}
