package cz.tul.ppj.model.dto;

public record WeatherAverageDTO(
        String stateId,
        String cityName,
        int nEvaluatedDays,
        long timestampLast,
        long timestamp1WeekStart,
        long timestamp2WeekStart,

        float lastTemperature,
        float lastFeelsLike,
        int lastPressure,
        int lastHumidity,

        double avg1WeekTemperature,
        double avg1WeekFeelsLike,
        double avg1WeekPressure,
        double avg1WeekHumidity,

        double avg2WeekTemperature,
        double avg2WeekFeelsLike,
        double avg2WeekPressure,
        double avg2WeekHumidity
) {
}
