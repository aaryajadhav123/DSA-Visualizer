package com.aarya.dsavisualizer.service;

import com.aarya.dsavisualizer.dto.StepDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.ArrayList;

@Service
public class VisualizerService {

    public List<StepDTO> generateSteps(String code) {

        List<StepDTO> steps = new ArrayList<>();

        Map<String, Integer> variables = new HashMap<>();
        Map<String, ArrayList<Integer>> arrays = new HashMap<>();

        String[] lines = code.split("\n");

        int stepNumber = 1;
        boolean executeBlock = true;
        boolean prevBool = false;
        int whileStartLine = -1;
        String whileCondition = "";
        int forStartLine = -1;
        String forCondition = "";
        String forUpdate = "";

        int currentLine = 0;

        while(currentLine < lines.length) {

            String line = lines[currentLine];

            line = line.trim();

            if(!executeBlock) {

                if(line.equals("}")) {
                    executeBlock = true;
                }

                currentLine++;

                continue;
            }

            if(line.equals("}") && whileStartLine != -1) {

                if(evaluateCondition(whileCondition, variables, arrays)) {

                    currentLine = whileStartLine;

                    continue;
                }
                else {

                    whileStartLine = -1;
                    whileCondition = "";
                }
            }

            else if(line.equals("}") && forStartLine != -1) {


                // i++
                if(forUpdate.endsWith("++")) {

                    String variable = forUpdate.replace("++", "").trim();

                    variables.put(
                            variable,
                            variables.get(variable) + 1
                    );
                }

// i--
                else if(forUpdate.endsWith("--")) {

                    String variable = forUpdate.replace("--", "").trim();

                    variables.put(
                            variable,
                            variables.get(variable) - 1
                    );
                }

// i += 2
                else if(forUpdate.contains("+=")) {

                    String[] parts = forUpdate.split("\\+=");

                    String variable = parts[0].trim();

                    int value = evaluateExpression(
                            parts[1].trim(),
                            variables,
                            arrays
                    );

                    variables.put(
                            variable,
                            variables.get(variable) + value
                    );
                }

// i -= 2
                else if(forUpdate.contains("-=")) {

                    String[] parts = forUpdate.split("-=");

                    String variable = parts[0].trim();

                    int value = evaluateExpression(
                            parts[1].trim(),
                            variables,
                            arrays
                    );

                    variables.put(
                            variable,
                            variables.get(variable) - value
                    );
                }

// i = i + 2
// i = i - 2
                else if(forUpdate.contains("=")) {

                    String[] parts = forUpdate.split("=");

                    String variable = parts[0].trim();

                    int value = evaluateExpression(
                            parts[1].trim(),
                            variables,
                            arrays
                    );


                    variables.put(
                            variable,
                            value
                    );
                }

                addStep(
                        steps,
                        variables,
                        arrays,
                        stepNumber++
                );

                if(evaluateCondition(forCondition, variables, arrays)) {

                    currentLine = forStartLine;

                    continue;
                }
                else {

                    forStartLine = -1;
                    forCondition = "";
                    forUpdate = "";
                }
            }

            else if(line.startsWith("for")) {

                String inside = line.substring(
                        line.indexOf("(") + 1,
                        line.indexOf(")")
                );

                String[] parts = inside.split(";");

                // Initialization
                String initialization = parts[0].trim();

                // Condition
                forCondition = parts[1].trim();

                // Update
                forUpdate = parts[2].trim();

                // Execute initialization as a normal statement
                if(initialization.startsWith("int ")) {

                    initialization = initialization.replace("int ", "");

                    String[] initParts = initialization.split("=");

                    String variableName = initParts[0].trim();

                    int value = getValue(
                            initParts[1].trim(),
                            variables,
                            arrays
                    );

                    variables.put(variableName, value);

                    addStep(
                            steps,
                            variables,
                            arrays,
                            stepNumber++
                    );
                }

                forStartLine = currentLine + 1;

                executeBlock = evaluateCondition(
                        forCondition,
                        variables,
                        arrays
                );
                currentLine++;
                continue;
            }
            if(line.startsWith("while")) {

                whileStartLine = currentLine;

                whileCondition = line.substring(
                        line.indexOf("(") + 1,
                        line.indexOf(")")
                );

                executeBlock = evaluateCondition(
                        whileCondition,
                        variables,
                        arrays
                );

                currentLine++;
                continue;
            }

            if(line.startsWith("if")) {

                String condition = line.substring(
                        line.indexOf("(") + 1,
                        line.indexOf(")")


                );

                prevBool = evaluateCondition(
                        condition,
                        variables,
                        arrays
                );

                executeBlock = prevBool;
                currentLine++;
                continue;


            }

            if(line.startsWith("else")) {
                executeBlock=!prevBool;
                currentLine++;
                continue;
            }

            if(line.startsWith("int[]")) {

                String statement = line
                        .replace("int[]", "")
                        .replace(";", "");

                String[] parts = statement.split("=");

                String arrayName = parts[0].trim();

                String values = parts[1].trim();

                values = values.replace("{", "")
                        .replace("}", "");

                String[] numbers = values.split(",");

                ArrayList<Integer> arrValues = new ArrayList<>();

                for(String num : numbers) {
                    arrValues.add(
                            Integer.parseInt(num.trim())
                    );
                }

                arrays.put(arrayName, arrValues);
                addStep(
                        steps,
                        variables,
                        arrays,
                        stepNumber++
                );


                currentLine++;
                continue;
            }

            if(line.startsWith("int ")) {

                String statement = line.replace("int ", "")
                        .replace(";", "");

                String[] parts = statement.split("=");

                String variableName = parts[0].trim();

                int value = getValue(
                        parts[1].trim(),
                        variables,
                        arrays
                );

                variables.put(variableName, value);

                addStep(steps, variables,arrays, stepNumber++);
            }
            else if(line.contains("="))
            {
                String statement = line.replace(";", "");

                String[] parts = statement.split("=");

                String variableName = parts[0].trim();

                if(variableName.contains("[") && variableName.contains("]")) {

                    String arrayName = variableName.substring(
                            0,
                            variableName.indexOf("[")
                    );

                    String indexText = variableName.substring(
                            variableName.indexOf("[") + 1,
                            variableName.indexOf("]")
                    ).trim();

                    int index = getValue(
                            indexText,
                            variables,
                            arrays
                    );

                    int value = evaluateExpression(
                            parts[1].trim(),
                            variables,
                            arrays
                    );

                    arrays.get(arrayName).set(index, value);
                    addStep(
                            steps,
                            variables,
                            arrays,
                            stepNumber++
                    );


                    currentLine++;
                    continue;
                }

                String expression = parts[1].trim();

                int value = evaluateExpression(
                        expression,
                        variables,
                        arrays
                );

                variables.put(variableName, value);

                addStep(
                        steps,
                        variables,
                        arrays,
                        stepNumber++
                );

            }

            currentLine++;
        }

        return steps;
    }

    private int getValue(
            String operand,
            Map<String, Integer> variables,
            Map<String, ArrayList<Integer>> arrays
    ) {
        if(operand.endsWith(".length")) {

            String arrayName = operand.replace(".length", "");

            return arrays.get(arrayName).size();
        }

        if(operand.contains("[") && operand.contains("]")) {

            String arrayName = operand.substring(
                    0,
                    operand.indexOf("[")
            );

            String indexText = operand.substring(
                    operand.indexOf("[") + 1,
                    operand.indexOf("]")
            ).trim();

            int index = getValue(
                    indexText,
                    variables,
                    arrays
            );

            return arrays.get(arrayName).get(index);
        }


        if(variables.containsKey(operand)) {
            return variables.get(operand);
        }

        return Integer.parseInt(operand);
    }

    private int evaluateExpression(
            String expression,
            Map<String, Integer> variables,
            Map<String, ArrayList<Integer>> arrays
    ) {

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
            return getValue(expression, variables, arrays);
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

        int leftValue = getValue(
                operands[0].trim(),
                variables,
                arrays
        );

        int rightValue = getValue(
                operands[1].trim(),
                variables,
                arrays
        );

        if(operator.equals("+")) {
            return leftValue + rightValue;
        }
        else if(operator.equals("-")) {
            return leftValue - rightValue;
        }
        else if(operator.equals("*")) {
            return leftValue * rightValue;
        }
        else {
            return leftValue / rightValue;
        }
    }



    private boolean evaluateCondition(
            String condition,
            Map<String, Integer> variables,
            Map<String, ArrayList<Integer>> arrays
    ) {

        String operator;


        if(condition.contains(">=")) {
            operator = ">=";
        }
        else if(condition.contains("<=")) {
            operator = "<=";
        }
        else if(condition.contains("!=")) {
            operator = "!=";
        }
        else if(condition.contains("==")) {
            operator = "==";
        }
        else if(condition.contains(">")) {
            operator = ">";
        }
        else {
            operator = "<";
        }

        String[] parts = condition.split(operator);

        String leftOperand = parts[0].trim();
        String rightOperand = parts[1].trim();

        int leftValue = getValue(leftOperand, variables, arrays);
        int rightValue = getValue(rightOperand, variables, arrays);

        if(operator.equals("==")) {
            return leftValue == rightValue;
        }
        else if(operator.equals("!=")) {
            return leftValue != rightValue;
        }
        else if(operator.equals(">=")) {
            return leftValue >= rightValue;
        }
        else if(operator.equals("<=")) {
            return leftValue <= rightValue;
            }
        else if(operator.equals(">")) {
            return leftValue > rightValue;
        }
        else {
            return leftValue < rightValue;
        }
    }

    private void addStep(
            List<StepDTO> steps,
            Map<String, Integer> variables,
            Map<String, ArrayList<Integer>> arrays,
            int stepNumber
    ) {
        Map<String, ArrayList<Integer>> arraysCopy = new HashMap<>();

        for(String key : arrays.keySet()) {
            arraysCopy.put(
                    key,
                    new ArrayList<>(arrays.get(key))
            );
        }

        steps.add(
                new StepDTO(
                        stepNumber,
                        new HashMap<>(variables),
                        arraysCopy
                )
        );
    }
}