package archives.tater.armorrack;

import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ArmorRackClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_ARMOR_RACK_LAYER = new EntityModelLayer(ArmorRack.id("armor_rack"), "main");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererRegistry.register(ArmorRack.ARMOR_RACK_ENTITY, ArmorRackEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MODEL_ARMOR_RACK_LAYER, ArmorStandEntityModel::getTexturedModelData);

		ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(ArmorRack.FALLBACK_MODEL_ID));

		DynamicItemRenderer itemRenderer = (stack, mode, matrices, vertexConsumers, light, overlay) -> {
			matrices.push();
			matrices.translate(0.5f, 0f, 0.5f);
			matrices.scale(-1, 1, -1);
			ArmorRackItemRenderer.renderArmorRack(stack, matrices, vertexConsumers, light, overlay);
			matrices.pop();
		};
		BuiltinItemRendererRegistry.INSTANCE.register(ArmorRack.EMPTY_ARMOR_RACK_ITEM, itemRenderer);
		BuiltinItemRendererRegistry.INSTANCE.register(ArmorRack.ARMOR_RACK_ITEM, itemRenderer);
	}

}
