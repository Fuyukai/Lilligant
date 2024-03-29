/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.flowerplanting;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import tf.veriny.lilligant.MakeUp;

@Mixin(FlowerBlock.class)
abstract class AddMoreGenericFlowerPlantingTag extends PlantBlock {
    private AddMoreGenericFlowerPlantingTag() {
        super(null);
        throw new NotImplementedException();
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        var original = super.canPlantOnTop(floor, world, pos);
        if (original || !this.getDefaultState().isIn(MakeUp.INSTANCE.getOVERRIDE_PLANT_CHECK())) {
            return original;
        }

        return floor.isIn(MakeUp.INSTANCE.getFLOWER_PLANT_BLOCKS());
    }
}
