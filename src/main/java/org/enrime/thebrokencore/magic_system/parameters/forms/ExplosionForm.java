package org.enrime.thebrokencore.magic_system.parameters.forms;

public class ExplosionForm extends Form {
    private static final ExplosionForm instance = new ExplosionForm();

    @Override
    public String getName(){
        return "Explosion";
    }

    public static ExplosionForm getInstance(){
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

    private ExplosionForm(){};
}
