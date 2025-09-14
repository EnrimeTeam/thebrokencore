package org.enrime.thebrokencore.magic_system.parameters.forms;

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
