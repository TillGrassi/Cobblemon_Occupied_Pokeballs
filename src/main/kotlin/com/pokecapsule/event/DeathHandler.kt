package com.pokecapsule.event

import com.cobblemon.mod.common.Cobblemon
import com.pokecapsule.config.PokeCapsuleConfig
import com.pokecapsule.util.BallNbt
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.GameRules

object DeathHandler {

    fun register() {
        ServerLivingEntityEvents.AFTER_DEATH.register { entity, _ ->
            if (entity !is ServerPlayerEntity) return@register
            if (!PokeCapsuleConfig.dropPartyOnDeath) return@register
            if (entity.world.gameRules.getBoolean(GameRules.KEEP_INVENTORY)) return@register

            val party = Cobblemon.storage.getParty(entity)
            val registryManager = entity.server!!.registryManager

            for (i in 0..5) {
                val pokemon = party.get(i) ?: continue

                val ballStack = ItemStack(pokemon.caughtBall.item())
                BallNbt.writePokemon(ballStack, pokemon, registryManager)
                party.remove(pokemon)

                entity.world.spawnEntity(
                    ItemEntity(entity.world, entity.x, entity.y, entity.z, ballStack)
                )
            }
        }
    }
}
