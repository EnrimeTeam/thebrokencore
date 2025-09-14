package org.enrime.thebrokencore.magic_system.parameters.forms;

public class RandomTeleportForm extends Form {
    private static final RandomTeleportForm instance = new RandomTeleportForm();

    @Override
    public String getName(){
        return "Random Teleport";
    }

    public static RandomTeleportForm getInstance(){
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

    private RandomTeleportForm(){};
}
