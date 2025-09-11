package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;

public class FlatModelGenerator extends FabricModelProvider {

	public FlatModelGenerator(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        var model = ItemModels.basic(ArmorRack.FLAT_MODEL_ID);
		itemModelGenerator.output.accept(ArmorRack.EMPTY_ARMOR_RACK_ITEM, model);
        itemModelGenerator.output.accept(ArmorRack.ARMOR_RACK_ITEM, model);
	}
}
