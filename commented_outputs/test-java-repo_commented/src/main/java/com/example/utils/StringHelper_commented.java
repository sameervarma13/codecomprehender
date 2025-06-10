package com.example.utils;

/**
 * Utility class providing methods for common string manipulations, 
 * including normalization, capitalization, emptiness checks, and truncation.
 */

public class StringHelper {
    
/**
 * Normalizes input string by trimming whitespace and converting to lowercase.
 *
 * @param input Input string to normalize
 * @return Normalized string
 */

    public static String normalize(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().toLowerCase();
    }
    
/**
 * Checks if a string is empty or null.
 *
 * @param str String to check
 * @return True if empty or null
 */

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
/**
 * Capitalizes the first letter of a string.
 *
 * @param str Input string
 * @return Capitalized string
 */

    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
/**
 * Truncates a string to a specified maximum length.
 *
 * @param str Input string
 * @param maxLength Maximum length for truncation
 * @return Truncated string with ellipsis if truncated
 */

    public static String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }
}