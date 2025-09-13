package org.enrime.thebrokencore.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.enrime.thebrokencore.TheBrokenCoreMod;
import org.enrime.thebrokencore.effect.custom.MarkEffect;
import org.enrime.thebrokencore.effect.custom.MarkAffectedEffect;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> MARK = registerStatusEffect("mark", new MarkEffect());
    public static final RegistryEntry<StatusEffect> MARK_AFFECTED = registerStatusEffect("mark_affected", new MarkAffectedEffect());

    private static RegistryEntry<StatusEffect> registerStatusEffect(String effectName, StatusEffect statusEffect) {
        var id = Identifier.of(TheBrokenCoreMod.MOD_ID, effectName);
        return Registry.registerReference(Registries.STATUS_EFFECT, id, statusEffect);
    }

    public static void registerModEffects() {
        TheBrokenCoreMod.LOGGER.info("Registering effects for " + TheBrokenCoreMod.MOD_ID);
    }
}
