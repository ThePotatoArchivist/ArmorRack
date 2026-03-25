package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;

public class FlatModelGenerator extends FabricModelProvider {

	public FlatModelGenerator(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        var model = ItemModelUtils.plainModel(ArmorRack.FLAT_MODEL_ID);
		itemModelGenerator.itemModelOutput.accept(ArmorRack.EMPTY_ARMOR_RACK_ITEM, model);
        itemModelGenerator.itemModelOutput.accept(ArmorRack.ARMOR_RACK_ITEM, model);
	}
}
