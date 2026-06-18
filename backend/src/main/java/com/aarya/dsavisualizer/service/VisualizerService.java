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

                addStep(steps, variables, stepNumber++);
            }
            else if(line.contains("="))
            {
                String statement = line.replace(";", "");

                String[] parts = statement.split("=");

                String variableName = parts[0].trim();

                String expression = parts[1].trim();

                String operator = "";

                if(expression.contains("+")) {
                    operator = "+";
                }
                else if(expression.contains("-")) {
                    operator = "-";
                }
                else if(expression.contains("*")) {
                    operator = "*";
                }
                else if(expression.contains("/")) {
                    operator = "/";
                }

                if(operator.equals("")) {
                    int value = getValue(expression, variables);

                    variables.put(variableName, value);

                    addStep(steps, variables, stepNumber++);

                    continue;
                }



                String[] operands;

                if(operator.equals("+")) {
                    operands = expression.split("\\+");
                }
                else if(operator.equals("-")) {
                    operands = expression.split("-");
                }
                else if(operator.equals("*")) {
                    operands = expression.split("\\*");
                }
                else {
                    operands = expression.split("/");
                }

                String leftOperand = operands[0].trim();
                String rightOperand = operands[1].trim();

                int leftValue = getValue(leftOperand, variables);
                int rightValue = getValue(rightOperand, variables);

                int result = 0;

                if(operator.equals("+")) {
                    result = leftValue + rightValue;
                }
                else if(operator.equals("-")) {
                    result = leftValue - rightValue;
                }
                else if(operator.equals("*")) {
                    result = leftValue * rightValue;
                }
                else if(operator.equals("/")) {
                    result = leftValue / rightValue;
                }

                variables.put(variableName, result);

                addStep(steps, variables, stepNumber++);

            }
        }

        return steps;
    }

    private int getValue(String operand,
                         Map<String, Integer> variables) {

        if(variables.containsKey(operand)) {
            return variables.get(operand);
        }

        return Integer.parseInt(operand);
    }

    private void addStep(
            List<StepDTO> steps,
            Map<String, Integer> variables,
            int stepNumber
    ) {

        steps.add(
                new StepDTO(
                        stepNumber,
                        new HashMap<>(variables)
                )
        );
    }
}