package com.uottawa.seg2105.grp1.mealer.model;

import com.uottawa.seg2105.grp1.mealer.model.Meal;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.List;

/**
 * This class provides search functionality for the application
 */
public class SearchEngine {

    /**
     * Executes a repository query and returns matching offered meals
     * @param name The name of the meal, does not have to be a perfect match (must be a subsequence of a meal's name)
     * @param type The type of meal, must be a perfect match
     * @param cuisine The cuisine, must be a perfect match
     * @param minPriceCents A lower bound to the price in cents of a meal
     * @param maxPriceCents An upper bound to the price in cents of a meal
     * @return All matching meals
     */
    public static List<Meal> searchMeal(String name, String type, String cuisine, int minPriceCents, int maxPriceCents) throws RepositoryRequestException {
        List<Meal> meals = Meal.getOfferedMeals();

        meals.removeIf(meal -> !(isSubsequence(name, meal.getName()) &&
                meal.getType().equals(type) &&
                meal.getCuisine().equals(cuisine) &&
                isBetween(meal.getPrice(), minPriceCents, maxPriceCents)));

        return meals;
    }

    /**
     * Checks if a given string of text is a subsequence in another string of text
     * @param subsequence the subsequence to check
     * @param supersequence the super-sequence
     * @return true if subsequence is a subsequence of supersequence
     */
    public static boolean isSubsequence(String subsequence, String supersequence) {
        if (subsequence.length() == 0)
            return true;

        if (supersequence.length() < subsequence.length())
            return false;

        int supidx = 0;
        int subidx = 0;
        while (subidx < subsequence.length()) {
            char current_subchar = subsequence.charAt(subidx);
            while (current_subchar != supersequence.charAt(supidx++)) { // We keep incrementing the supersequence index until we meet a character that matches
                if (supidx >= supersequence.length()) // If we ran out of characters to check in the super-sequence then it's no match.
                    return false;
            }
            subidx++;
        }

        return subidx == subsequence.length(); // The subsequence was matched if it reached its end
    }

    /**
     * Checks if an integer is between min and max inclusively
     * @param x the integer to check
     * @param min the minimum
     * @param max the maximum
     * @return true if min <= x <= max
     */
    public static boolean isBetween(int x, int min, int max) {
        return min <= x && x <= max;
    }

}
