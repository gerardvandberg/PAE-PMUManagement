package com.pae.PMU.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pae.PMU.schema.InterventionSchema;
import com.pae.PMU.schema.MaterialSchema;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "pump_intervention_entities")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@IdClass(PumpInterventionKey.class)
public class PumpInterventionEntity {
    @Id
    private String pumpId;
    @Id
    private Date interventionDate;
    private String falseId;
    private String remarks;
    private Integer interventionCode;
    private Integer responseTime;
    private String type;
    private Date failureDate;
    private String problem;
    private String operation;
    private String cause;
    @Column(name="valueTo")
    private String to;
    @Column(name="valueFrom")
    private String from;
    private Integer distanceTravelled;
    private Integer workers;
    private Integer costPerWorker;
    private Double interventionPrice;

    @ElementCollection
    private List<MaterialSchema> materials;

    public PumpInterventionEntity() {

    }

    public PumpInterventionEntity(InterventionSchema intervention) {
        this.pumpId=intervention.getPumpId();
        this.interventionDate=intervention.getInterventionDate();
        this.remarks=intervention.getRemarks();
        this.interventionCode=intervention.getInterventionCode();
        this.responseTime=intervention.getResponseTime();
        this.type=intervention.getType();
        this.failureDate=intervention.getFailureDate();
        this.problem=intervention.getProblem();
        this.operation=intervention.getOperation();
        this.cause=intervention.getCause();
        this.to=intervention.getTo();
        this.from=intervention.getFrom();
        this.distanceTravelled=intervention.getDistanceTravelled();
        this.workers=intervention.getWorkers();
        this.costPerWorker=intervention.getCostPerWorker();
        Set<String> ids=new HashSet<>();
        List<MaterialSchema> materialUnitsPair=new ArrayList<>();
        for (MaterialSchema material:intervention.getMaterials()) {
            if (!ids.contains(material.getMaterial())) {
                ids.add(material.getMaterial());
                materialUnitsPair.add(material);
            }
        }
        this.materials=materialUnitsPair;
        this.falseId=this.pumpId+this.interventionDate;
    }

    public Double getInterventionPrice() {
        return interventionPrice;
    }

    public void setInterventionPrice(Double interventionPrice) {
        this.interventionPrice = interventionPrice;
    }

    public String getPumpId() {
        return pumpId;
    }

    public void setPumpId(String pumpId) {
        this.pumpId = pumpId;
    }

    public Date getInterventionDate() {
        return interventionDate;
    }

    public void setInterventionDate(Date interventionDate) {
        this.interventionDate = interventionDate;
    }

    public String getFalseId() {
        return falseId;
    }

    public void setFalseId(String falseId) {
        this.falseId = falseId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getInterventionCode() {
        return interventionCode;
    }

    public void setInterventionCode(Integer interventionCode) {
        this.interventionCode = interventionCode;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getFailureDate() {
        return failureDate;
    }

    public void setFailureDate(Date failureDate) {
        this.failureDate = failureDate;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(Integer distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public List<MaterialSchema> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialSchema> materials) {
        this.materials = materials;
    }

    public Integer getWorkers() {
        return workers;
    }

    public void setWorkers(Integer workers) {
        this.workers = workers;
    }

    public Integer getCostPerWorker() {
        return costPerWorker;
    }

    public void setCostPerWorker(Integer costPerWorker) {
        this.costPerWorker = costPerWorker;
    }


}
