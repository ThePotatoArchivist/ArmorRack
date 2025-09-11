package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

import java.util.concurrent.CompletableFuture;

public class EnglishLangGenerator extends FabricLanguageProvider {
    public EnglishLangGenerator(FabricDataOutput dataOutput, CompletableFuture<WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    public void generateTranslations(WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ArmorRack.ARMOR_RACK_ITEM, "Armor Rack");
        translationBuilder.add(ArmorRack.EMPTY_ARMOR_RACK_ITEM, "Armor Rack");
        translationBuilder.add(ArmorRack.ARMOR_RACK_ENTITY, "Armor Rack");
    }
}
