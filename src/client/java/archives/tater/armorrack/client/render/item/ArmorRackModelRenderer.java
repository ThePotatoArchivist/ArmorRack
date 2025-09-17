package archives.tater.armorrack.client.render.item;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.client.render.ArmorRackEntityCache;
import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;
import com.mojang.serialization.MapCodec;
import net.minecraft.class_12075;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public class ArmorRackModelRenderer implements SpecialModelRenderer<ArmorStandEntityRenderState> {

    public ArmorRackModelRenderer() {}

    private static final class_12075 UNKNOWN_OBJECT = new class_12075();

    @Override
    public void render(@Nullable ArmorStandEntityRenderState data, ItemDisplayContext displayContext, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, boolean glint) {
        matrices.push();
        matrices.translate(0.5f, 0f, 0.5f);
        matrices.scale(-1, 1, -1);
        MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(data).render(data, matrices, queue, UNKNOWN_OBJECT);
        matrices.pop();
    }

    @Override
    public void collectVertices(Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        var state = new ArmorStandEntityRenderState();
        state.entityType = ArmorRack.ARMOR_RACK_ENTITY;
        ((ArmorRackEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(state)).getModel().getRootPart().collectVertices(matrixStack, vertices);
    }

    @Override
    public @Nullable ArmorStandEntityRenderState getData(ItemStack stack) {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return null;
        return ArmorRackEntityCache.getOrCreate(stack, world);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(BakeContext context) {
            return new ArmorRackModelRenderer();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> getCodec() {
            return CODEC;
        }
    }
}
