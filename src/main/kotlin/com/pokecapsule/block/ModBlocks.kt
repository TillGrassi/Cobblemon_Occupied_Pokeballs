package com.pokecapsule.block

import com.pokecapsule.PokeCapsuleMod.MOD_ID
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.AbstractBlock
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlocks {

    lateinit var PLACED_BALL: PlacedBallBlock
        private set

    lateinit var PLACED_BALL_ENTITY_TYPE: BlockEntityType<PlacedBallBlockEntity>
        private set

    fun register() {
        PLACED_BALL = Registry.register(
            Registries.BLOCK,
            Identifier.of(MOD_ID, "placed_ball"),
            PlacedBallBlock(AbstractBlock.Settings.create().strength(0f).nonOpaque().noCollision())
        )
        PLACED_BALL_ENTITY_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(MOD_ID, "placed_ball"),
            FabricBlockEntityTypeBuilder.create(::PlacedBallBlockEntity, PLACED_BALL).build()
        )
    }
}
