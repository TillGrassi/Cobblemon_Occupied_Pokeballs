package com.pokecapsule.event

import com.cobblemon.mod.common.item.PokeBallItem
import com.pokecapsule.block.ModBlocks
import com.pokecapsule.block.PlacedBallBlockEntity
import com.pokecapsule.util.BallNbt
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.SideShapeType
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper

object PlaceBallHandler {

    fun register() {
        UseBlockCallback.EVENT.register { player, world, hand, hitResult ->
            // Only main hand, only occupied Poké Balls, only clicking the top face
            if (hand != Hand.MAIN_HAND)                          return@register ActionResult.PASS
            val stack = player.getStackInHand(hand)
            if (stack.item !is PokeBallItem)                     return@register ActionResult.PASS
            if (!BallNbt.hasPokemon(stack))                      return@register ActionResult.PASS
            if (hitResult.side != Direction.UP)                  return@register ActionResult.PASS

            val targetPos = hitResult.blockPos.up()

            if (!world.isClient) {
                // Require a full solid surface beneath
                val belowState = world.getBlockState(hitResult.blockPos)
                if (!belowState.isSideSolid(world, hitResult.blockPos, Direction.UP, SideShapeType.FULL))
                    return@register ActionResult.PASS

                // Target position must be air (or replaceable)
                if (!world.getBlockState(targetPos).isAir)
                    return@register ActionResult.PASS

                // Calculate yaw rotation (0–15), same formula Minecraft uses for skulls
                val rotation = MathHelper.floor((player.yaw + 180f) * 16f / 360f + 0.5f) and 15

                world.setBlockState(targetPos, ModBlocks.PLACED_BALL.defaultState)
                val be = world.getBlockEntity(targetPos) as? PlacedBallBlockEntity
                if (be != null) {
                    be.ballStack = stack.copy()
                    be.rotation  = rotation
                    be.markDirty()
                    world.updateListeners(targetPos, ModBlocks.PLACED_BALL.defaultState, ModBlocks.PLACED_BALL.defaultState, 3)
                }

                if (!player.abilities.creativeMode) stack.decrement(1)
            }

            ActionResult.SUCCESS
        }
    }
}
