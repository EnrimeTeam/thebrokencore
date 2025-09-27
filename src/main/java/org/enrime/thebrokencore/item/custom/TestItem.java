package org.enrime.thebrokencore.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.enrime.thebrokencore.magic_system.SpellResult;
import org.enrime.thebrokencore.magic_system.SpellSystemData;
import org.enrime.thebrokencore.magic_system.parameters.forms.VanillaFireballForm;
import org.enrime.thebrokencore.util.MathHelper;

import java.util.List;

public class TestItem extends Item {
    private SpellResult spellResult;

    public TestItem(Settings settings) {
        super(settings);
        spellResult = new SpellResult.Builder()
                .withElement(SpellSystemData.FIRE)
                .withPrimaryForm(VanillaFireballForm.getInstance())
                .build();
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        spellResult.apply(world, user, MathHelper.fromPitchYawDegrees(user.getPitch(), user.getYaw()));
        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.thebrokencore.test_item.tooltip.line1"));
        tooltip.add(Text.translatable("item.thebrokencore.test_item.tooltip.line2", stack.getCount()));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
