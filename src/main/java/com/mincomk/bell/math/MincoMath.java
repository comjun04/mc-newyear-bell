package com.mincomk.bell.math;

import org.bukkit.util.Vector;
import org.joml.Matrix3d;
import org.joml.Vector3d;

public class MincoMath {
    private static Matrix3d rotationMatrix(double theta, RotationAxis axis) {
        switch (axis) {
            case Z:
                return new Matrix3d(
                        Math.cos(theta), -Math.sin(theta), 0,
                        Math.sin(theta), Math.cos(theta), 0,
                        0, 0, 1
                );
            case Y:
                return new Matrix3d(
                        Math.cos(theta), 0, Math.sin(theta),
                        0, 1, 0,
                        -Math.sin(theta), 0, Math.cos(theta)
                );
            case X:
                return new Matrix3d(
                        1, 0, 0,
                        0, Math.cos(theta), -Math.sin(theta),
                        0, Math.sin(theta), Math.cos(theta)
                );
            default:
                throw new IllegalArgumentException("Invalid plane");
        }
    }

    public static Vector getLookVector(float yaw, float pitch) {
        var front = new Vector3d(0, 0, 1);
        var rotation = rotationMatrix(Math.toRadians(yaw), RotationAxis.Y)
                .mul(rotationMatrix(Math.toRadians(pitch), RotationAxis.X));
        var result = rotation.transform(front).normalize();
        return new Vector(result.x, result.y, result.z);
    }

    public static Vector getUpVector(float yaw, float pitch) {
        var up = new Vector3d(0, 1, 0);
        var rotation = rotationMatrix(Math.toRadians(yaw), RotationAxis.Y)
                .mul(rotationMatrix(Math.toRadians(pitch), RotationAxis.X));
        var result = rotation.transform(up).normalize();
        return new Vector(result.x, result.y, result.z);
    }

    public static Vector translateAlongLook(Vector vec, float yaw, float pitch) {
        var front = getLookVector(yaw, pitch);
        var up = getUpVector(yaw, pitch);
        var right = up.clone().crossProduct(front);
        var result = front.multiply(vec.getZ()).add(up.multiply(vec.getY())).add(right.multiply(vec.getX()));
        return result;
    }
}
