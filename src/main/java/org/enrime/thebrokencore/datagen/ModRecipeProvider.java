package org.enrime.thebrokencore.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import org.enrime.thebrokencore.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {
                // ============ Ice Armor ============
                createShaped(RecipeCategory.COMBAT, ModItems.ICE_HELMET)
                        .pattern("BBB")
                        .pattern("B B")
                        .input('B', Items.BLUE_ICE)
                        .criterion(hasItem(Items.BLUE_ICE), conditionsFromItem(Items.BLUE_ICE))
                        .offerTo(exporter);
                createShaped(RecipeCategory.COMBAT, ModItems.ICE_CHESTPLATE)
                        .pattern("B B")
                        .pattern("BBB")
                        .pattern("BBB")
                        .input('B', Items.BLUE_ICE)
                        .criterion(hasItem(Items.BLUE_ICE), conditionsFromItem(Items.BLUE_ICE))
                        .offerTo(exporter);
                createShaped(RecipeCategory.COMBAT, ModItems.ICE_LEGGINGS)
                        .pattern("BBB")
                        .pattern("B B")
                        .pattern("B B")
                        .input('B', Items.BLUE_ICE)
                        .criterion(hasItem(Items.BLUE_ICE), conditionsFromItem(Items.BLUE_ICE))
                        .offerTo(exporter);
                createShaped(RecipeCategory.COMBAT, ModItems.ICE_BOOTS)
                        .pattern("B B")
                        .pattern("B B")
                        .input('B', Items.BLUE_ICE)
                        .criterion(hasItem(Items.BLUE_ICE), conditionsFromItem(Items.BLUE_ICE))
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "TheBrokenCoreRecipeProvider";
    }
}
