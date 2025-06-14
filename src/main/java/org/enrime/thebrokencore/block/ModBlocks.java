package org.enrime.thebrokencore.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.block.custom.VerticalSlabBlock;

import java.util.function.Function;

public class ModBlocks {
    public static final Block OAK_VERTICAL_SLAB = registerBlock("oak_vertical_slab", VerticalSlabBlock::new, Blocks.OAK_SLAB.getSettings());
    public static final Block STONE_VERTICAL_SLAB = registerBlock("stone_vertical_slab", VerticalSlabBlock::new, Blocks.STONE_SLAB.getSettings());
    public static final Block BRICK_VERTICAL_SLAB = registerBlock("brick_vertical_slab", VerticalSlabBlock::new, Blocks.BRICK_SLAB.getSettings());

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        var key = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(TheBrokenCoreMod.MOD_ID, name));
        Block block = Blocks.register(key, factory, settings);
        Items.register(block);
        return block;
    }

    public static void registerModBlocks() {
        TheBrokenCoreMod.LOGGER.info("Registering blocks for " + TheBrokenCoreMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(OAK_VERTICAL_SLAB);
            entries.add(STONE_VERTICAL_SLAB);
            entries.add(BRICK_VERTICAL_SLAB);
        });
    }
}
