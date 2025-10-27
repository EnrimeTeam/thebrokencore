package org.enrime.thebrokencore.magic_system.parameters.forms;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.enrime.thebrokencore.magic_system.parameters.elements.Element;

import java.util.LinkedList;
import java.util.List;

public class AuraForm extends Form {
    private static final AuraForm instance = new AuraForm();

    @Override
    public String getName(){
        return "Aura";
    }

    public static AuraForm getInstance(){
        return instance;
    }

    @Override
    public boolean canBePrimary(){
        return true;
    }

    @Override
    public boolean canBeSecondary(){
        return true;
    }

    private AuraForm(){};
}
