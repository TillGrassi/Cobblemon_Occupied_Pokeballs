package com.pokecapsule.event

import com.cobblemon.mod.common.Cobblemon
import com.pokecapsule.PokeCapsuleMod.LOGGER
import com.pokecapsule.util.BallNbt
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object ReclaimHandler {

    fun registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(ReclaimBallPayload.ID) { payload, context ->
            context.server().execute {
                handleReclaim(context.player())
            }
        }
    }

    private fun handleReclaim(player: ServerPlayerEntity) {
        val stack = player.mainHandStack
        if (!BallNbt.hasPokemon(stack)) return

        val registryManager = player.server!!.registryManager
        val pokemon = BallNbt.readPokemon(stack, registryManager)
        if (pokemon == null) {
            LOGGER.error("Cobblemon Occupied Pokéballs: corrupt ball NBT for player ${player.name.string}")
            player.sendMessage(Text.translatable("cobblemon_occupied_pokeballs.msg.corrupt"), true)
            return
        }

        val storage = Cobblemon.storage
        val party   = storage.getParty(player)
        val added   = party.add(pokemon)

        if (added) {
            player.sendMessage(
                Text.translatable("cobblemon_occupied_pokeballs.msg.reclaimed_party", pokemon.species.translatedName),
                true
            )
        } else {
            val pcAdded = storage.getPC(player).add(pokemon)
            if (!pcAdded) {
                player.sendMessage(
                    Text.translatable("cobblemon_occupied_pokeballs.msg.pc_full", pokemon.species.translatedName),
                    true
                )
                return
            }
            player.sendMessage(
                Text.translatable("cobblemon_occupied_pokeballs.msg.reclaimed_pc", pokemon.species.translatedName),
                true
            )
        }

        // Remove the ball from the player's hand
        BallNbt.clear(stack)
        stack.decrement(1)
    }
}
