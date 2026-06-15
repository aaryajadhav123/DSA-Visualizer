package com.aarya.dsavisualizer.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5175")
public class VisualizerController {

    @GetMapping("/")
    public String home() {
        return "Backend Working!";
    }

    @PostMapping("/visualize")
    public String visualize(@RequestBody String code) {

        System.out.println("Received Code:");
        System.out.println(code);

        return "Code Received Successfully";
    }
}