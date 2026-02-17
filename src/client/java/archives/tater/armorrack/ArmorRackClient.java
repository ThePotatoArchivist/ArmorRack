package archives.tater.armorrack;

import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;
import archives.tater.armorrack.client.render.item.ArmorRackModelRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.armorstand.ArmorStandModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class ArmorRackClient implements ClientModInitializer {
	public static final ModelLayerLocation MODEL_ARMOR_RACK_LAYER = new ModelLayerLocation(ArmorRack.id("armor_rack"), "main");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRenderers.register(ArmorRack.ARMOR_RACK_ENTITY, ArmorRackEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MODEL_ARMOR_RACK_LAYER, ArmorStandModel::createBodyLayer);

//		ModelLoadingPlugin.register(pluginContext -> pluginContext.addModel(ExtraModelKey.create(), SimpleUnbakedExtraModel.blockStateModel(ArmorRack.FLAT_MODEL_ID)));

        SpecialModelRenderers.ID_MAPPER.put(ArmorRack.id("armor_rack"), ArmorRackModelRenderer.Unbaked.CODEC);

        ComponentTooltipAppenderRegistry.addLast(ArmorRack.ARMOR_STAND_ARMOR);
	}

}
