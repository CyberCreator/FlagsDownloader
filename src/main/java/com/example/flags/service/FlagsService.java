package com.example.flags.service;

import com.example.flags.client.ApiFlagsClient;
import com.example.flags.model.Country;
import com.example.flags.model.TypeImg;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlagsService {

    private final ApiFlagsClient apiFlagsClient;


    @Async
    public void getFlagsAndSave(Set<String> flags, String dir, TypeImg typeImg) {
        var countries = apiFlagsClient.getCountry(flags, typeImg);
        countries
                .parallelStream()
                .forEach(c -> {
                    switch (typeImg) {
                        case png:
                            saveImageInDisk(c, dir);
                            break;
                        case svg:
                            saveSvgInDisk(c, dir);
                            break;
                        default:
                            throw new RuntimeException("Unknown image type");
                    }
                });
    }

    @SneakyThrows
    public void saveImageInDisk(Country country, String dir) {
        try {
            BufferedImage img = ImageIO.read(country.getImageUrl());
            String fullPath = getAndMakeFullPath(country, dir, TypeImg.png);
            File outputFile = new File(fullPath);
            ImageIO.write(img, TypeImg.png.name(), outputFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error save image on disk ");
        }

    }

    @SneakyThrows
    public void saveSvgInDisk(Country country, String dir) {
        String fullPath = getAndMakeFullPath(country, dir, TypeImg.svg);
        FileOutputStream fos = new FileOutputStream(fullPath);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(country.getImageUrl().openStream()))) {
            var buffer = br.lines().collect(Collectors.joining());
            log.info(buffer);

            fos.write(buffer.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            fos.flush();
            fos.close();
        }
    }

    private String getAndMakeFullPath(Country country, String dir, TypeImg typeImg) throws IOException {
        Path path = Paths.get(dir);
        Files.createDirectories(path);
        List<String> saveFullPathFile = Arrays.asList(dir, "/", country.getName(), ".", typeImg.name());
        return String.join("", saveFullPathFile);
    }
}
