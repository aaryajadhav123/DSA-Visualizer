package com.aarya.dsavisualizer.service;

import com.aarya.dsavisualizer.dto.StepDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VisualizerService {

    public List<StepDTO> generateSteps(String code) {

        List<StepDTO> steps = new ArrayList<>();

        Map<String, Integer> variables = new HashMap<>();

        String[] lines = code.split("\n");

        int stepNumber = 1;

        for(String line : lines) {

            line = line.trim();

            if(line.startsWith("int ")) {

                String statement = line.replace("int ", "")
                        .replace(";", "");

                String[] parts = statement.split("=");

                String variableName = parts[0].trim();

                int value = Integer.parseInt(parts[1].trim());

                variables.put(variableName, value);

                steps.add(
                        new StepDTO(
                                stepNumber++,
                                new HashMap<>(variables)
                        )
                );
            }
        }

        return steps;
    }
}