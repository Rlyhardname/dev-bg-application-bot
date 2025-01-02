package com.dimitrovsolutions.model;

import com.dimitrovsolutions.util.LoggerUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JobTest {
    @Test
    void isOlderThanThreeMonths_edgeCaseNinetyDays_returnsFalse() {
        // Given
        LocalDateTime nowMinusNinetyDays = LocalDateTime.now().minusDays(90);
        Job job = new Job(
                "Janitor", "www.janitor.com/janitor/?id=1111", nowMinusNinetyDays);

        // When
        boolean result = job.olderThanThreeMonths();

        // Then
        assertFalse(result);
    }

    @Test
    void isOlderThanThreeMonths_ninetyOneDays_returnsTrue() {
        // Given
        LocalDateTime nowMinusNinetyDays = LocalDateTime.now().minusDays(91);
        Job job = new Job(
                "Janitor", "www.janitor.com/janitor/?id=1111", nowMinusNinetyDays);

        // When
        boolean result = job.olderThanThreeMonths();

        // Then
        assertTrue(result);
    }
}