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

private fun isShiftDown(handle: Long) =
    InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) ||
    InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_SHIFT)

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

                val heldStack   = client.player!!.mainHandStack
                val shiftHeld   = isShiftDown(client.window.handle)
                val holdingBall = heldStack.item is PokeBallItem && BallNbt.hasPokemon(heldStack)

                when {
                    // Shift+T with occupied ball in hand → add to party and send out immediately
                    shiftHeld && holdingBall -> ClientPlayNetworking.send(SendOutFromHandPayload)

                    // T with occupied ball in hand → reclaim into party/PC
                    holdingBall -> ClientPlayNetworking.send(ReclaimBallPayload)

                    // T with no ball (or empty ball) → eject the selected party Pokémon
                    else -> {
                        val selectedSlot = CobblemonClient.storage.selectedSlot
                        ClientPlayNetworking.send(EjectBallPayload(selectedSlot))
                    }
                }
            }
        }
    }
}
