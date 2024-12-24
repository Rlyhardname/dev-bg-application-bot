package com.dimitrovsolutions.cache;

import com.dimitrovsolutions.model.Job;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CacheTest {
    @Test
    void constructor_nullArguments_throwNullPointerException() {
        // Given
        Class<IllegalArgumentException> expectedException = IllegalArgumentException.class;

        // When // Then
        assertThrows(expectedException, () -> new PersistenceCache(null));
        assertThrows(expectedException, () -> new LoaderCache(null));
    }

    @Nested
    class CacheWithValues {
        static LoaderCache cache;
        static String title = "Beginner Java/Kotlin Developer";

        @BeforeAll
        static void setup() {
            cache = new LoaderCache(Map.of(
                    374531, new Job(
                            "SAP iXp Intern – Backend Developer for Cloud Transformation XSA team",
                            "https://dev.bg/company/jobads/sap-ixp-intern-backend-developer-for-cloud-transformation-xsa-team/",
                            LocalDateTime.now()),
                    380771, new Job(
                            "Beginner Java/Kotlin Developer",
                            "https://dev.bg/company/jobads/markovski-solutions-beginner-java-kotlin-developer/",
                            LocalDateTime.of(2024, 06, 10, 10, 55)),
                    380775, new Job(
                            "Beginner Java/Kotlin Developer",
                            "https://dev.bg/company/jobads/markovski-solutions-beginner-java-kotlin-developer/",
                            LocalDateTime.now())
            ));
        }

        @Test
        void fetchJobsInAscendingOrder_returnsListOfJobsFromFirstAddedToLastAdded() {
            // Given
            Job expected = cache.getCache().get(380771);

            // When
            List<Job> jobs = cache.fetchJobsInAscendingOrder(title);

            // Then
            Job actual = jobs.get(0);
            assertEquals(expected, actual);
        }

        @Test
        void fetchJobsInDescendingOrder_returnsListOfJobsFromLastAddedToFirstAdded() {
            // Given
            Job expected = cache.getCache().get(380775);

            // When
            List<Job> jobs = cache.fetchJobsInDescendingOrder(title);

            // Then
            Job actual = jobs.get(0);
            assertEquals(expected, actual);
        }
    }
}