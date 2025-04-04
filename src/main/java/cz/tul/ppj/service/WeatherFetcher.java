package cz.tul.ppj.service;

import cz.tul.ppj.model.Weather;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

public class WeatherFetcher {

    private final String API_URL = "https://history.openweathermap.org/data/2.5/";

    @Value("${openweather.api.key}")
    private String API_KEY;

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Autowired
    private WebClient.Builder webClientBuilder;

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

        JSONStringToWeathers(response);
    }

    private List<Weather> JSONStringToWeathers(String raw) {
        var result = new ArrayList<Weather>();

        var jsonRoot = new JSONObject(raw);
        var jsonArray = jsonRoot.getJSONArray("list");

        jsonArray.forEach(item -> {
            var obj = (JSONObject) item;

            long dt = obj.getLong("dt");

            var main = obj.getJSONObject("main");
            double temp = main.getDouble("temp");
            double feels_like = main.getDouble("feels_like");
            int pressure = main.getInt("pressure");
            int humidity = main.getInt("humidity");

            var weatherArray = obj.getJSONArray("weather");
            var weatherItem0 = weatherArray.getJSONObject(0);
            String description = weatherItem0.getString("description");

            var toAdd = new Weather();
            toAdd.setTimestamp(dt);
            result.add(toAdd);
        });

        return result;
    }

}
