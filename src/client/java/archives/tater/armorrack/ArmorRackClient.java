package archives.tater.armorrack;

import archives.tater.armorrack.client.render.item.ArmorRackModelRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;

import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;

public class ArmorRackClient implements ClientModInitializer {
	public static final Identifier ARMOR_RACK_TEXTURE = ArmorRack.id("textures/entity/armorrack.png");
	public static final RenderStateDataKey<Boolean> IS_ARMOR_RACK = RenderStateDataKey.create(() -> ArmorRack.MOD_ID + ":is_armor_rack");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SpecialModelRenderers.ID_MAPPER.put(ArmorRack.id("armor_rack"), ArmorRackModelRenderer.Unbaked.CODEC);

        ItemComponentTooltipProviderRegistry.addFirst(ArmorRack.ARMOR_STAND_ARMOR);
	}

}
