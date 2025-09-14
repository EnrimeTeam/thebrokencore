package org.enrime.thebrokencore.magic_system.parameters.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class Form{
    public abstract boolean canBePrimary();
    public abstract boolean canBeSecondary();
    public abstract String getName();

    private static final Map<String, Form> takenNamesToInstances = new HashMap<>();

    public static List<String> getTakenNames(){
        //Deep copying, to make spoiling takenNamesToInstances data impossible with this getter
        return List.copyOf(takenNamesToInstances.keySet().stream()
                .map(String::new)
                .collect(Collectors.toSet()));
    }

    public static Form getFormByName(String name) throws NoSuchElementException {
        if(!takenNamesToInstances.containsKey(name)){
            throw new NoSuchElementException("Attempted to use getFormByName with a free name: " + name);
        }
        return takenNamesToInstances.get(name);
    }

    protected Form() {
        if(takenNamesToInstances.containsKey(getName())){
            throw new IllegalStateException("Ambiguous Form naming: name " + getName() + " has been taken multiple times");
        }
        takenNamesToInstances.put(getName(), this);
    }
}
