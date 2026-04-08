package com.pokecapsule.event

import com.cobblemon.mod.common.client.CobblemonClient
import com.cobblemon.mod.common.item.PokeBallItem
import com.pokecapsule.PokeCapsuleMod.MOD_ID
import com.pokecapsule.util.BallNbt
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object KeyBindings {

    lateinit var ejectBall: KeyBinding
        private set

    fun register() {
        ejectBall = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.$MOD_ID.eject_ball",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_T,
                "key.category.$MOD_ID"
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (ejectBall.wasPressed()) {
                if (client.player == null) continue

                val heldStack = client.player!!.mainHandStack

                if (heldStack.item is PokeBallItem && BallNbt.hasPokemon(heldStack)) {
                    // Holding a ball with a Pokémon inside → reclaim it
                    ClientPlayNetworking.send(ReclaimBallPayload)
                } else {
                    // Eject the Pokémon highlighted in the Cobblemon party overlay
                    val selectedSlot = CobblemonClient.storage.selectedSlot
                    ClientPlayNetworking.send(EjectBallPayload(selectedSlot))
                }
            }
        }
    }
}
