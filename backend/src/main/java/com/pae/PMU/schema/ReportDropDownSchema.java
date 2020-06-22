package com.pae.PMU.schema;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.*;

@ApiModel(description = "Class representing a dropdown")
public class ReportDropDownSchema {
    @ApiModelProperty(notes = "List of causes.", example = "Cause 1", required = true)
    TreeSet<String> cause;
    @ApiModelProperty(notes = "List of problems.", example = "Problem 1", required = true)
    TreeSet<String> problem;
    @ApiModelProperty(notes = "List of operations.", example = "Operation 1", required = true)
    TreeSet<String> operation;

    public Set<String> getCause() {
        return cause;
    }

    public void setCause(TreeSet<String> cause) {
        this.cause = cause;
    }

    public Set<String> getProblem() {
        return problem;
    }

    public void setProblem(TreeSet<String> problem) {
        this.problem = problem;
    }

    public Set<String> getOperation() {
        return operation;
    }

    public void setOperation(TreeSet<String> operation) {
        this.operation = operation;
    }




}
