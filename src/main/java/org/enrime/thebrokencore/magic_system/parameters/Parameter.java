package org.enrime.thebrokencore.magic_system.parameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Parameter {
    private static final Map<String, Parameter> takenNamesToInstances = new HashMap<>();
    private final String name;

    public static List<String> getTakenNames(){
        //Deep copying, to make spoiling takenNamesToInstances data impossible with this getter
        return List.copyOf(takenNamesToInstances.keySet().stream()
                .map(String::new)
                .collect(Collectors.toSet()));
    }

    public static Parameter getParameterByName(String name) throws NoSuchElementException{
        if(!takenNamesToInstances.containsKey(name)){
            throw new NoSuchElementException("Attempted to use getParameterByName with a free name: " + name);
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
