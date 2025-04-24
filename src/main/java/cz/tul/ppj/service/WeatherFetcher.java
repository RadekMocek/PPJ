package cz.tul.ppj.service;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.dto.BoolAndMessage;
import cz.tul.ppj.service.jpa.WeatherService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

public class WeatherFetcher {

    private final String API_URL = "https://history.openweathermap.org/data/2.5/";

    private static final Logger log = LoggerFactory.getLogger(WeatherFetcher.class);

    @Value("${openweather.api.key}")
    private String API_KEY;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private WeatherService weatherService;

    //

    public BoolAndMessage fetchWeatherDataAndStoreToDatabase(String location, long timestamp, int nDays, City city) {
        String logPrefix = "WF/" + location + "/" + timestamp + "/" + nDays + " ::";

        int nHours = 1 + (24 * (nDays - 1));

        log.info("{} Got fetch request for location={}, timestamp={}, nHours={}. Calling OpenWeatherMap API...",
                logPrefix, location, timestamp, nHours);

        String response;
        try {
            response = webClientBuilder
                    .baseUrl(API_URL)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("history/city")
                            .queryParam("q", location)
                            .queryParam("type", "hour")
                            .queryParam("appid", API_KEY)
                            .queryParam("start", timestamp)
                            .queryParam("cnt", nHours)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.info("{} API responded with {}. Done.", logPrefix, e.getMessage());
            return new BoolAndMessage(false, e.getMessage());
        }
        log.info("{} API responded with OK. Parsing the JSON...", logPrefix);

        var weatherReports = getWeatherReportsFromResponse(city, response);
        log.info("{} JSON parsed and got weather reports for {} days. Adding to database...", logPrefix, weatherReports.size());

        weatherService.createBulk(weatherReports);
        log.info("{} Done.", logPrefix);

        return new BoolAndMessage(true, "Downloaded " + weatherReports.size() + " weather reports and added new ones to database.");
    }

    private static ArrayList<Weather> getWeatherReportsFromResponse(City city, String response) {
        var weatherReports = new ArrayList<Weather>();
        var jsonRoot = new JSONObject(response);
        var jsonArray = jsonRoot.getJSONArray("list");
        var arrayLen = jsonArray.length();
        for (int i = 0; i < arrayLen; i += 24) {
            JSONObject obj = jsonArray.getJSONObject(i);
            long dt = obj.getLong("dt");
            var main = obj.getJSONObject("main");
            float temp = main.getFloat("temp");
            float feels_like = main.getFloat("feels_like");
            int pressure = main.getInt("pressure");
            int humidity = main.getInt("humidity");
            var weatherArray = obj.getJSONArray("weather");
            var weatherItem0 = weatherArray.getJSONObject(0);
            String description = weatherItem0.getString("description");
            var weatherReportToAdd = new Weather(dt, city, temp, feels_like, pressure, humidity, description);
            weatherReports.add(weatherReportToAdd);
        }
        return weatherReports;
    }

}
