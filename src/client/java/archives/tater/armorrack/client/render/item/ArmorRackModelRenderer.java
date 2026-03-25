package archives.tater.armorrack.client.render.item;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.client.render.ArmorRackEntityCache;
import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.item.ItemStack;

import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class ArmorRackModelRenderer implements SpecialModelRenderer<ArmorStandRenderState> {

    public ArmorRackModelRenderer() {}

    private static final CameraRenderState CAMERA_STATE = new CameraRenderState();

    @Override
    public void submit(@Nullable ArmorStandRenderState argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        if (argument == null) return;
        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.scale(-1, 1, -1);
        Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(argument).submit(argument, poseStack, submitNodeCollector, CAMERA_STATE);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> vertices) {
        PoseStack matrixStack = new PoseStack();
        var state = new ArmorStandRenderState();
        state.entityType = ArmorRack.ARMOR_RACK_ENTITY;
        ((ArmorRackEntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(state)).getModel().root().getExtentsForGui(matrixStack, vertices);
    }

    @Override
    public @Nullable ArmorStandRenderState extractArgument(ItemStack stack) {
        var world = Minecraft.getInstance().level;
        if (world == null) return null;
        return ArmorRackEntityCache.getOrCreate(stack, world);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<ArmorStandRenderState> {

        public static final MapCodec<ArmorRackModelRenderer.Unbaked> CODEC = MapCodec.unit(new ArmorRackModelRenderer.Unbaked());

        @Override
        public SpecialModelRenderer<ArmorStandRenderState> bake(BakingContext context) {
            return new ArmorRackModelRenderer();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<ArmorStandRenderState>> type() {
            return CODEC;
        }
    }
}
