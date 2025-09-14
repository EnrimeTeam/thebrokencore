package org.enrime.thebrokencore.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import org.enrime.thebrokencore.entity.ModEntities;
import org.enrime.thebrokencore.entity.custom.StingrayEntity;

public class ModEntitySpawns {
    public static void addSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.OCEAN, BiomeKeys.WARM_OCEAN, BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN),
                SpawnGroup.WATER_AMBIENT, ModEntities.STINGRAY, 7, 1, 30);

        SpawnRestriction.register(ModEntities.STINGRAY, SpawnLocationTypes.IN_WATER,
                Heightmap.Type.OCEAN_FLOOR, StingrayEntity::canSpawn);
    }
}
