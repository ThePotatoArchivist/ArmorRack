package archives.tater.armorrack.datagen;

import archives.tater.armorrack.ArmorRack;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class ARRecipeGenerator extends RecipeProvider {

    protected ARRecipeGenerator(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    public void buildRecipes() {
        shaped(RecipeCategory.DECORATIONS, ArmorRack.EMPTY_ARMOR_RACK_ITEM)
                .pattern(" # ")
                .pattern("#&#")
                .pattern(" # ")
                .define('#', Items.IRON_INGOT)
                .define('&', Items.ARMOR_STAND)
                .unlockedBy(getHasName(Items.ARMOR_STAND), has(Items.ARMOR_STAND))
                .save(output);
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
            return new ARRecipeGenerator(recipes, advancements);
        }

    }
}
