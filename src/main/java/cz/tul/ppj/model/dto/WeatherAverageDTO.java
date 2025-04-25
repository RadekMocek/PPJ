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

        float avg1WeekTemperature,
        float avg1WeekFeelsLike,
        float avg1WeekPressure,
        float avg1WeekHumidity,

        float avg2WeekTemperature,
        float avg2WeekFeelsLike,
        float avg2WeekPressure,
        float avg2WeekHumidity
) {
}
