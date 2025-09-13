package org.enrime.thebrokencore.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.entity.ModEntities;
import org.enrime.thebrokencore.item.custom.BloodyTearItem;
import org.enrime.thebrokencore.item.custom.IceArmorItem;
import org.enrime.thebrokencore.item.custom.PoisonousStingItem;
import org.enrime.thebrokencore.item.custom.TestItem;

import java.util.function.Function;

public class ModItems {
    // ============ Regular items ============
    public static final Item TEST_ITEM = registerItem("test_item", TestItem::new, new Item.Settings()
            .rarity(Rarity.EPIC)
            .fireproof());
    public static final Item POISONOUS_STING = registerItem("poisonous_sting", PoisonousStingItem::new, new Item.Settings());
    public static final Item BLOODY_TEAR = registerItem("bloody_tear", BloodyTearItem::new, new Item.Settings());

    // ============ Armor items ============
    public static final Item ICE_HELMET = registerItem("ice_helmet", settings -> new IceArmorItem(ModArmorMaterials.ICE, EquipmentType.HELMET, settings), new Item.Settings());
    public static final Item ICE_CHESTPLATE = registerItem("ice_chestplate", settings -> new IceArmorItem(ModArmorMaterials.ICE, EquipmentType.CHESTPLATE, settings), new Item.Settings());
    public static final Item ICE_LEGGINGS = registerItem("ice_leggings", settings -> new IceArmorItem(ModArmorMaterials.ICE, EquipmentType.LEGGINGS, settings), new Item.Settings());
    public static final Item ICE_BOOTS = registerItem("ice_boots", settings -> new IceArmorItem(ModArmorMaterials.ICE, EquipmentType.BOOTS, settings), new Item.Settings());

    // ============ Spawn eggs ============
    public static final Item STINGRAY_SPAWN_EGG = registerSpawnEgg("stingray_spawn_egg", ModEntities.STINGRAY);
    public static final Item BATEYE_SPAWN_EGG = registerSpawnEgg("bateye_spawn_egg", ModEntities.BATEYE);

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
