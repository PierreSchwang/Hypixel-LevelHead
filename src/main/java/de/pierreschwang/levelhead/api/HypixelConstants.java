package de.pierreschwang.levelhead.api;

/**
 * Constants based on
 * https://github.com/HypixelDev/PublicAPI/blob/master/hypixel-api-core/src/main/java/net/hypixel/api/util/ILeveling.java
 */
public class HypixelConstants {

    public static double BASE = 10_000;
    public static double GROWTH = 2_500;

    /* Constants to generate the total amount of XP to complete a level */
    public static double HALF_GROWTH = 0.5 * GROWTH;

    /* Constants to look up the level from the total amount of XP */
    public static double REVERSE_PQ_PREFIX = -(BASE - 0.5 * GROWTH) / GROWTH;
    public static double REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX;
    public static double GROWTH_DIVIDES_2 = 2 / GROWTH;

    public static double getLevel(double exp) {
        return exp < 0 ? 1 : Math.floor(1 + REVERSE_PQ_PREFIX + Math.sqrt(REVERSE_CONST + GROWTH_DIVIDES_2 * exp));
    }

}