package org.enrime.thebrokencore.magic_system;

import org.enrime.thebrokencore.magic_system.parameters.Parameter;
import org.enrime.thebrokencore.magic_system.parameters.elements.*;
import org.enrime.thebrokencore.magic_system.parameters.forms.*;

import java.util.List;
import java.util.Map;

public class SpellSystemData {
    public static final Parameter DAMAGE = new Parameter("Damage");
    public static final Parameter SPEED = new Parameter("Speed");
    public static final Parameter RADIUS = new Parameter("Radius");
    public static final Parameter DURATION = new Parameter("Duration");

    public static final Element FIRE = new Element("Fire",
            Map.of(
                    AuraForm.getInstance(), 0.1,
                    BallForm.getInstance(), 1.5,
                    ExplosionForm.getInstance(), 1.5),
            Map.of(
                    DAMAGE, 1.5,
                    SPEED, 0.1,
                    RADIUS, 0.5,
                    DURATION, 0.2
            ));
    public static final Element WATER = new Element("Water",
            Map.of(),
            Map.of());
    public static final Element AIR = new Element("Air",
            Map.of(),
            Map.of());
    public static final Element EARTH = new Element("Earth",
            Map.of(),
            Map.of());
    public static final Element END = new Element("End",
            Map.of(),
            Map.of());
    public static final Element CHAOS = new Element("Chaos",
            Map.of(),
            Map.of());

    public static final List<Element> ELEMENTS = List.of(
            FIRE,
            WATER,
            AIR,
            EARTH,
            END,
            CHAOS);

    public static final List<Parameter> PARAMETERS = List.of(
            DAMAGE,
            SPEED,
            RADIUS,
            DURATION);

    public static final List<Form> FORMS = List.of(
            AuraForm.getInstance(),
            BallForm.getInstance(),
            DirectedTeleportForm.getInstance(),
            ExplosionForm.getInstance(),
            RandomTeleportForm.getInstance(),
            RayForm.getInstance(),
            ShieldForm.getInstance()
    );
}
