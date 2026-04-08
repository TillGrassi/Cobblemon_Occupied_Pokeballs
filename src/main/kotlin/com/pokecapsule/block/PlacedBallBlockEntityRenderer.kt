package com.pokecapsule.block

import com.cobblemon.mod.common.CobblemonEntities
import com.cobblemon.mod.common.client.render.pokeball.PokeBallRenderer
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.item.PokeBallItem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
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

        // Prefer direct model.render() which renders upright with our own transforms.
        // posableModel is a lateinit var that only initialises when the full renderer pipeline runs,
        // so on the very first frame we fall back to ballRenderer.render() to trigger that init.
        matrices.push()
        matrices.translate(0.5, 0.0, 0.5)                                            // sit above ground
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f))            // flip right-side up
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw))             // face forward + player yaw
        matrices.scale(0.75f, 0.75f, 0.75f)

        try {
            val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(texture))
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, -1)
        } catch (_: UninitializedPropertyAccessException) {
            // posableModel not yet initialized — call the full renderer pipeline once to initialize it.
            // This only happens before any real PokeBall entity has been rendered in the world.
            matrices.pop()
            matrices.push()
            matrices.translate(0.5, 0.0, 0.5)
            ballRenderer.render(fakeEntity, yaw, tickDelta, matrices, vertexConsumers, light)
        }

        matrices.pop()
    }
}
