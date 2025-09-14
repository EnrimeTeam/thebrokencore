package org.enrime.thebrokencore.magic_system;

import org.enrime.thebrokencore.magic_system.parameters.elements.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellInput {
    private final List<Element> elementList;

    public SpellInput(List<Element> elementList){
        this.elementList = new ArrayList<>(elementList);
    }

    public List<Element> getElementList() {
        return new ArrayList<>(elementList);
    }

    public Element get(int index){
        return elementList.get(index);
    }

    public int size(){
        return elementList.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpellInput that = (SpellInput) o;
        return Objects.equals(elementList, that.elementList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementList);
    }
}
