package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import net.minecraft.core.HolderLookup.Provider;

import java.util.concurrent.CompletableFuture;

public class EnglishLangGenerator extends FabricLanguageProvider {
    public EnglishLangGenerator(FabricPackOutput dataOutput, CompletableFuture<Provider> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    public void generateTranslations(Provider wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ArmorRack.ARMOR_RACK_ITEM, "Armor Rack");
        translationBuilder.add(ArmorRack.EMPTY_ARMOR_RACK_ITEM, "Armor Rack");
        translationBuilder.add(ArmorRackEntity.ARMOR_RACK_ENTITY_TRANSLATION, "Armor Rack");
    }
}
