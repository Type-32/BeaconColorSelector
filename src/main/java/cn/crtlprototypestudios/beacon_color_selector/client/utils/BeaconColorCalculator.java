package cn.crtlprototypestudios.beacon_color_selector.client.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BeaconColorCalculator {
    public static String calculateStainedGlassSequence(String desiredHexColor) {
        Color desiredColor = Color.decode(desiredHexColor);
        Color[] stainedGlassColors = getStainedGlassColors();

        List<String> sequence = new ArrayList<>();
        Color currentColor = new Color(255, 255, 255); // Start with white (default color)

        while (!isColorClose(currentColor, desiredColor) && sequence.size() < stainedGlassColors.length) {
            Color bestColor = findBestColor(currentColor, desiredColor, stainedGlassColors);
            if (bestColor == null) {
                break; // No better color found, exit the loop
            }
            currentColor = blendColors(currentColor, bestColor, sequence.size() + 1);
            sequence.add(getColorName(bestColor));
        }

        return String.join(" ", sequence);
    }

    private static Color findBestColor(Color currentColor, Color desiredColor, Color[] stainedGlassColors) {
        Color bestColor = null;
        double minDistance = Double.MAX_VALUE;

        for (Color stainedGlassColor : stainedGlassColors) {
            if (!currentColor.equals(stainedGlassColor)) {
                Color newColor = blendColors(currentColor, stainedGlassColor, 1);
                double distance = calculateColorDistance(newColor, desiredColor);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestColor = stainedGlassColor;
                }
            }
        }

        return bestColor;
    }

    private static Color blendColors(Color currentColor, Color newColor, int step) {
        double ratio = 1.0 / (Math.pow(2, step));
        int red = (int) (currentColor.getRed() * (1 - ratio) + newColor.getRed() * ratio);
        int green = (int) (currentColor.getGreen() * (1 - ratio) + newColor.getGreen() * ratio);
        int blue = (int) (currentColor.getBlue() * (1 - ratio) + newColor.getBlue() * ratio);
        return new Color(red, green, blue);
    }

    private static boolean isColorClose(Color color1, Color color2) {
        double threshold = 10.0;
        double distance = calculateColorDistance(color1, color2);
        return distance <= threshold;
    }

    private static double calculateColorDistance(Color color1, Color color2) {
        int redDiff = color1.getRed() - color2.getRed();
        int greenDiff = color1.getGreen() - color2.getGreen();
        int blueDiff = color1.getBlue() - color2.getBlue();
        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }

    public static Color[] getStainedGlassColors() {
        return new Color[]{
                new Color(255, 255, 255), // White
                new Color(255, 165, 0),   // Orange
                new Color(255, 0, 255),   // Magenta
                new Color(173, 216, 230), // Light Blue
                new Color(255, 255, 0),   // Yellow
                new Color(0, 255, 0),     // Lime
                new Color(255, 192, 203), // Pink
                new Color(128, 128, 128), // Gray
                new Color(211, 211, 211), // Light Gray
                new Color(0, 255, 255),   // Cyan
                new Color(128, 0, 128),   // Purple
                new Color(0, 0, 255),     // Blue
                new Color(139, 69, 19),   // Brown
                new Color(0, 128, 0),     // Green
                new Color(255, 0, 0),     // Red
                new Color(0, 0, 0)        // Black
        };
    }

    public static String getColorName(Color color) {
        // Return the name of the stained glass color based on the RGB values
        if (color.equals(new Color(255, 255, 255))) return "White";
        if (color.equals(new Color(255, 165, 0))) return "Orange";
        if (color.equals(new Color(255, 0, 255))) return "Magenta";
        if (color.equals(new Color(173, 216, 230))) return "Light Blue";
        if (color.equals(new Color(255, 255, 0))) return "Yellow";
        if (color.equals(new Color(0, 255, 0))) return "Lime";
        if (color.equals(new Color(255, 192, 203))) return "Pink";
        if (color.equals(new Color(128, 128, 128))) return "Gray";
        if (color.equals(new Color(211, 211, 211))) return "Light Gray";
        if (color.equals(new Color(0, 255, 255))) return "Cyan";
        if (color.equals(new Color(128, 0, 128))) return "Purple";
        if (color.equals(new Color(0, 0, 255))) return "Blue";
        if (color.equals(new Color(139, 69, 19))) return "Brown";
        if (color.equals(new Color(0, 128, 0))) return "Green";
        if (color.equals(new Color(255, 0, 0))) return "Red";
        if (color.equals(new Color(0, 0, 0))) return "Black";
        return "Unknown";
    }
}
