package org.enrime.thebrokencore.magic_system.parameters.elements;

import org.enrime.thebrokencore.magic_system.parameters.Parameter;
import org.enrime.thebrokencore.magic_system.parameters.forms.*;

import java.util.*;

public class Element{
    private final Map<Form, Double> formImpacts;
    public static final double DEFAULT_FORM_IMPACT = 1.0;

    private final Map<Parameter, Double> generalParameterImpact;
    public static final double DEFAULT_GENERAL_PARAMETER_IMPACT = 1.0;

    private final String name;
    private static final Map<String, Element> takenNamesToInstances = new HashMap<>();

    public Element(String name, Map<Form, Double> formImpacts, Map<Parameter, Double> generalParameterImpact){
        this.name = name;
        if(takenNamesToInstances.containsKey(getName())){
            throw new IllegalStateException("Ambiguous Parameter naming: name " + getName() + " has been taken multiple times");
        }
        takenNamesToInstances.put(getName(), this);
        this.formImpacts = Objects.requireNonNull(formImpacts, "formCoefficients must not be null");
        this.generalParameterImpact = Objects.requireNonNull(generalParameterImpact, "generalParameterCoefficients must not be null");
    }

    public static Set<String> getTakenNames(){
        return takenNamesToInstances.keySet();
    }

    public static Element getElementByName(String name) throws IllegalArgumentException {
        if(!takenNamesToInstances.containsKey(name)){
            throw new IllegalArgumentException("Attempted to use getFormByName with a free name: " + name);
        }
        return takenNamesToInstances.get(name);
    }

    public double getGeneralParameterImpact(String parameterName) throws NoSuchElementException {
        return generalParameterImpact.get(Parameter.getParameterByName(parameterName));
    }

    public double getGeneralParameterImpact(Parameter parameter){
        return generalParameterImpact.getOrDefault(parameter, DEFAULT_GENERAL_PARAMETER_IMPACT);
    }

    public double getFormImpact(String formName) throws NoSuchElementException, IllegalArgumentException {
        Form form = Form.getFormByName(formName);
        return getFormImpact(form);
    }

    public double getFormImpact(Form form){
        return formImpacts.getOrDefault(form, DEFAULT_FORM_IMPACT);
    }

    public String getName(){
        return name;
    }
}
