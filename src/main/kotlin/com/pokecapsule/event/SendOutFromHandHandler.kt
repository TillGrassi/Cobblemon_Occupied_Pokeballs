package com.pokecapsule.event

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.net.messages.server.SendOutPokemonPacket
import com.cobblemon.mod.common.net.serverhandling.storage.SendOutPokemonHandler
import com.pokecapsule.PokeCapsuleMod.LOGGER
import com.pokecapsule.util.BallNbt
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object SendOutFromHandHandler {

    fun registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(SendOutFromHandPayload.ID) { _, context ->
            context.server().execute {
                handle(context.player())
            }
        }
    }

    private fun handle(player: ServerPlayerEntity) {
        val stack = player.mainHandStack
        if (!BallNbt.hasPokemon(stack)) return

        val party = Cobblemon.storage.getParty(player)
        val hasSpace = (0..5).any { party.get(it) == null }
        if (!hasSpace) {
            player.sendMessage(
                Text.translatable("cobblemon_occupied_pokeballs.msg.party_full"),
                true
            )
            return
        }

        val server  = player.server!!
        val pokemon = BallNbt.readPokemon(stack, server.registryManager)
        if (pokemon == null) {
            LOGGER.error("Cobblemon Occupied Pokéballs: corrupt ball NBT for player ${player.name.string}")
            player.sendMessage(Text.translatable("cobblemon_occupied_pokeballs.msg.corrupt"), true)
            return
        }

        party.add(pokemon)

        // Trigger Cobblemon's own send-out handler using the slot the Pokémon landed in.
        // This plays the full ball-throw animation exactly as if the player pressed send-out.
        val slot = (0..5).firstOrNull { party.get(it) == pokemon }
        if (slot != null) {
            SendOutPokemonHandler.handle(SendOutPokemonPacket(slot), server, player)
        }

        BallNbt.clear(stack)
        stack.decrement(1)
    }
}
