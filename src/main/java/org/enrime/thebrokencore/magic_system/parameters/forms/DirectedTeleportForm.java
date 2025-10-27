package org.enrime.thebrokencore.magic_system.parameters.forms;

public class DirectedTeleportForm extends Form {
    private static final DirectedTeleportForm instance = new DirectedTeleportForm();

    @Override
    public String getName(){
        return "Directed Teleport";
    }

    public static DirectedTeleportForm getInstance(){
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

    private DirectedTeleportForm(){};
}
