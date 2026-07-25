package tf.veriny.lilligant.mixin.durability;

import net.minecraft.item.ToolMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolMaterials.class)
abstract class MakeToolsMoreDurable {
    private MakeToolsMoreDurable() {
    }

    @Inject(method = "getDurability", at = @At("HEAD"), cancellable = true)
    void ll$overrideDurability(CallbackInfoReturnable<Integer> cir) {
        var self = (ToolMaterials) (Object) this;
        // mostly random numbers
        cir.setReturnValue(switch (self) {
            case WOOD -> 600;
            case STONE -> 1560;
            case IRON -> 11010;
            case DIAMOND -> 25610;
            case GOLD -> 250;
            case NETHERITE -> 55390;
        });
    }
}
