package com.pae.PMU.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "report_drop_down")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReportDropDownEntity implements Serializable {
    @Id
    @Column(length = 300)
    String pumpId;
    @ElementCollection
    Set<String> problem;
    @ElementCollection
    Set<String> operation;
    @ElementCollection
    Set<String> cause;

    public ReportDropDownEntity() {
        this.problem=new TreeSet<>();
        this.operation=new TreeSet<>();
        this.cause=new TreeSet<>();
    }

    public String getPumpId() {
        return pumpId;
    }

    public void setPumpId(String pumpId) {
        this.pumpId = pumpId;
    }

    public Set<String> getProblem() {
        return problem;
    }

    public void setProblem(Set<String> problem) {
        this.problem = problem;
    }

    public Set<String> getOperation() {
        return operation;
    }

    public void setOperation(Set<String> operation) {
        this.operation = operation;
    }

    public Set<String> getCause() {
        return cause;
    }

    public void setCause(Set<String> cause) {
        this.cause = cause;
    }
}
