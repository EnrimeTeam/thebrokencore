package org.enrime.thebrokencore.magic_system;

import org.enrime.thebrokencore.magic_system.parameters.Parameter;
import org.enrime.thebrokencore.magic_system.parameters.elements.*;
import org.enrime.thebrokencore.magic_system.parameters.forms.*;

import java.util.List;
import java.util.Map;

class SpellSystemData {
    public static final Parameter damage = new Parameter("Damage");
    public static final Parameter speed = new Parameter("Speed");
    public static final Parameter radius = new Parameter("Radius");
    public static final Parameter duration = new Parameter("Duration");

    public static final Element fire = new Element("Fire",
            Map.of(
                    AuraForm.getInstance(), 0.1,
                    BallForm.getInstance(), 1.5,
                    ExplosionForm.getInstance(), 1.5),
            Map.of(
                    damage, 1.5,
                    speed, 0.1,
                    radius, 0.5,
                    duration, 0.2
            ));
    public static final Element water = new Element("Water",
            Map.of(),
            Map.of());
    public static final Element air = new Element("Air",
            Map.of(),
            Map.of());
    public static final Element earth = new Element("Earth",
            Map.of(),
            Map.of());
    public static final Element end = new Element("End",
            Map.of(),
            Map.of());
    public static final Element chaos = new Element("Chaos",
            Map.of(),
            Map.of());

    public static final List<Element> elements = List.of(
            fire,
            water,
            air,
            earth,
            end,
            chaos);

    public static final List<Parameter> parameters = List.of(
            damage,
            speed,
            radius,
            duration);

    public static final List<Form> forms = List.of(
            AuraForm.getInstance(),
            BallForm.getInstance(),
            DirectedTeleportForm.getInstance(),
            ExplosionForm.getInstance(),
            RandomTeleportForm.getInstance(),
            RayForm.getInstance(),
            ShieldForm.getInstance()
    );
}
