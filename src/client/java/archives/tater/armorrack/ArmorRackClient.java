package archives.tater.armorrack;

import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ArmorRackClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_ARMOR_RACK_LAYER = new EntityModelLayer(new Identifier(ArmorRack.MOD_ID, "armor_rack"), "main");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererRegistry.register(ArmorRack.ARMOR_RACK_ENTITY, ArmorRackEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MODEL_ARMOR_RACK_LAYER, ArmorStandEntityModel::getTexturedModelData);
	}

}
