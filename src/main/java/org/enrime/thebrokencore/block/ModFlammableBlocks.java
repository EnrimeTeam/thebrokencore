package org.enrime.thebrokencore.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.TagKey;
import org.enrime.thebrokencore.TheBrokenCoreMod;

public class ModFlammableBlocks {
    private static void registerFlammableBlock(Block block, int burn, int spread) {
        FlammableBlockRegistry.getDefaultInstance().add(block, burn, spread);
    }

    private static void registerFlammableTag(TagKey<Block> tag, int burn, int spread) {
        FlammableBlockRegistry.getDefaultInstance().add(tag, burn, spread);
    }

    public static void registerModFlammableBlocks() {
        TheBrokenCoreMod.LOGGER.info("Registering flammable blocks for " + TheBrokenCoreMod.MOD_ID);

        registerFlammableBlock(ModBlocks.OAK_VERTICAL_SLAB, 5, 20);
    }
}
