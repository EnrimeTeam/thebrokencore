package org.enrime.thebrokencore.item;

import net.minecraft.item.equipment.*;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.util.ModTags;

import java.util.EnumMap;

public class ModArmorMaterials implements ArmorMaterials {
    static RegistryKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset"));

    public static final RegistryKey<EquipmentAsset> ICE_KEY = RegistryKey.of(REGISTRY_KEY, Identifier.of(TheBrokenCoreMod.MOD_ID, "ice"));
    public static final ArmorMaterial ICE = new ArmorMaterial(
            7, /* Durability */
            Util.make(new EnumMap<>(EquipmentType.class), map -> {
                map.put(EquipmentType.BOOTS, 2); /* Element protection */
                map.put(EquipmentType.LEGGINGS, 4);
                map.put(EquipmentType.CHESTPLATE, 6);
                map.put(EquipmentType.HELMET, 2);
                map.put(EquipmentType.BODY, 4);
            }),
            20, /* Enchantibility */
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, /* Equip sound */
            0.0f, /* Toughness */
            0.0f, /* Knockback resistance */
            ModTags.Items.REPAIRS_ICE_ARMOR,
            ICE_KEY);
}
