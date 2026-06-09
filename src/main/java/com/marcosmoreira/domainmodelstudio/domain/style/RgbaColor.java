package com.marcosmoreira.domainmodelstudio.domain.style;

/**
 * Color propio del dominio visual, independiente de JavaFX.
 */
public record RgbaColor(int red, int green, int blue, double opacity) {

    public RgbaColor {
        validateChannel(red, "red");
        validateChannel(green, "green");
        validateChannel(blue, "blue");
        if (!Double.isFinite(opacity) || opacity < 0.0 || opacity > 1.0) {
            throw new IllegalArgumentException("La opacidad debe estar entre 0.0 y 1.0");
        }
    }

    public static RgbaColor rgb(int red, int green, int blue) {
        return new RgbaColor(red, green, blue, 1.0);
    }

    public static RgbaColor rgba(int red, int green, int blue, double opacity) {
        return new RgbaColor(red, green, blue, opacity);
    }

    public static RgbaColor fromHex(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("El color hexadecimal no puede ser null");
        }
        String normalized = hex.trim();
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        }
        if (normalized.length() != 6) {
            throw new IllegalArgumentException("El color hexadecimal debe tener 6 caracteres RGB");
        }
        int red = Integer.parseInt(normalized.substring(0, 2), 16);
        int green = Integer.parseInt(normalized.substring(2, 4), 16);
        int blue = Integer.parseInt(normalized.substring(4, 6), 16);
        return rgb(red, green, blue);
    }

    public String toHex() {
        return "#" + toHexChannel(red) + toHexChannel(green) + toHexChannel(blue);
    }

    private static void validateChannel(int value, String name) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException("El canal " + name + " debe estar entre 0 y 255");
        }
    }

    private static String toHexChannel(int value) {
        String hex = Integer.toHexString(value).toUpperCase();
        return hex.length() == 1 ? "0" + hex : hex;
    }
}
