package com.aarya.dsavisualizer.controller;

import com.aarya.dsavisualizer.dto.StepDTO;
import com.aarya.dsavisualizer.service.VisualizerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VisualizerController {

    private final VisualizerService visualizerService;

    public VisualizerController(VisualizerService visualizerService) {
        this.visualizerService = visualizerService;
    }

    @GetMapping("/")
    public String home() {
        return "Backend Working!";
    }

    @PostMapping("/visualize")
    public List<StepDTO> visualize(@RequestBody String code) {

        return visualizerService.generateSteps(code);

    }
}