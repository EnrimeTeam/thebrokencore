package org.enrime.thebrokencore;

import net.fabricmc.api.ModInitializer;
import org.enrime.thebrokencore.block.ModBlocks;
import org.enrime.thebrokencore.entity.ModEntities;
import org.enrime.thebrokencore.item.ModItems;
import org.enrime.thebrokencore.world.gen.ModWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheBrokenCoreMod implements ModInitializer {
    public static final String MOD_ID = "thebrokencore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModEntities.registerMobEntities();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        
        ModWorldGeneration.generateModWorldGen();
    }
}
