package com.aarya.dsavisualizer.model;

public class LoopFrame {

    private String type;

    private int startLine;

    private String condition;

    private String update;

    public LoopFrame(
            String type,
            int startLine,
            String condition,
            String update
    ) {
        this.type = type;
        this.startLine = startLine;
        this.condition = condition;
        this.update = update;
    }

    public String getType() {
        return type;
    }

    public int getStartLine() {
        return startLine;
    }

    public String getCondition() {
        return condition;
    }

    public String getUpdate() {
        return update;
    }

}