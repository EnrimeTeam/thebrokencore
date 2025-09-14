package org.enrime.thebrokencore.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.ModEntities;
import org.enrime.thebrokencore.item.custom.PoisonousStingItem;
import org.enrime.thebrokencore.item.custom.TestItem;

import java.util.function.Function;

public class ModItems {
    // ============ Regular items ============
    public static final Item TEST_ITEM = registerItem("test_item", TestItem::new, new Item.Settings()
            .rarity(Rarity.EPIC)
            .fireproof());
    public static final Item POISONOUS_STING = registerItem("poisonous_sting", PoisonousStingItem::new, new Item.Settings());

    public static final Item TEST_POTION = registerItem("test_potion", ThrowablePotionItem::new, new Item.Settings());

    // ============ Spawn eggs ============
    public static final Item STINGRAY_SPAWN_EGG = registerSpawnEgg("stingray_spawn_egg", ModEntities.STINGRAY);

    private static Item registerSpawnEgg(String name, EntityType<? extends MobEntity> entityType) {
        var key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TheBrokenCoreMod.MOD_ID, name));
        return Items.register(key, settings -> new SpawnEggItem(entityType, settings));
    }

    private static Item registerItem(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        var key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TheBrokenCoreMod.MOD_ID, name));
        return Items.register(key, factory, settings);
    }

    public static void registerModItems() {
        TheBrokenCoreMod.LOGGER.info("Registering items for " + TheBrokenCoreMod.MOD_ID);
    }
}
