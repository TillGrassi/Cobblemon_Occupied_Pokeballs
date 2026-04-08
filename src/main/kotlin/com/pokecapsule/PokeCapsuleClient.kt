package com.pokecapsule

import com.cobblemon.mod.common.item.PokeBallItem
import com.pokecapsule.block.ModBlocks
import com.pokecapsule.block.PlacedBallBlockEntityRenderer
import com.pokecapsule.event.KeyBindings
import com.pokecapsule.util.BallNbt
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object PokeCapsuleClient : ClientModInitializer {

    override fun onInitializeClient() {
        KeyBindings.register()
        BlockEntityRendererFactories.register(ModBlocks.PLACED_BALL_ENTITY_TYPE, ::PlacedBallBlockEntityRenderer)
        registerTooltip()
    }

    private fun registerTooltip() {
        ItemTooltipCallback.EVENT.register { stack, context, type, lines ->
            if (stack.item !is PokeBallItem) return@register
            if (!BallNbt.hasPokemon(stack)) return@register

            // Remove the original ball description (catch rate, flavour text, etc.)
            // and replace it entirely with Pokémon info.
            // lines[0] is the item name (our CUSTOM_NAME), keep that.
            if (lines.size > 1) lines.subList(1, lines.size).clear()

            val type1  = BallNbt.getCachedType1(stack)
            val type2  = BallNbt.getCachedType2(stack)
            val nature = BallNbt.getCachedNature(stack)
            val gender = BallNbt.getCachedGender(stack)
            val shiny  = BallNbt.getCachedShiny(stack)
            val key    = KeyBindings.ejectBall.boundKeyLocalizedText

            val typeStr = if (type2.isEmpty()) type1 else "$type1 / $type2"
            lines.add(Text.literal(typeStr).formatted(Formatting.GRAY))
            lines.add(Text.literal("$nature Nature").formatted(Formatting.GRAY))
            if (gender.isNotEmpty()) {
                lines.add(Text.literal(gender).formatted(Formatting.GRAY))
            }
            if (shiny) {
                lines.add(Text.literal("✦ Shiny").formatted(Formatting.YELLOW))
            }
            lines.add(
                Text.literal("").append(key).append(
                    Text.literal(" to reclaim").formatted(Formatting.DARK_GRAY)
                )
            )
        }
    }
}
