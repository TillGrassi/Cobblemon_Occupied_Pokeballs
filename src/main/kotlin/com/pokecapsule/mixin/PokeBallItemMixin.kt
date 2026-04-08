package com.pokecapsule.mixin

import com.cobblemon.mod.common.item.PokeBallItem
import com.pokecapsule.util.BallNbt
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(PokeBallItem::class)
class PokeBallItemMixin {

    @Inject(method = ["use"], at = [At("HEAD")], cancellable = true)
    private fun preventThrowWhenOccupied(
        world: World,
        user: PlayerEntity,
        hand: Hand,
        cir: CallbackInfoReturnable<TypedActionResult<ItemStack>>
    ) {
        val stack = user.getStackInHand(hand)
        if (BallNbt.hasPokemon(stack)) {
            cir.returnValue = TypedActionResult.fail(stack)
        }
    }
}
