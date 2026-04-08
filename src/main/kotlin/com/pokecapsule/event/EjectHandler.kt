package com.pokecapsule.event

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.storage.party.PartyStore
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.activestate.InactivePokemonState
import com.pokecapsule.PokeCapsuleMod.LOGGER
import com.pokecapsule.util.BallNbt
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object EjectHandler {

    fun registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(EjectBallPayload.ID) { payload, context ->
            context.server().execute {
                handleEject(context.player(), payload.slot)
            }
        }
    }

    private fun handleEject(player: ServerPlayerEntity, selectedSlot: Int) {
        val party: PartyStore = Cobblemon.storage.getParty(player)
        val pokemon: Pokemon = party.get(selectedSlot.coerceIn(0, 5)) ?: return

        if (pokemon.state !is InactivePokemonState) {
            player.sendMessage(Text.translatable("cobblemon_occupied_pokeballs.msg.pokemon_out"), true)
            return
        }

        val registryManager = player.server!!.registryManager
        val ballStack = ItemStack(pokemon.caughtBall.item())
        BallNbt.writePokemon(ballStack, pokemon, registryManager)

        party.remove(pokemon)

        val inserted = player.inventory.insertStack(ballStack)
        if (!inserted) {
            player.dropItem(ballStack, false)
            LOGGER.warn("Cobblemon Occupied Pokéballs: inventory full for ${player.name.string}, dropping ball on ground.")
        }
    }
}
