package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.client.render.item.ArmorRackModelRenderer;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;

public class ModelGenerator extends FabricModelProvider {

	public ModelGenerator(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        var model = ItemModelUtils.specialModel(ArmorRack.id("item/armor_rack_entity"), new ArmorRackModelRenderer.Unbaked());
		itemModelGenerator.itemModelOutput.accept(ArmorRack.EMPTY_ARMOR_RACK_ITEM, model);
        itemModelGenerator.itemModelOutput.accept(ArmorRack.ARMOR_RACK_ITEM, model);
		ModelTemplates.FLAT_ITEM.create(ArmorRack.FLAT_MODEL_ID, TextureMapping.layer0(ArmorRack.ARMOR_RACK_ITEM), itemModelGenerator.modelOutput);
	}
}
