package com.kshitijpatil.tazabazar.api.utils;

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
        inventory.setId(productId);
        inventory.setQuantity(generator.nextInt(50));
        inventory.setCreatedAt(getRandomCreatedDateTime());
        inventory.setModifiedAt(OffsetDateTime.now());
        return inventory;
    }
}
