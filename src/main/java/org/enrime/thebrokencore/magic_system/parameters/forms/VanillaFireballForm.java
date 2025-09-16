package org.enrime.thebrokencore.magic_system.parameters.forms;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.enrime.thebrokencore.magic_system.parameters.elements.Element;

import java.util.List;

public class VanillaFireballForm extends Form {
    private static final VanillaFireballForm instance = new VanillaFireballForm();

    @Override
    public String getName(){
        return "Vanilla Fireball";
    }

    @Override
    public void instantiate(Element element, World world, LivingEntity caster,
                            Vec3d position, Vec3d direction, List<Form> remaining) {
        FireballEntity fireballEntity = new FireballEntity(world, caster, direction, 4);
        world.spawnEntity(fireballEntity);

        if (remaining == null || remaining.isEmpty()) {
            return;
        }

        Form firstForm = remaining.getFirst();
        List<Form> nextForms = remaining.subList(1, remaining.size());

        firstForm.instantiate(
                element,
                world,
                caster,
                position,
                direction,
                nextForms
        );
    }

    public static VanillaFireballForm getInstance(){
        return instance;
    }

    @Override
    public boolean canBePrimary(){
        return true;
    }

    @Override
    public boolean canBeSecondary(){
        return false;
    }

    private VanillaFireballForm(){};
}
