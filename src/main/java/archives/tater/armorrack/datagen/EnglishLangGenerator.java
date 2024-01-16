package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class EnglishLangGenerator extends FabricLanguageProvider {
    protected EnglishLangGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ArmorRack.ARMOR_RACK_ITEM, "Armor Rack");
        translationBuilder.add(ArmorRack.EMPTY_ARMOR_RACK_ITEM, "Armor Rack");
        translationBuilder.add(ArmorRack.ARMOR_RACK_ENTITY, "Armor Rack");
    }
}
