package tf.veriny.lilligant.mixin.nohunger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Removes the hunger bar entirely in peaceful mode.
 */
@Mixin(InGameHud.class)
abstract class RemoveHungerBar {
    @Shadow @Final private MinecraftClient client;

    private RemoveHungerBar() {}

    // getHeartCount() will return the heart count if you're riding something.
    // if it's more than 1, the hunger bar won't be rendered.
    // fun fact: this mixin has worked since 1.15
    @Redirect(
            method = "renderStatusBars",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"
            )
    )
    int ll$proxyGetHeartCount(InGameHud instance, LivingEntity entity) {
        if (entity != null) {
            float f = entity.getMaxHealth();
            int i = (int)(f + 0.5F) / 2;
            if (i > 30) {
                i = 30;
            }

            return i;
        }

        var world = this.client.world;
        if (world != null) {
            return world.getDifficulty() == Difficulty.PEACEFUL ? -1 : 0;
        }
        return 0;
    }
}
