package com.pokecapsule.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class ModMenuIntegration : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<Screen> =
        ConfigScreenFactory { parent -> buildScreen(parent) }

    private fun buildScreen(parent: Screen): Screen {
        val builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.translatable("config.cobblemon_occupied_pokeballs.title"))
            .setSavingRunnable(PokeCapsuleConfig::save)

        val category = builder.getOrCreateCategory(
            Text.translatable("config.cobblemon_occupied_pokeballs.category.general")
        )

        category.addEntry(
            builder.entryBuilder()
                .startBooleanToggle(
                    Text.translatable("config.cobblemon_occupied_pokeballs.show_floating_name"),
                    PokeCapsuleConfig.showFloatingName
                )
                .setDefaultValue(true)
                .setTooltip(Text.translatable("config.cobblemon_occupied_pokeballs.show_floating_name.tooltip"))
                .setSaveConsumer { PokeCapsuleConfig.showFloatingName = it }
                .build()
        )

        category.addEntry(
            builder.entryBuilder()
                .startBooleanToggle(
                    Text.translatable("config.cobblemon_occupied_pokeballs.drop_party_on_death"),
                    PokeCapsuleConfig.dropPartyOnDeath
                )
                .setDefaultValue(false)
                .setTooltip(Text.translatable("config.cobblemon_occupied_pokeballs.drop_party_on_death.tooltip"))
                .setSaveConsumer { PokeCapsuleConfig.dropPartyOnDeath = it }
                .build()
        )

        return builder.build()
    }
}
