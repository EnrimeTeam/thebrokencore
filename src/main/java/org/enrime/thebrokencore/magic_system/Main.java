package org.enrime.thebrokencore.magic_system;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите названия элементов через пробел (например: Fire Fire Water Air):");
        String input = scanner.nextLine();
        List<String> elementNames = List.of(input.split("\\s+"));

        SpellSystem ss = new SpellSystem.Builder()
                .withGeneralParameters(SpellSystemData.parameters)
                .withForms(SpellSystemData.forms)
                .withElements(SpellSystemData.elements)
                .withFormThresholds(List.of(new SpellSystem.Threshold(0, new HashSet<>())))
                .build();

        SpellResult spellResult = ss.evaluateSpellInput(elementNames);
        System.out.println("\nСозданное заклинание:");
        System.out.println(spellResult.getStatString());

        scanner.close();
    }
}