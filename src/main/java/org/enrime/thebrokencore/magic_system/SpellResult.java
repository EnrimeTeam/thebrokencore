package org.enrime.thebrokencore.magic_system;

import org.enrime.thebrokencore.magic_system.parameters.Parameter;
import org.enrime.thebrokencore.magic_system.parameters.elements.*;
import org.enrime.thebrokencore.magic_system.parameters.forms.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellResult {
    private boolean validity;
    private final Element element;
    private final Form primaryForm;
    private final List<Form> secondaryForms;
    private final Map<Parameter, Double> parameterValues;

    private SpellResult(Builder builder) {
        this.validity = builder.validity;
        this.element = builder.element;
        this.primaryForm = builder.primaryForm;
        this.secondaryForms = builder.secondaryForms;
        this.parameterValues = builder.parameterValues;
    }

    public Element getElement() {
        return element;
    }

    public Form getPrimaryForm() {
        return primaryForm;
    }

    public List<Form> getSecondaryForms() {
        return List.copyOf(secondaryForms);
    }

    public double getParameterValue(Parameter parameter) {
        return parameterValues.get(parameter);
    }

    public double getParameterValue(String parameterName) {
        return parameterValues.get(Parameter.getParameterByName(parameterName));
    }

    public boolean isValid(){
        return validity;
    }

    public static class Builder {
        private boolean validity = true;
        private Element element;
        private Form primaryForm;
        private List<Form> secondaryForms = new ArrayList<>();
        private Map<Parameter, Double> parameterValues = new HashMap<>();

        public Builder withElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder withPrimaryForm(Form primaryForm) {
            this.primaryForm = primaryForm;
            return this;
        }

        public Builder addSecondaryForm(Form secondaryForm) {
            this.secondaryForms.add(secondaryForm);
            return this;
        }

        public Builder withSecondaryForms(List<Form> secondaryForms) {
            this.secondaryForms = new ArrayList<>(secondaryForms);
            return this;
        }

        public Builder withParameter(Parameter parameter, double value) {
            this.parameterValues.put(parameter, value);
            return this;
        }

        public Builder withParameter(String parameterName, double value) {
            this.parameterValues.put(Parameter.getParameterByName(parameterName), value);
            return this;
        }

        public Builder withParameters(Map<Parameter, Double> parameters) {
            this.parameterValues = new HashMap<>(parameters);
            return this;
        }

        public Builder withValidity(boolean validity){
            this.validity = validity;
            return this;
        }

        public SpellResult build() {
            if (element == null) {
                throw new IllegalStateException("Element must be set");
            }
            if (primaryForm == null) {
                throw new IllegalStateException("Primary form must be set");
            }

            return new SpellResult(this);
        }

        public SpellResult buildInvalid(){
            validity = false;
            return new SpellResult(this);
        }
    }

    public String getStatString() {
        StringBuilder sb = new StringBuilder();

        // Basic spell info
        sb.append("Spell Statistics:\n");
        sb.append(String.format("- Element: %s%n", element.getName()));
        sb.append(String.format("- Primary Form: %s%n", primaryForm.getName()));

        // Secondary forms
        if (secondaryForms.isEmpty()) {
            sb.append("- Secondary Forms: None\n");
        } else {
            sb.append("- Secondary Forms: ");
            sb.append(secondaryForms.stream()
                    .map(Form::getName)
                    .collect(java.util.stream.Collectors.joining(", ")));
            sb.append("\n");
        }

        // Parameters
        if (parameterValues.isEmpty()) {
            sb.append("- Parameters: None\n");
        } else {
            sb.append("- Parameters:\n");
            parameterValues.entrySet().stream()
                    .sorted(Map.Entry.<Parameter, Double>comparingByValue().reversed())
                    .forEach(entry -> sb.append(String.format("  • %s: %.2f%n",
                            entry.getKey().getName(),
                            entry.getValue())));
        }

        return sb.toString();
    }
}