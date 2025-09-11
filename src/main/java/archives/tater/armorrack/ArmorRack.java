package archives.tater.armorrack;

import archives.tater.armorrack.entity.ArmorRackEntity;
import archives.tater.armorrack.item.ArmorRackItem;
import archives.tater.armorrack.item.ArmorStandArmorComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class ArmorRack implements ModInitializer {
    public static String MOD_ID = "armorrack";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

    private static <T extends Entity> EntityType<T> register(Identifier id, EntityType.Builder<T> type) {
        var key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    private static Item register(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        var key = RegistryKey.of(RegistryKeys.ITEM, id);
        return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
    }

	public static final EntityType<ArmorRackEntity> ARMOR_RACK_ENTITY = register(
			id("armor_rack"),
			EntityType.Builder.create(ArmorRackEntity::new, SpawnGroup.MISC)
                    .dimensions(EntityType.ARMOR_STAND.getDimensions().width(), EntityType.ARMOR_STAND.getDimensions().height())
	);

	public static final ComponentType<ArmorStandArmorComponent> ARMOR_STAND_ARMOR = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			id("armor_stand_armor"),
			ComponentType.<ArmorStandArmorComponent>builder()
					.codec(ArmorStandArmorComponent.CODEC)
					.packetCodec(ArmorStandArmorComponent.PACKET_CODEC)
					.cache()
					.build()
	);

	public static final Item EMPTY_ARMOR_RACK_ITEM = register(
			id("empty_armor_rack"),
            ArmorRackItem::new,
			new Item.Settings()
					.maxCount(16)
					.component(ARMOR_STAND_ARMOR, ArmorStandArmorComponent.EMPTY));

	public static final Item ARMOR_RACK_ITEM = register(
			id("armor_rack"),
			ArmorRackItem::new,
            new Item.Settings()
					.maxCount(1)
					.component(ARMOR_STAND_ARMOR, ArmorStandArmorComponent.EMPTY));

	public static final Identifier FLAT_MODEL_ID = id("item/armor_rack");
    public static final Identifier FLAT_RESOURCE_PACK_ID = id("flat");

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
        ResourceManagerHelper.registerBuiltinResourcePack(FLAT_RESOURCE_PACK_ID,
				FabricLoader.getInstance().getModContainer(MOD_ID).get(),
				Text.literal("2D Armor Rack"),
				ResourcePackActivationType.NORMAL);
	}
}
