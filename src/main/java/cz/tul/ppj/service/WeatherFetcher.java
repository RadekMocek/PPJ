package cz.tul.ppj.service;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.Weather;
import cz.tul.ppj.model.WeatherKey;
import cz.tul.ppj.service.jpa.WeatherService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

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

    public void fetchWeatherDataAndStoreToDatabase() {
        String cityCountry = "London,GB";

        String response = webClientBuilder
                .baseUrl(API_URL)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("history/city")
                        .queryParam("q", cityCountry)
                        .queryParam("appid", API_KEY).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        var weatherReports = JSONStringToWeathers(response);

        weatherService.createBulk(weatherReports);

        log.info("WeatherFetcher :: fetchWeatherDataAndStoreToDatabase OK");
    }

    private List<Weather> JSONStringToWeathers(String raw) {
        var result = new ArrayList<Weather>();

        var jsonRoot = new JSONObject(raw);
        int city_id = jsonRoot.getInt("city_id");

        var jsonArray = jsonRoot.getJSONArray("list");
        jsonArray.forEach(item -> {
            var obj = (JSONObject) item;

            long dt = obj.getLong("dt");

            var main = obj.getJSONObject("main");
            float temp = main.getFloat("temp");
            float feels_like = main.getFloat("feels_like");
            int pressure = main.getInt("pressure");
            int humidity = main.getInt("humidity");

            var weatherArray = obj.getJSONArray("weather");
            var weatherItem0 = weatherArray.getJSONObject(0);
            String description = weatherItem0.getString("description");

            var weatherReportToAdd = new Weather();
            var weatherKey = new WeatherKey();
            weatherReportToAdd.setWeatherKey(weatherKey);
            weatherReportToAdd.setTimestamp(dt);
            City city = new City();
            city.setCityId(city_id);
            weatherReportToAdd.setCity(city);
            weatherReportToAdd.setTemperature(temp);
            weatherReportToAdd.setFeelsLike(feels_like);
            weatherReportToAdd.setPressure(pressure);
            weatherReportToAdd.setHumidity(humidity);
            weatherReportToAdd.setDescription(description);
            result.add(weatherReportToAdd);
        });

        return result;
    }

}
