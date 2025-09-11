package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;

import java.util.Optional;

public class ModelGenerator extends FabricModelProvider {

	public ModelGenerator(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
//		itemModelGenerator.register(ArmorRack.ARMOR_RACK_ITEM, new Model(Optional.of(new Identifier("builtin/entity")), Optional.empty()));
		itemModelGenerator.register(ArmorRack.EMPTY_ARMOR_RACK_ITEM, new Model(Optional.of(ArmorRack.id("item/armor_rack")), Optional.empty()));
		Models.GENERATED.upload(ArmorRack.FALLBACK_MODEL_ID, TextureMap.layer0(ArmorRack.ARMOR_RACK_ITEM), itemModelGenerator.writer);
	}
}
