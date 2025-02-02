package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ArmorRack.EMPTY_ARMOR_RACK_ITEM)
                .pattern(" # ")
                .pattern("#&#")
                .pattern(" # ")
                .input('#', Items.IRON_INGOT)
                .input('&', Items.ARMOR_STAND)
                .criterion(hasItem(Items.ARMOR_STAND), conditionsFromItem(Items.ARMOR_STAND))
                .offerTo(exporter);
    }
}
