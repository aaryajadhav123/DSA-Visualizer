package com.aarya.dsavisualizer.dto;

import java.util.Map;

public class StepDTO {

    private int step;
    private Map<String, Integer> variables;

    public StepDTO(int step, Map<String, Integer> variables) {
        this.step = step;
        this.variables = variables;
    }

    public int getStep() {
        return step;
    }

    public Map<String, Integer> getVariables() {
        return variables;
    }
}