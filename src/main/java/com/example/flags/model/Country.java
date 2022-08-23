package com.example.flags.model;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.URL;

@Setter
@Getter
public class Country {

    private String name;
    private String url;

    @SneakyThrows
    public URL getImageUrl() {
        return new URL(url);

    }
}
