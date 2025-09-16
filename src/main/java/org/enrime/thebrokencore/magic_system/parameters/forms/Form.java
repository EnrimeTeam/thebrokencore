package org.enrime.thebrokencore.magic_system.parameters.forms;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.enrime.thebrokencore.magic_system.parameters.elements.Element;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Form{
    public abstract boolean canBePrimary();
    public abstract boolean canBeSecondary();
    public abstract String getName();

    private static final Map<String, Form> takenNamesToInstances = new HashMap<>();

    //TODO: make this an abstract method when actual spell forms will be implemented
    public void instantiate(Element element, World world, LivingEntity caster,
                            Vec3d position, Vec3d direction, List<Form> remaining) {
        if (remaining == null || remaining.isEmpty()) {
            return;
        }

        Form firstForm = remaining.getFirst();
        List<Form> nextForms = remaining.subList(1, remaining.size());

        firstForm.instantiate(
                element,
                world,
                caster,
                position,
                direction,
                nextForms
        );
    }

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
