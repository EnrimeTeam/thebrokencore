package org.enrime.thebrokencore.item.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.enrime.thebrokencore.entity.custom.StingrayEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0F, speed, divergence);
    }

    public static Vec3d fromPitchYaw(double pitch, double yaw) {

        pitch = Math.toRadians(pitch);
        yaw = Math.toRadians(yaw);
        // Вычисляем компоненты вектора с использованием тригонометрии
        double x = -Math.sin(yaw) * Math.cos(pitch);
        double y = -Math.sin(pitch);
        double z = Math.cos(yaw) * Math.cos(pitch);

        return new Vec3d(x, y, z);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        FireballEntity fireballEntity = new FireballEntity(world, user, new Vec3d(1, 0 ,0), 4);
        user.playSound(SoundEvents.BLOCK_ANVIL_FALL, 1.0F, 1.0F);
        fireballEntity.setVelocity(fromPitchYaw(user.getPitch(), user.getYaw()));
        //fireballEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.0f, 0);
        world.spawnEntity(fireballEntity);
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
