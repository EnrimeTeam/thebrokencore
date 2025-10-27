package org.enrime.thebrokencore.magic_system.parameters.forms;

public class BallForm extends Form {
    private static final BallForm instance = new BallForm();

    @Override
    public String getName(){
        return "Ball";
    }

    public static BallForm getInstance(){
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

    private BallForm(){};
}
