package com.pokecapsule

import com.pokecapsule.config.PokeCapsuleConfig
import com.pokecapsule.event.DeathHandler
import com.pokecapsule.event.EjectBallPayload
import com.pokecapsule.event.EjectHandler
import com.pokecapsule.event.ReclaimBallPayload
import com.pokecapsule.event.ReclaimHandler
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import org.slf4j.LoggerFactory

object PokeCapsuleMod : ModInitializer {

    const val MOD_ID = "cobblemon_occupied_pokeballs"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        LOGGER.info("Cobblemon Occupied Pokéballs initialising…")
        PokeCapsuleConfig.load()
        PayloadTypeRegistry.playC2S().register(EjectBallPayload.ID, EjectBallPayload.CODEC)
        PayloadTypeRegistry.playC2S().register(ReclaimBallPayload.ID, ReclaimBallPayload.CODEC)
        EjectHandler.registerServerReceiver()
        ReclaimHandler.registerServerReceiver()
        DeathHandler.register()
        LOGGER.info("Cobblemon Occupied Pokéballs ready.")
    }
}
