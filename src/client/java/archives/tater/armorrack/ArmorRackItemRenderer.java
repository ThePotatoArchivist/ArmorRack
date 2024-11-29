package archives.tater.armorrack;

import archives.tater.armorrack.entity.ArmorRackEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ArmorRackItemRenderer {

//    private static @Nullable ArmorRackEntity RENDERED_ARMOR_RACK = null;
//
//    private static final NbtCompound EMPTY = Util.make(() -> {
//        var compound = new NbtCompound();
//        var armorItems = new NbtList();
//        for (int i = 0; i < 4; i++) {
//            armorItems.add(new NbtCompound());
//        }
//        var handItems = new NbtList();
//        for (int i = 0; i < 2; i++) {
//            handItems.add(new NbtCompound());
//        }
//        compound.put("ArmorItems", armorItems);
//        compound.put("HandItems", handItems);
//        return compound;
//    });

    public static void renderArmorRack(ItemStack itemStack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var world = MinecraftClient.getInstance().world;
        if (world == null)
            MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.NONE, false, matrices, vertexConsumers, light, overlay, MinecraftClient.getInstance().getBakedModelManager().getModel(ArmorRack.FALLBACK_MODEL_ID));
        var entity = new ArmorRackEntity(ArmorRack.ARMOR_RACK_ENTITY, world);
        var nbt = itemStack.getSubNbt("EntityTag");
        if (nbt != null) entity.readNbt(nbt);
        MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0, matrices, vertexConsumers, light);
    }
}
