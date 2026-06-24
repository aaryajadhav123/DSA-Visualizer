package com.aarya.dsavisualizer.dto;

import java.util.Map;
import java.util.ArrayList;

public class StepDTO {

    private int step;
    private Map<String, Integer> variables;
    private Map<String, ArrayList<Integer>> arrays;

    public StepDTO(
            int step,
            Map<String, Integer> variables,
            Map<String, ArrayList<Integer>> arrays
    ) {
        this.step = step;
        this.variables = variables;
        this.arrays = arrays;
    }

    public Map<String, ArrayList<Integer>> getArrays() {
        return arrays;
    }

    public int getStep() {
        return step;
    }

    public Map<String, Integer> getVariables() {
        return variables;
    }
}