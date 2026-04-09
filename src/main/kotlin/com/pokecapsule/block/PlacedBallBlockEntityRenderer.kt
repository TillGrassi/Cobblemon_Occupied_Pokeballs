package com.pokecapsule.block

import com.cobblemon.mod.common.CobblemonEntities
import com.cobblemon.mod.common.client.render.pokeball.PokeBallRenderer
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.item.PokeBallItem
import com.pokecapsule.config.PokeCapsuleConfig
import com.pokecapsule.util.BallNbt
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer.TextLayerType
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.RotationAxis

class PlacedBallBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<PlacedBallBlockEntity> {

    override fun render(
        entity:          PlacedBallBlockEntity,
        tickDelta:       Float,
        matrices:        MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light:           Int,
        overlay:         Int
    ) {
        if (entity.ballStack.isEmpty) return

        val client   = MinecraftClient.getInstance()
        val world    = entity.world as? ClientWorld ?: return
        val pokeBall = (entity.ballStack.item as? PokeBallItem)?.pokeBall ?: return

        val fakeEntity   = EmptyPokeBallEntity(pokeBall, world, CobblemonEntities.EMPTY_POKEBALL)
        val ballRenderer = client.entityRenderDispatcher.getRenderer(fakeEntity) as? PokeBallRenderer ?: return
        val model        = ballRenderer.model
        val texture      = ballRenderer.getTextureLocation(fakeEntity)
        val yaw          = entity.rotation * 360f / 16f

        // --- 3D ball model ---
        // Prefer direct model.render() which renders upright with our own transforms.
        // posableModel is a lateinit var that only initialises when the full renderer pipeline runs,
        // so on the very first frame we fall back to ballRenderer.render() to trigger that init.
        matrices.push()
        matrices.translate(0.5, 0.0, 0.5)
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f))
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw))
        matrices.scale(0.75f, 0.75f, 0.75f)

        try {
            val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(texture))
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, -1)
        } catch (_: UninitializedPropertyAccessException) {
            matrices.pop()
            matrices.push()
            matrices.translate(0.5, 0.0, 0.5)
            ballRenderer.render(fakeEntity, yaw, tickDelta, matrices, vertexConsumers, light)
        }

        matrices.pop()

        // --- Floating name label ---
        if (!PokeCapsuleConfig.showFloatingName) return

        val name  = BallNbt.getCachedName(entity.ballStack)
        val shiny = BallNbt.getCachedShiny(entity.ballStack)

        val label = Text.literal("").also { t ->
            if (shiny) t.append(Text.literal("✦ ").formatted(Formatting.YELLOW))
            t.append(Text.literal(name).formatted(Formatting.WHITE))
        }.asOrderedText()

        val textRenderer = client.textRenderer
        val textWidth    = textRenderer.getWidth(label)

        val x = -textWidth / 2f

        matrices.push()
        matrices.translate(0.5, 0.75, 0.5)
        matrices.multiply(client.entityRenderDispatcher.rotation) // billboard: always face camera
        matrices.scale(0.015f, -0.015f, 0.015f)

        // Draw background + text (SEE_THROUGH renders faintly through blocks, NORMAL renders solid on top)
        val bgColor = (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255f).toInt() shl 24
        textRenderer.draw(label, x, 0f, 0x20FFFFFF, false, matrices.peek().positionMatrix, vertexConsumers, TextLayerType.SEE_THROUGH, bgColor, light)
        textRenderer.draw(label, x, 0f, 0xFFFFFF,   false, matrices.peek().positionMatrix, vertexConsumers, TextLayerType.NORMAL,      0,       light)

        matrices.pop()
    }
}
