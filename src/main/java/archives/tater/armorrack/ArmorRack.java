package archives.tater.armorrack;

import archives.tater.armorrack.entity.ArmorRackEntity;
import archives.tater.armorrack.item.ArmorRackItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArmorRack implements ModInitializer {
	public static String MOD_ID = "armorrack";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final EntityType<ArmorRackEntity> ARMOR_RACK_ENTITY = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(MOD_ID, "armor_rack"),
			FabricEntityTypeBuilder.<ArmorRackEntity>create(SpawnGroup.MISC, ArmorRackEntity::new).dimensions(EntityType.ARMOR_STAND.getDimensions()).build()
	);

	public static final Item ARMOR_RACK_ITEM = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "armor_rack"), new ArmorRackItem(new FabricItemSettings().maxCount(16)));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		FabricDefaultAttributeRegistry.register(ARMOR_RACK_ENTITY, ArmorRackEntity.createLivingAttributes());
	}
}
