package archives.tater.armorrack;

import archives.tater.armorrack.datagen.ARRecipeGenerator;
import archives.tater.armorrack.datagen.EnglishLangGenerator;
import archives.tater.armorrack.datagen.FlatModelGenerator;
import archives.tater.armorrack.datagen.ModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ArmorRackDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModelGenerator::new);
		pack.addProvider(EnglishLangGenerator::new);
		pack.addProvider(ARRecipeGenerator.Provider::new);
        
        var flatPack = fabricDataGenerator.createBuiltinResourcePack(ArmorRack.FLAT_RESOURCE_PACK_ID);
        flatPack.addProvider(FlatModelGenerator::new);
	}
}
