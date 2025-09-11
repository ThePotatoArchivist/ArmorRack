package archives.tater.armorrack.client.render.item;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.client.render.ArmorRackEntityCache;
import archives.tater.armorrack.mixin.client.EntityRenderDispatcherAccessor;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
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

    @Override
    public void render(@Nullable ArmorStandEntityRenderState data, ItemDisplayContext displayContext, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, boolean glint) {
        matrices.push();
        matrices.translate(0.5f, 0f, 0.5f);
        matrices.scale(-1, 1, -1);
        getRenderer().render(data, matrices, vertexConsumers, light);
        matrices.pop();
    }

    @Override
    public void collectVertices(Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        getRenderer().getModel().getRootPart().collectVertices(matrixStack, vertices);
    }


    private static ArmorStandEntityRenderer getRenderer() {
        return (ArmorStandEntityRenderer) ((EntityRenderDispatcherAccessor) MinecraftClient.getInstance().getEntityRenderDispatcher()).getRenderers().get(ArmorRack.ARMOR_RACK_ENTITY);
    }

    @Override
    public @Nullable ArmorStandEntityRenderState getData(ItemStack stack) {
        var world = MinecraftClient.getInstance().world;
        return world == null ? null : getRenderer().getAndUpdateRenderState(ArmorRackEntityCache.getOrCreate(stack, world), 1f);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(LoadedEntityModels entityModels) {
            return new ArmorRackModelRenderer();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> getCodec() {
            return CODEC;
        }
    }
}
