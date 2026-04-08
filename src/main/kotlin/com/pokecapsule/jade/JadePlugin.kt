package com.pokecapsule.jade

import com.pokecapsule.block.PlacedBallBlock
import com.pokecapsule.block.PlacedBallBlockEntity
import com.pokecapsule.util.BallNbt
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import snownee.jade.api.*
import snownee.jade.api.config.IPluginConfig

@WailaPlugin
class JadePlugin : IWailaPlugin {

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(PlacedBallProvider, PlacedBallBlock::class.java)
    }

    object PlacedBallProvider : IBlockComponentProvider {

        private val UID = Identifier.of("cobblemon_occupied_pokeballs", "placed_ball_info")

        override fun getUid(): Identifier = UID

        override fun appendTooltip(
            tooltip:  ITooltip,
            accessor: BlockAccessor,
            config:   IPluginConfig
        ) {
            val be = accessor.blockEntity as? PlacedBallBlockEntity ?: return
            val stack = be.ballStack
            if (stack.isEmpty) return

            val name   = BallNbt.getCachedName(stack)
            val level  = BallNbt.getCachedLevel(stack)
            val type1  = BallNbt.getCachedType1(stack)
            val type2  = BallNbt.getCachedType2(stack)
            val nature = BallNbt.getCachedNature(stack)
            val gender = BallNbt.getCachedGender(stack)
            val shiny  = BallNbt.getCachedShiny(stack)

            val genderSuffix = if (gender.isNotEmpty()) " $gender" else ""
            tooltip.add(Text.literal("$name$genderSuffix (Lv.$level)").formatted(Formatting.WHITE))

            val typeStr = if (type2.isEmpty()) type1 else "$type1 / $type2"
            tooltip.add(Text.literal(typeStr).formatted(Formatting.GRAY))

            tooltip.add(Text.literal("$nature Nature").formatted(Formatting.GRAY))

            if (shiny) tooltip.add(Text.literal("✦ Shiny").formatted(Formatting.YELLOW))
        }
    }
}
