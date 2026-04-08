package com.pokecapsule.block

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos

class PlacedBallBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlocks.PLACED_BALL_ENTITY_TYPE, pos, state) {

    var ballStack: ItemStack = ItemStack.EMPTY
    var rotation:  Int       = 0

    override fun writeNbt(nbt: NbtCompound, lookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, lookup)
        if (!ballStack.isEmpty) {
            ItemStack.CODEC.encodeStart(lookup.getOps(NbtOps.INSTANCE), ballStack)
                .result()
                .ifPresent { nbt.put("BallStack", it) }
        }
        nbt.putInt("Rotation", rotation)
    }

    override fun readNbt(nbt: NbtCompound, lookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, lookup)
        ballStack = nbt.get("BallStack")?.let { element ->
            ItemStack.CODEC.parse(lookup.getOps(NbtOps.INSTANCE), element).result().orElse(null)
        } ?: ItemStack.EMPTY
        rotation = nbt.getInt("Rotation")
    }

    override fun toInitialChunkDataNbt(lookup: RegistryWrapper.WrapperLookup): NbtCompound = createNbt(lookup)
    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)
}
