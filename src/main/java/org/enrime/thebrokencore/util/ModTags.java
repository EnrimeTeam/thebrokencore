package org.enrime.thebrokencore.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;

public class ModTags {
    /*
        * Memo *
        To create and use a tag:
        1. Inside the Blocks or Items class add a new variable:
            public static final TagKey<[Block|Item]> TAG_NAME = createTag("tag_name");
        2. Add required blocks/items into the tag in Mod[Block|Item]TagProvider class
        3. Whether you need to check if the block/item is in the tag use
            .is[In|Of](ModTags.[Block|Item].TAG_NAME)
    */

    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(TheBrokenCoreMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> REPAIRS_ICE_ARMOR = createTag("repairs_ice_armor");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(TheBrokenCoreMod.MOD_ID, name));
        }
    }
}
