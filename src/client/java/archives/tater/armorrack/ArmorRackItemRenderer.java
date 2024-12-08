package archives.tater.armorrack;

import archives.tater.armorrack.entity.ArmorRackEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class ArmorRackItemRenderer {

    private static final Map<ItemStack, ArmorRackEntity> CACHE = new WeakHashMap<>();

    private static ArmorRackEntity createRack(ItemStack itemStack, World world) {
        var entity =  new ArmorRackEntity(ArmorRack.ARMOR_RACK_ENTITY, world);
        var nbt = itemStack.getSubNbt("EntityTag");
        if (nbt != null) entity.readNbt(nbt);
        return entity;
    }

    private static ArmorRackEntity getOrCreate(ItemStack itemStack, World world) {
        return CACHE.computeIfAbsent(itemStack, (_stack) -> createRack(itemStack, world));
    }

    public static void renderArmorRack(ItemStack itemStack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var world = MinecraftClient.getInstance().world;
        if (world == null)
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.NONE, false, matrices, vertexConsumers, light, overlay, MinecraftClient.getInstance().getBakedModelManager().getModel(ArmorRack.FALLBACK_MODEL_ID));
        MinecraftClient.getInstance().getEntityRenderDispatcher().render(getOrCreate(itemStack, world), 0, 0, 0, 0, 0, matrices, vertexConsumers, light);
    }
}
