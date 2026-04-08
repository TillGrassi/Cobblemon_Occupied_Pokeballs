package com.pokecapsule.event

import com.pokecapsule.PokeCapsuleMod.MOD_ID
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

/** Client → Server: eject the Cobblemon-selected party Pokémon (slot 0-5). */
class EjectBallPayload(val slot: Int) : CustomPayload {
    companion object {
        val ID: CustomPayload.Id<EjectBallPayload> = CustomPayload.Id(Identifier.of(MOD_ID, "eject_ball"))
        val CODEC: PacketCodec<RegistryByteBuf, EjectBallPayload> =
            object : PacketCodec<RegistryByteBuf, EjectBallPayload> {
                override fun decode(buf: RegistryByteBuf) = EjectBallPayload(buf.readVarInt())
                override fun encode(buf: RegistryByteBuf, value: EjectBallPayload) { buf.writeVarInt(value.slot) }
            }
    }
    override fun getId(): CustomPayload.Id<EjectBallPayload> = ID
}

/** Client → Server: reclaim the Pokémon stored in the player's held ball. */
data object ReclaimBallPayload : CustomPayload {
    val ID: CustomPayload.Id<ReclaimBallPayload> = CustomPayload.Id(Identifier.of(MOD_ID, "reclaim_ball"))
    val CODEC: PacketCodec<RegistryByteBuf, ReclaimBallPayload> = PacketCodec.unit(ReclaimBallPayload)
    override fun getId(): CustomPayload.Id<ReclaimBallPayload> = ID
}

/** Client → Server: add held ball's Pokémon to party and immediately send it out. */
data object SendOutFromHandPayload : CustomPayload {
    val ID: CustomPayload.Id<SendOutFromHandPayload> = CustomPayload.Id(Identifier.of(MOD_ID, "send_out_from_hand"))
    val CODEC: PacketCodec<RegistryByteBuf, SendOutFromHandPayload> = PacketCodec.unit(SendOutFromHandPayload)
    override fun getId(): CustomPayload.Id<SendOutFromHandPayload> = ID
}
