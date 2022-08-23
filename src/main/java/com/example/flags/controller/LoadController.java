package com.example.flags.controller;

import com.example.flags.model.TypeImg;
import com.example.flags.service.FlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/load")
@RequiredArgsConstructor
public class LoadController {

    private final FlagsService flagsService;


    @GetMapping
    public void load(
            @RequestParam Set<String> flags,
            @RequestParam String dir,
            @RequestParam TypeImg type
    ) {
        flagsService.getFlagsAndSave(flags, dir, type);
    }
}
