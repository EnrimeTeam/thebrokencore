package org.enrime.thebrokencore.magic_system.parameters.forms;

public class ShieldForm extends Form {
    private static final ShieldForm instance = new ShieldForm();

    @Override
    public String getName(){
        return "Shield";
    }

    public static ShieldForm getInstance(){
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

    private ShieldForm(){};
}
