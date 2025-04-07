package tf.veriny.lilligant.mixin.entityblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.veriny.lilligant.config.LilligantConfig;

import java.util.List;

/**
 * Prevents bees from trying to spawn.
 */
@Mixin(BeehiveBlockEntity.class)
abstract class BlockBeehiveSpam {
    private BlockBeehiveSpam() {}

    @Inject(method = "releaseBee", at = @At("HEAD"), cancellable = true)
    private static void ll$hijackReleaseBee(World world, BlockPos pos, BlockState state,
                              BeehiveBlockEntity.Bee bee, List<Entity> entities, BeehiveBlockEntity.BeeState beeState, BlockPos flowerPos, CallbackInfoReturnable<Boolean> cir) {
        if (LilligantConfig.INSTANCE.getEntityBlockerConfig().getBlockBeeSpawning()) {
            // just pretend we did, but don't actually release any bees.
            cir.setReturnValue(true);
        }
    }
}
