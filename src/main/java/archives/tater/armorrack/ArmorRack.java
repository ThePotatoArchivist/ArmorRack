package archives.tater.armorrack;

import archives.tater.armorrack.entity.ArmorRackEntity;
import archives.tater.armorrack.item.ArmorRackItem;
import archives.tater.armorrack.item.ArmorStandArmorComponent;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

    private static <T extends Entity> EntityType<T> register(Identifier id, EntityType.Builder<T> type) {
        var key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type.build(key));
    }

    private static Item register(Identifier id, Function<Item.Properties, Item> factory, Item.Properties settings) {
        var key = ResourceKey.create(Registries.ITEM, id);
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(settings.setId(key)));
    }

	public static final EntityType<ArmorRackEntity> ARMOR_RACK_ENTITY = register(
			id("armor_rack"),
			EntityType.Builder.of(ArmorRackEntity::new, MobCategory.MISC)
                    .sized(EntityType.ARMOR_STAND.getDimensions().width(), EntityType.ARMOR_STAND.getDimensions().height())
	);

	public static final DataComponentType<ArmorStandArmorComponent> ARMOR_STAND_ARMOR = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			id("armor_stand_armor"),
			DataComponentType.<ArmorStandArmorComponent>builder()
					.persistent(ArmorStandArmorComponent.CODEC)
					.networkSynchronized(ArmorStandArmorComponent.STREAM_CODEC)
					.cacheEncoding()
					.build()
	);

	public static final Item EMPTY_ARMOR_RACK_ITEM = register(
			id("empty_armor_rack"),
            ArmorRackItem::new,
			new Item.Properties()
					.stacksTo(16)
					.component(ARMOR_STAND_ARMOR, ArmorStandArmorComponent.EMPTY));

	public static final Item ARMOR_RACK_ITEM = register(
			id("armor_rack"),
			ArmorRackItem::new,
            new Item.Properties()
					.stacksTo(1)
					.component(ARMOR_STAND_ARMOR, ArmorStandArmorComponent.EMPTY));

	public static final Identifier FLAT_MODEL_ID = id("item/armor_rack");
    public static final Identifier FLAT_RESOURCE_PACK_ID = id("flat");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

        //noinspection DataFlowIssue
        FabricDefaultAttributeRegistry.register(ARMOR_RACK_ENTITY, ArmorRackEntity.createLivingAttributes());

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
			output.insertAfter(Items.ARMOR_STAND, EMPTY_ARMOR_RACK_ITEM);
		});

        ResourceLoader.registerBuiltinPack(FLAT_RESOURCE_PACK_ID,
				FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
				Component.literal("2D Armor Rack"),
				PackActivationType.NORMAL);
	}
}
