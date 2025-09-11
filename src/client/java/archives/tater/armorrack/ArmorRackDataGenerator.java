package archives.tater.armorrack;

import archives.tater.armorrack.datagen.EnglishLangGenerator;
import archives.tater.armorrack.datagen.ModelGenerator;
import archives.tater.armorrack.datagen.RecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ArmorRackDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModelGenerator::new);
		pack.addProvider(EnglishLangGenerator::new);
		pack.addProvider(RecipeGenerator::new);
	}
}
