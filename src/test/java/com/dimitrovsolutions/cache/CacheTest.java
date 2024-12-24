package com.dimitrovsolutions.cache;

import com.dimitrovsolutions.model.Job;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {
    @Test
    void cache_constructor_throws_NullPointerException() {
        var expected = NullPointerException.class;

        assertThrows(expected, () -> new PersistenceCache(null));
        assertThrows(expected, () -> new LoaderCache(null));
    }

    @Test
    void fetch_cache_returns_map() {
        Map<Integer, Job> persistenceCache = new PersistenceCache().getCache();
        Map<Integer, Job> loaderCache = new LoaderCache().getCache();

        assertTrue(Objects.nonNull(persistenceCache));
        assertTrue(Objects.nonNull(loaderCache));
    }

    @Nested
    class CacheWithValues {
        static Cache cache;
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
        void fetch_list_of_jobs_with_title_in_ascending_order() {
            Job expected = cache.getCache().get(380771);

            List<Job> jobs = cache.fetchJobsInAscendingOrder(title);
            Job actual = jobs.get(0);

            assertEquals(expected, actual);
        }

        @Test
        void fetch_list_of_jobs_with_title_in_descending_order() {
            Job expected = cache.getCache().get(380775);

            List<Job> jobs = cache.fetchJobsInDescendingOrder(title);
            Job actual = jobs.get(0);

            assertEquals(expected, actual);
        }
    }
}