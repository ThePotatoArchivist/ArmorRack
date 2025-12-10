package archives.tater.armorrack;

import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;
import archives.tater.armorrack.client.render.item.ArmorRackModelRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;

import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;

public class ArmorRackClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_ARMOR_RACK_LAYER = new EntityModelLayer(ArmorRack.id("armor_rack"), "main");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererFactories.register(ArmorRack.ARMOR_RACK_ENTITY, ArmorRackEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MODEL_ARMOR_RACK_LAYER, ArmorStandEntityModel::getTexturedModelData);

//		ModelLoadingPlugin.register(pluginContext -> pluginContext.addModel(ExtraModelKey.create(), SimpleUnbakedExtraModel.blockStateModel(ArmorRack.FLAT_MODEL_ID)));

        SpecialModelTypes.ID_MAPPER.put(ArmorRack.id("armor_rack"), ArmorRackModelRenderer.Unbaked.CODEC);

        ComponentTooltipAppenderRegistry.addLast(ArmorRack.ARMOR_STAND_ARMOR);
	}

}
