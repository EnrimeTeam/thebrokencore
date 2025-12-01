package org.enrime.thebrokencore.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.custom.StingrayEntity;
import org.enrime.thebrokencore.entity.custom.TestEntity;

public class ModEntities {
    public static final EntityType<StingrayEntity> STINGRAY = registerEntity("stingray", StingrayEntity::new, SpawnGroup.WATER_AMBIENT, 2.3f, 0.4f);
    public static final EntityType<TestEntity> TEST_ENTITY = registerEntity("test_entity", TestEntity::new, SpawnGroup.MONSTER, 0.33f, 0.33f);

    // TODO: EntityType.Builder can take too many parameters. Pass it as an argument?..
    private static <T extends Entity> EntityType<T> registerEntity(String entityName, EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, float width, float height) {
        var id = Identifier.of(TheBrokenCoreMod.MOD_ID, entityName);
        var key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);
        return Registry.register(
                Registries.ENTITY_TYPE,
                id,
                EntityType.Builder.create(factory, spawnGroup)
                        .dimensions(width, height)
                        .build(key)
        );
    }

    public static void registerMobEntities() {
        TheBrokenCoreMod.LOGGER.info("Registering entities for " + TheBrokenCoreMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(STINGRAY, StingrayEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TEST_ENTITY, TestEntity.createAttributes());
    }
}
