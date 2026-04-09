package com.pokecapsule.config

import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader

object PokeCapsuleConfig {

    private val GSON = GsonBuilder().setPrettyPrinting().create()
    private val configFile = FabricLoader.getInstance().configDir.resolve("cobblemon_occupied_pokeballs.json").toFile()

    var dropPartyOnDeath:  Boolean = false
    var showFloatingName:  Boolean = true

    fun load() {
        if (!configFile.exists()) { save(); return }
        try {
            val data = GSON.fromJson(configFile.readText(), Data::class.java) ?: return
            dropPartyOnDeath = data.dropPartyOnDeath
            showFloatingName = data.showFloatingName
        } catch (_: Exception) { /* keep defaults on corrupt file */ }
    }

    fun save() {
        configFile.parentFile?.mkdirs()
        configFile.writeText(GSON.toJson(Data(dropPartyOnDeath, showFloatingName)))
    }

    private data class Data(
        val dropPartyOnDeath: Boolean = false,
        val showFloatingName: Boolean = true,
    )
}
