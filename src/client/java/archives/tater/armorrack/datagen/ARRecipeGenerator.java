package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class ARRecipeGenerator extends RecipeProvider {

    protected ARRecipeGenerator(net.minecraft.core.HolderLookup.Provider registries, RecipeOutput exporter) {
        super(registries, exporter);
    }

    @Override
    public void buildRecipes() {
        shaped(RecipeCategory.DECORATIONS, ArmorRack.EMPTY_ARMOR_RACK_ITEM)
                .pattern(" # ")
                .pattern("#&#")
                .pattern(" # ")
                .input('#', Items.IRON_INGOT)
                .input('&', Items.ARMOR_STAND)
                .criterion(getHasName(Items.ARMOR_STAND), has(Items.ARMOR_STAND))
                .offerTo(output);
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(net.minecraft.core.HolderLookup.Provider wrapperLookup, RecipeOutput recipeExporter) {
            return new ARRecipeGenerator(wrapperLookup, recipeExporter);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
