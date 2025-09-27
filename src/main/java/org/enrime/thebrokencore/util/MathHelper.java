package org.enrime.thebrokencore.util;

import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Map;

public class MathHelper {
    /**
     * Преобразует углы pitch и yaw в градусах в нормализованный вектор направления
     * @param yawDegrees угол поворота вокруг оси Y (в градусах)
     * @param pitchDegrees угол наклона вверх/вниз (в градусах)
     * @return нормализованный вектор направления
     */
    public static Vec3d fromPitchYawDegrees(double pitchDegrees, double yawDegrees) {
        return fromPitchYawRadians(Math.toRadians(pitchDegrees), Math.toRadians(yawDegrees));
    }

    /**
     * Преобразует углы pitch и yaw в радианах в нормализованный вектор направления
     * @param pitchRad угол наклона вверх/вниз (в радианах)
     * @param yawRad угол поворота вокруг оси Y (в радианах)
     * @return нормализованный вектор направления
     */
    public static Vec3d fromPitchYawRadians(double pitchRad, double yawRad) {
        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vec3d(x, y, z);
    }

    public static <K> List<Map.Entry<K, Double>> sortByValue(Map<K, Double> impacts) {
        return impacts.entrySet()
                .stream()
                .sorted(Map.Entry.<K, Double>comparingByValue().reversed())
                .toList();
    }
}
