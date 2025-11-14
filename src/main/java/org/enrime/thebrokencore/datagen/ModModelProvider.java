package org.enrime.thebrokencore.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import org.enrime.thebrokencore.block.ModBlocks;
import org.enrime.thebrokencore.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // ...
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // ============ Regular items ============
        itemModelGenerator.register(ModItems.TEST_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLOODY_TEAR, Models.GENERATED);
        itemModelGenerator.register(ModItems.POISONOUS_STING, Models.GENERATED);

        // ============ Armor ============
        itemModelGenerator.register(ModItems.ICE_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.ICE_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ICE_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.ICE_BOOTS, Models.GENERATED);

        // ============ Vertical slabs ============
        itemModelGenerator.register(ModBlocks.OAK_VERTICAL_SLAB.asItem());
        itemModelGenerator.register(ModBlocks.STONE_VERTICAL_SLAB.asItem());
        itemModelGenerator.register(ModBlocks.BRICK_VERTICAL_SLAB.asItem());

        // ============ Spawn eggs ============
        itemModelGenerator.registerSpawnEgg(ModItems.STINGRAY_SPAWN_EGG, 0x184D74, 0xFDFDFE);
        itemModelGenerator.registerSpawnEgg(ModItems.BATEYE_SPAWN_EGG, 0xF0E9CF, 0x4A130E);
        itemModelGenerator.registerSpawnEgg(ModItems.CHAINBOUND_SPAWN_EGG, 0x6E391E, 0x8D8B76);
    }
}
