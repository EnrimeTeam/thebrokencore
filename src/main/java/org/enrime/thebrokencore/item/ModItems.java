package org.enrime.thebrokencore.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.item.custom.TestItem;

import java.util.function.Function;

public class ModItems {
    public static final TestItem TEST_ITEM = (TestItem) registerItem("test_item", TestItem::new, new Item.Settings()
            .rarity(Rarity.EPIC)
            .fireproof());

    private static Item registerItem(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        var key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TheBrokenCoreMod.MOD_ID, name));
        return Items.register(key, factory, settings);
    }

    public static void registerModItems() {
        TheBrokenCoreMod.LOGGER.info("Registering items for " + TheBrokenCoreMod.MOD_ID);
    }
}
