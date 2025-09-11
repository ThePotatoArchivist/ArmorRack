package archives.tater.armorrack.client.render.item;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.client.render.ArmorRackEntityCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ArmorRackItemRenderer {
    // Static utility class
    private ArmorRackItemRenderer() {}

    public static void renderArmorRack(ItemStack itemStack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var world = MinecraftClient.getInstance().world;
        if (world == null)
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.NONE, false, matrices, vertexConsumers, light, overlay, MinecraftClient.getInstance().getBakedModelManager().getModel(ArmorRack.FALLBACK_MODEL_ID));
        MinecraftClient.getInstance().getEntityRenderDispatcher().render(ArmorRackEntityCache.getOrCreate(itemStack, world), 0, 0, 0, 0, 0, matrices, vertexConsumers, light);
    }
}
