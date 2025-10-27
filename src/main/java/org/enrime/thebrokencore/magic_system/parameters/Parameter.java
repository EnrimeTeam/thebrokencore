package org.enrime.thebrokencore.magic_system.parameters;

import java.util.*;
import java.util.stream.Collectors;

public class Parameter {
    private static final Map<String, Parameter> takenNamesToInstances = new HashMap<>();
    private final String name;

    public static Set<String> getTakenNames(){
        return takenNamesToInstances.keySet();
    }

    public static Parameter getParameterByName(String name) throws IllegalArgumentException{
        if(!takenNamesToInstances.containsKey(name)){
            throw new IllegalArgumentException("Attempted to use getParameterByName with a free name: " + name);
        }
        return takenNamesToInstances.get(name);
    }

    public Parameter(String name) {
        this.name = name;
        if(takenNamesToInstances.containsKey(getName())){
            throw new IllegalStateException("Ambiguous Parameter naming: name " + getName() + " has been taken multiple times");
        }
        takenNamesToInstances.put(getName(), this);
    }

    public String getName(){
        return name;
    }
}
