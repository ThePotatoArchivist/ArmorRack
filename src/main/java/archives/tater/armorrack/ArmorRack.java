package archives.tater.armorrack;

import archives.tater.armorrack.entity.ArmorRackEntity;
import archives.tater.armorrack.item.ArmorRackItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArmorRack implements ModInitializer {
	public static String MOD_ID = "armorrack";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static final EntityType<ArmorRackEntity> ARMOR_RACK_ENTITY = Registry.register(
			Registries.ENTITY_TYPE,
			id("armor_rack"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, ArmorRackEntity::new).dimensions(EntityType.ARMOR_STAND.getDimensions()).build()
	);

	public static final Item EMPTY_ARMOR_RACK_ITEM = Registry.register(Registries.ITEM, id("empty_armor_rack"), new ArmorRackItem(new FabricItemSettings().maxCount(16)));
	public static final Item ARMOR_RACK_ITEM = Registry.register(Registries.ITEM, id("armor_rack"), new ArmorRackItem(new FabricItemSettings().maxCount(1)));

	public static final Identifier FALLBACK_MODEL_ID = id("item/armor_rack_fallback");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		FabricDefaultAttributeRegistry.register(ARMOR_RACK_ENTITY, ArmorRackEntity.createLivingAttributes());

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
			content.addAfter(Items.ARMOR_STAND, EMPTY_ARMOR_RACK_ITEM);
		});

        //noinspection OptionalGetWithoutIsPresent
        ResourceManagerHelper.registerBuiltinResourcePack(id("flat"),
				FabricLoader.getInstance().getModContainer(MOD_ID).get(),
				Text.literal("2D Armor Rack"),
				ResourcePackActivationType.NORMAL);
	}
}
