package cz.tul.ppj.service;

import cz.tul.ppj.dao.WeatherDAO;
import cz.tul.ppj.model.Weather;
import cz.tul.ppj.provisioning.DBProvisioner;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

public class WeatherFetcher {

    private final String API_URL = "https://history.openweathermap.org/data/2.5/";

    private static final Logger log = LoggerFactory.getLogger(DBProvisioner.class);

    @Value("${openweather.api.key}")
    private String API_KEY;

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private WeatherDAO weatherDAO;

    //

    public void fetchWeatherDataAndStoreToDatabase() {
        String cityCountry = "London,GB";

        String response = webClientBuilder
                .baseUrl(API_URL)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("history/city")
                        .queryParam("q", cityCountry)
                        .queryParam("appid", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        var weatherReports = JSONStringToWeathers(response);

        for (Weather weatherReport : weatherReports) {
            weatherDAO.create(weatherReport);
        }

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
            weatherReportToAdd.setTimestamp(dt);
            weatherReportToAdd.setCityId(city_id);
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
