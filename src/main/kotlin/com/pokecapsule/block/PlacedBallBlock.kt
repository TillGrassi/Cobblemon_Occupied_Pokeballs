package com.pokecapsule.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class PlacedBallBlock(settings: Settings) : BlockWithEntity(settings) {

    companion object {
        val CODEC: MapCodec<PlacedBallBlock> = createCodec(::PlacedBallBlock)

        // 10×10×10 px centred, sitting flush on the ground — same proportions as a skull
        val SHAPE: VoxelShape = createCuboidShape(3.0, 0.0, 3.0, 13.0, 10.0, 13.0)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = CODEC
    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape = SHAPE
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = PlacedBallBlockEntity(pos, state)

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        if (!world.isClient) {
            val be = world.getBlockEntity(pos) as? PlacedBallBlockEntity
            if (be != null && !be.ballStack.isEmpty && !player.abilities.creativeMode) {
                dropStack(world, pos, be.ballStack)
            }
        }
        return super.onBreak(world, pos, state, player)
    }

    override fun getDroppedStacks(state: BlockState, builder: net.minecraft.loot.context.LootContextParameterSet.Builder): MutableList<ItemStack> =
        mutableListOf()
}
