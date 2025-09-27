package org.enrime.thebrokencore.magic_system;

import org.enrime.thebrokencore.magic_system.parameters.Parameter;
import org.enrime.thebrokencore.magic_system.parameters.elements.*;
import org.enrime.thebrokencore.magic_system.parameters.forms.*;
import org.enrime.thebrokencore.util.MathHelper;

import java.util.*;
import java.util.stream.Collectors;

public class SpellSystem {
    public static final int DEFAULT_SLOT_COUNT = 5;
    private final List<Slot> slots;

    private final List<Parameter> generalParameters;
    private final List<Form> forms;
    private final List<Element> elements;

    private final List<Threshold> formThresholds;

    private boolean isCaching;
    private final Map<SpellInput, SpellResult> cache;

    private SpellSystem(Builder builder) {
        this.generalParameters = builder.generalParameters;
        this.elements = builder.elements;
        this.forms = builder.forms;
        this.slots = new ArrayList<>();
        this.formThresholds = builder.formThresholds;

        for (int i = 0; i < builder.slotCount; i++) {
            slots.add(new Slot());
        }

        cache = new HashMap<>();
        isCaching = true;
    }

    /**
     * <b>The main method of this class API, intended for accessing CORE logic of the magic system.</b>
     * <p>
     * Calculates SpellResult based on a given SpellInput if not utilizing a cache hit<br>
     * If isCaching == true, saves and reuses results for all spellInputs.
     * <p>
     * For the calculation algorithm, see {@link #calculateSpellResult(SpellInput)} (not a pleasant read)
     * @param spellInput - encapsulates a spell as a sequence of elements
     * @return an object representing the effect of given SpellInput
     */
    public SpellResult evaluateSpellInput(SpellInput spellInput){
        if(!isCaching) return calculateSpellResult(spellInput);
        if(cache.containsKey(spellInput)){
            return cache.get(spellInput);
        }
        else{
            SpellResult spellResult = calculateSpellResult(spellInput);
            cache.put(spellInput, spellResult);
            return spellResult;
        }
    }

    /**
     * Convenience method, overloading {@link #evaluateSpellInput(SpellInput)} with List<String>
     * @param spellInputAsStrings - represents a spell as a sequence of elements, converted to SpellInput
     * @return result of a {@link #evaluateSpellInput(SpellInput)} call
     */
    public SpellResult evaluateSpellInput(List<String> spellInputAsStrings) {
        List<Element> elementList = spellInputAsStrings.stream()
                .map(Element::getElementByName)
                .toList();
        return evaluateSpellInput(new SpellInput(elementList));
    }

    /**
     * Enables caching in {@link #evaluateSpellInput(SpellInput)}, if disabled. Otherwise, does nothing. Doesn't clean cache
     */
    public void enableCaching(){
        isCaching = true;
    }

    /**
     * Disables caching in {@link #evaluateSpellInput(SpellInput)}, if enabled. Otherwise, does nothing. Doesn't clean cache
     */
    public void disableCaching(){
        isCaching = false;
    }

    /**
     * Cleans SpellInput -> SpellResult mapping cache
     */
    public void cleanCache(){
        cache.clear();
    }

    public List<Parameter> getGeneralParameters() {
        return Collections.unmodifiableList(generalParameters);
    }

    public List<Form> getForms() {
        return Collections.unmodifiableList(forms);
    }

    public List<Element> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public void setParameterCoefficientOnSlot(int slotNumber, Parameter parameter, double value) {
        slots.get(slotNumber).setCoefficient(parameter, value);
        cleanCache();
    }

    public void setElementCoefficientOnSlot(int slotNumber, double value) {
        slots.get(slotNumber).setElementCoefficient(value);
        cleanCache();
    }

    public double getElementCoefficientOnSlot(int slotNumber) {
        return slots.get(slotNumber).getElementCoefficient();
    }

    public void setFormCoefficientOnSlot(int slotNumber, double value) {
        slots.get(slotNumber).setFormCoefficient(value);
        cleanCache();
    }

    public double getFormCoefficientOnSlot(int slotNumber) {
        return slots.get(slotNumber).getFormCoefficient();
    }

    public void setParameterCoefficientOnSlot(int slotNumber, String parameterName, double value) {
        slots.get(slotNumber).setCoefficient(parameterName, value);
        cleanCache();
    }

    public double getParameterCoefficientOnSlot(int slotNumber, Parameter parameter) {
        return slots.get(slotNumber).getCoefficient(parameter);
    }

    public double getParameterCoefficientOnSlot(int slotNumber, String parameterName) {
        return slots.get(slotNumber).getCoefficient(parameterName);
    }

    public int getSlotCount() {
        return slots.size();
    }

    /**
     * Calculates the final effect ({@link SpellResult}) of casting a spell.
     *
     * <h2>System Overview</h2>
     * The spell system has {@code slotCount} available slots. A spell is defined by a sequence of
     * {@link Element} instances placed into these slots (provided via {@link SpellInput}).
     *
     * <h2>Key Concepts</h2>
     * <ul>
     *   <li><b>Slot Properties:</b>
     *     <ul>
     *       <li><i>Form Coefficient:</i> A multiplier applied to a specific {@link Form} (e.g., Fireball, Shield).</li>
     *       <li><i>Element Coefficient:</i> A multiplier applied to a specific {@link Element} (e.g., Fire, Water).</li>
     *       <li><i>Parameter Coefficients:</i> A set of multipliers, one for each general spell parameter (e.g., Damage, Speed).</li>
     *     </ul>
     *   </li>
     *   <li><b>Element Properties:</b> Each {@link Element} provides base values that are scaled by the slot's coefficients.
     *     <ul>
     *       <li><i>Form Impact:</i> A value this element contributes to each possible {@link Form}.</li>
     *       <li><i>General Parameter Impact:</i> A value this element contributes to each general spell parameter.</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <h2>Calculation Process</h2>
     * The method calculates three sets of values to determine the result:
     *
     * <h3>1. Form Power (for each Form)</h3>
     * This determines which form the spell takes.
     * Form power for a given form FORM_X is calculated like so:
     *      <ol>
     *          <li>Multiplying FORM_X coefficients in each slot and FORM_X impact in elements in this slot</li>
     *          <li>Adding multiplication result in all these slots together</li>
     *      </ol>
     * <h3>2. Element Power (for each Element)</h3>
     * This determines the dominant element of the spell.
     * Element impact for a given element ELEMENT_X is calculated like so:<br>
     * ELEMENT_X coefficient on each slot whit ELEMENT_X as element are added together
     * <p>
     *
     * <h3>3. Parameter Power (for each Parameter)</h3>
     * These are the final values for the spell's effects (e.g., how much Damage it does).
     * General parameter power for a given parameter PARAMETER_X is calculated like so:
     *      <ol>
     *          <li>Multiplying PARAMETER_X coefficients in each slot and PARAMETER_X impact in elements in this slot</li>
     *          <li>Adding multiplication result in all these slots together</li>
     *      </ol>
     *
     * <h2>Determining the Result</h2>
     * The {@link SpellResult} is built as follows:
     * <ul>
     *   <li><b>Parameters:</b> The calculated Parameter Power Values become the final spell effects.</li>
     *   <li><b>Element:</b> The element with the highest Element Power is chosen. A tie results in an undefined element.</li>
     *   <li><b>Primary Form:</b> The form with the highest Form Power that can be primary is chosen. A tie results in an undefined form.</li>
     *   <li><b>Secondary Forms:</b> Additional forms are selected based on the primary form's power and a list of predefined thresholds.
     *       <ol>
     *         <li>Thresholds are processed in ascending order of their power value.</li>
     *         <li>For each threshold which is less or equal to the primary form's power:
     *           <ul>
     *             <li>Form Power is <i>recalculated</i>, considering only the slots specified by the threshold's pattern.</li>
     *             <li>The most powerful form from this recalculated set that can be a secondary form is selected.</li>
     *             <li>Each selected form is added to the list of secondary forms.</li>
     *           </ul>
     *         </li>
     *       </ol>
     *     This mechanism allows powerful spells to manifest additional effects based on specific slot combinations.
     *     </li>
     *   </ul>
     * </ul>
     *
     * @param spell the definition of the spell to calculate, consisting of a sequence of elements
     * @return a SpellResult object representing the calculated effects of the spell
     */
    private SpellResult calculateSpellResult(SpellInput spell) {
        Map<Parameter, Double> generalParametersPower = calculateGeneralParametersPower(spell);
        Map<Form, Double> formsPower = calculateFormsPower(spell);
        Map<Element, Double> elementsPower = calculateElementsPower(spell);

        List<Map.Entry<Form, Double>> sortedForms = MathHelper.sortByValue(formsPower);
        List<Map.Entry<Element, Double>> sortedElements = MathHelper.sortByValue(elementsPower);

        Element element = sortedElements.getFirst().getKey();

        Map.Entry<Form, Double> primaryFormEntry = pickPrimaryForm(sortedForms);
        if (primaryFormEntry == null) return new SpellResult.Builder().buildInvalid();

        Form primaryForm = primaryFormEntry.getKey();
        double primaryFormImpact = primaryFormEntry.getValue();

        List<Form> secondaryForms = pickSecondaryForms(spell, primaryFormImpact);

        return new SpellResult.Builder()
                .withElement(element)
                .withPrimaryForm(primaryForm)
                .withParameters(generalParametersPower)
                .withSecondaryForms(secondaryForms)
                .build();
    }

    private Map<Parameter, Double> calculateGeneralParametersPower(SpellInput spell){
        Map<Parameter, Double> generalParameterValues = new HashMap<>();

        for (Parameter generalParameter : generalParameters) {
            generalParameterValues.put(generalParameter, 0.0);
        }

        for (int slotIndex = 0; slotIndex < spell.size(); slotIndex++) {
            Element currentElement = spell.get(slotIndex);
            for (Parameter generalParameter : generalParameters) {
                Double product = getParameterCoefficientOnSlot(slotIndex, generalParameter)
                        * currentElement.getGeneralParameterImpact(generalParameter);
                generalParameterValues.merge(generalParameter, product, Double::sum);
            }
        }

        return generalParameterValues;
    }

    private Map<Form, Double> calculateFormsPower(SpellInput spell, Set<Integer> pattern){
        Map<Form, Double> formImpactValues = new HashMap<>();

        for (Form form : forms) {
            formImpactValues.put(form, 0.0);
        }

        for (int slotIndex = 0; slotIndex < spell.size(); slotIndex++) {
            if (!(pattern.contains(slotIndex))) {
                continue;
            }
            Element currentElement = spell.get(slotIndex);
            for (Form form : forms) {
                Double product = getFormCoefficientOnSlot(slotIndex)
                        * currentElement.getFormImpact(form);
                formImpactValues.merge(form, product, Double::sum);
            }
        }

        return formImpactValues;
    }

    private Map<Form, Double> calculateFormsPower(SpellInput spell){
        return calculateFormsPower(spell, getFullPattern(spell));
    }

    private Map<Element, Double> calculateElementsPower(SpellInput spell){
        Map<Element, Double> elementImpactValues = new HashMap<>();

        for (Element element : elements) {
            elementImpactValues.put(element, 0.0);
        }

        for (int slotIndex = 0; slotIndex < spell.size(); slotIndex++) {
            Element currentElement = spell.get(slotIndex);
            elementImpactValues.merge(currentElement, getElementCoefficientOnSlot(slotIndex), Double::sum);
        }

        return elementImpactValues;
    }

    private Map.Entry<Form, Double> pickPrimaryForm(List<Map.Entry<Form, Double>> sortedForms){
        for (var entry : sortedForms) {
            if (entry.getKey().canBePrimary()) {
                return entry;
            }
        }
        return null;
    }

    private List<Form> pickSecondaryForms(SpellInput spell, double primaryFormImpact){
        List<Form> secondaryForms = new ArrayList<>();

        for (Threshold threshold : formThresholds) {
            if (primaryFormImpact < threshold.value()) break;

            Map<Form, Double> formImpactValues = calculateFormsPower(spell, threshold.slotPattern);
            var sortedFormsOnPattern = MathHelper.sortByValue(formImpactValues);

            for (var entry : sortedFormsOnPattern) {
                if (entry.getKey().canBeSecondary()) {
                    secondaryForms.add(entry.getKey());
                    break;
                }
            }
        }

        return secondaryForms;
    }

    private Set<Integer> getFullPattern(SpellInput spell){
        Set<Integer> fullPattern = new HashSet<>();
        for (int index = 0; index < spell.size(); index++) {
            fullPattern.add(index);
        }
        return fullPattern;
    }

    public static class Builder {
        private List<Parameter> generalParameters = new ArrayList<>();
        private List<Form> forms = new ArrayList<>();
        private List<Element> elements = new ArrayList<>();
        private List<Threshold> formThresholds = new ArrayList<>();
        private int slotCount = SpellSystem.DEFAULT_SLOT_COUNT;

        public Builder withGeneralParameters(List<Parameter> generalParameters) {
            this.generalParameters = Objects.requireNonNull(generalParameters, "generalParameters must not be null");
            return this;
        }

        public Builder withForms(List<Form> forms) {
            this.forms = Objects.requireNonNull(forms, "forms must not be null");
            return this;
        }

        public Builder withElements(List<Element> elements) {
            this.elements = Objects.requireNonNull(elements, "elements must not be null");
            return this;
        }

        public Builder withSlotCount(int slotCount) {
            this.slotCount = slotCount;
            return this;
        }

        public Builder withFormThresholds(List<Threshold> formThresholds) {
            this.formThresholds = Objects.requireNonNull(formThresholds, "formThresholds must not be null");
            return this;
        }

        public Builder addThreshold(Threshold threshold) {
            this.formThresholds.add(threshold);
            return this;
        }

        private void sortThresholds() {
            if (this.formThresholds == null) {
                this.formThresholds = new ArrayList<>();
                return;
            }
            this.formThresholds = this.formThresholds.stream()
                    .distinct()
                    .sorted(Comparator.comparingDouble(Threshold::value))
                    .collect(Collectors.toList());
        }

        public SpellSystem build() {
            sortThresholds();
            return new SpellSystem(this);
        }
    }

    public record Threshold(double value, Set<Integer> slotPattern) {
    }

    private class Slot {
        private final Map<Parameter, Double> generalParametersCoefficients;
        private Double formCoefficient;
        private Double elementCoefficient;

        public Double getElementCoefficient() {
            return elementCoefficient;
        }

        public void setElementCoefficient(Double elementCoefficient) {
            this.elementCoefficient = elementCoefficient;
        }

        public Double getFormCoefficient() {
            return formCoefficient;
        }

        public void setFormCoefficient(Double formCoefficient) {
            this.formCoefficient = formCoefficient;
        }

        private Slot() {
            this.generalParametersCoefficients = new HashMap<>();
            this.formCoefficient = 1.0;
            this.elementCoefficient = 1.0;
            for (Parameter param : generalParameters) {
                generalParametersCoefficients.put(param, 1.0);
            }
        }

        public void setCoefficient(Parameter parameter, double value) throws IllegalArgumentException {
            if (!generalParameters.contains(parameter)) {
                throw new IllegalArgumentException("Attempted to set slot coefficient for parameter " + parameter.getName() + ", which doesn't exist in the SpellSystem");
            }
            generalParametersCoefficients.put(parameter, value);
        }

        public void setCoefficient(String parameterName, double value) throws NoSuchElementException, IllegalArgumentException {
            Parameter parameter = Parameter.getParameterByName(parameterName);
            setCoefficient(parameter, value);
        }

        public double getCoefficient(Parameter parameter) throws IllegalArgumentException {
            if (!generalParameters.contains(parameter)) {
                throw new IllegalArgumentException("Attempted to get slot coefficient for parameter " + parameter.getName() + ", which doesn't exist in the SpellSystem");
            }
            return generalParametersCoefficients.getOrDefault(parameter, 1.0);
        }

        public double getCoefficient(String parameterName) throws NoSuchElementException, IllegalArgumentException {
            Parameter parameter = Parameter.getParameterByName(parameterName);
            return getCoefficient(parameter);
        }
    }
}