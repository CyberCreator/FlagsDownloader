package com.example.flags.client;

import com.example.flags.model.Country;
import com.example.flags.model.TypeImg;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class ApiFlagsClient {

    private static final String API_URL = "https://restcountries.com/v2/alpha?codes=";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public List<Country> getCountry(Set<String> flags, TypeImg typeImg) {
        var fullUrlPath = API_URL + String.join(",", flags);
        return getDataFromApi(fullUrlPath, typeImg);
    }


    @SneakyThrows
    private List<Country> getDataFromApi(String webURL, TypeImg type) {
        URL url = new URL(webURL);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String buffer = br
                    .lines()
                    .collect(Collectors.joining());

            log.info(buffer);
            JsonNode jsonArray = OBJECT_MAPPER.readValue(buffer, JsonNode.class);

            return IntStream
                    .range(0, jsonArray.size())
                    .mapToObj(jsonArray::get)
                    .map(jsonNode -> {
                        Country country = new Country();
                        country.setName(jsonNode.get("name").textValue());
                        country.setUrl(jsonNode.get("flags").get(type.name()).textValue());
                        return country;
                    })
                    .collect(Collectors.toList());
        }
    }
}
