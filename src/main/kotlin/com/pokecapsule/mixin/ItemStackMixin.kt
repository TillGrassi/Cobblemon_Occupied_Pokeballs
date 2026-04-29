package com.pokecapsule.mixin

import com.cobblemon.mod.common.item.PokeBallItem
import com.pokecapsule.util.BallNbt
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ItemStack::class)
class ItemStackMixin {

    @Inject(method = ["getMaxCount"], at = [At("RETURN")], cancellable = true)
    private fun limitOccupiedBallCount(cir: CallbackInfoReturnable<Int>) {
        val self = this as ItemStack
        if (self.item is PokeBallItem && BallNbt.hasPokemon(self)) {
            cir.returnValue = 1
        }
    }
}
