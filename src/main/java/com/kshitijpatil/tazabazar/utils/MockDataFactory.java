package com.kshitijpatil.tazabazar.utils;

import com.kshitijpatil.tazabazar.api.inventory.Inventory;

import java.time.*;
import java.util.Random;

public class MockDataFactory {
    private static final Random generator = new Random(1234);

    private static int getRandomYear() {
        int[] yearOptions = {2018, 2019, 2020, 2021};
        return yearOptions[generator.nextInt(yearOptions.length)];
    }

    // returns random value between 1 to 12
    private static int getRandomMonth() {
        return generator.nextInt(12) + 1;
    }

    private static int getRandomDayOfMonth(boolean isLeapYear, int month) {
        int maxDate = Month.of(month).length(isLeapYear);
        return generator.nextInt(maxDate) + 1;
    }

    private static OffsetDateTime getRandomCreatedDateTime() {
        var month = getRandomMonth();
        var year = getRandomYear();
        var dayOfMonth = getRandomDayOfMonth(Year.isLeap(year), month);
        var secondsPassed = generator.nextInt(86401);
        var randomTime = LocalTime.MIN.plusSeconds(secondsPassed);
        var randomDate = LocalDate.of(year, month, dayOfMonth);
        var zoneOffset = ZoneOffset.ofHoursMinutes(5, 30);
        return OffsetDateTime.of(randomDate, randomTime, zoneOffset);
    }


    public static Inventory createInventory(int productId) {
        var inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantity(generator.nextInt(50));
        inventory.setCreatedAt(getRandomCreatedDateTime());
        inventory.setModifiedAt(OffsetDateTime.now());
        return inventory;
    }

    public static boolean getRandomBoolean() {
        return generator.nextBoolean();
    }

    public static int getRandomInt(int bound) {
        return generator.nextInt(bound);
    }

    /**
     * Returns a random bounded stock value with probability threshold for
     * returning not available (0). If a random probability is greater than
     * given threshold, 0 is returned.
     */
    public static int getRandomStock(int bound, int notAvailableThreshold) {
        var isAvailableProbability = getRandomInt(100) + 1;
        return isAvailableProbability < notAvailableThreshold ? getRandomInt(bound) + 1 : 0;
    }
}
