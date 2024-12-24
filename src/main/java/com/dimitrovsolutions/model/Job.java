package com.dimitrovsolutions.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Main entity representing a job listing.
 */
public record Job(String title, String url, LocalDateTime localDate) {

    public boolean olderThanThreeMonths() {
        return Duration.between(localDate, LocalDateTime.now()).toDays() > 90;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(title(), job.title()) && Objects.equals(url(), job.url());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title(), url());
    }

    @Override
    public String toString() {
        return "Job{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", localDate=" + localDate +
                '}';
    }
}