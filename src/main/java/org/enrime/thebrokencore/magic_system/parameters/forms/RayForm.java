package org.enrime.thebrokencore.magic_system.parameters.forms;

public class RayForm extends Form {
    private static final RayForm instance = new RayForm();

    @Override
    public String getName(){
        return "Ray";
    }

    public static RayForm getInstance(){
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

    private RayForm(){};
}
