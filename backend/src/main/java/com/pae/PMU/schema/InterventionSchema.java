package com.pae.PMU.schema;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.pae.PMU.entity.PumpInterventionEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

public class InterventionSchema {
    @ApiModelProperty(notes = "If of the pump intervened.", example = "5", required = true)
    private String pumpId;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(notes = "Date of intervention.", example = "2020-02-02T20:50:12.123Z", required = true)
    private Date interventionDate;
    @ApiModelProperty(notes = "Remarks about intervention.", example = "Part x looks worn out, might need replacement soon", required = true)
    private String remarks;
    @ApiModelProperty(notes = "Intervention code.", example = "1", required = true)
    private Integer interventionCode;
    @ApiModelProperty(notes = "How long until the intervention team arrived in days.", example = "30", required = true)
    private Integer responseTime;
    @ApiModelProperty(notes = "Type of pump involved in the intervention.", example = "Wind", required = true)
    private String type;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(notes = "When the pump failed.", example = "2020-01-02T20:50:12.123Z", required = false)
    private Date failureDate;
    @ApiModelProperty(notes = "What problem caused the pump to malfunction.", example = "Broken bearings", required = true)
    private String problem;
    @ApiModelProperty(notes = "Operation performed.", example = "Replaced bearings", required = true)
    private String operation;
    @ApiModelProperty(notes = "Cause of the malfunction.", example = "High pressure put too much stress", required = true)
    private String cause;
    @ApiModelProperty(notes = "From where the pump team departed.", example = "Area1", required = true)
    private String to;
    @ApiModelProperty(notes = "Where the pump team intervened.", example = "Area2", required = true)
    private String from;
    @ApiModelProperty(notes = "Kilometers traveled to get to the pump since last intervention.", example = "340", required = true)
    private Integer distanceTravelled;
    @ApiModelProperty(notes = "Number of workers who performed the intervention.", example = "5", required = true)
    private Integer workers;
    @ApiModelProperty(notes = "Cost per worker.", example = "100", required = true)
    private Integer costPerWorker;
    @ApiModelProperty(notes = "FalseId.", example = "100", required = true)
    private String falseId;


    private List<MaterialSchema> materials;

    public InterventionSchema() {

    }

    public InterventionSchema(PumpInterventionEntity intervention) {
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
        this.falseId=intervention.getFalseId();
        this.materials=intervention.getMaterials();
    }

    public void setPumpId(String pumpId) {
        this.pumpId = pumpId;
    }

    public String getFalseId() {
        return falseId;
    }

    public void setFalseId(String falseId) {
        this.falseId = falseId;
    }

    public String getPumpId() {
        return pumpId;
    }

    public void setId(String pumpId) {
        this.pumpId = pumpId;
    }

    public Date getInterventionDate() {
        return interventionDate;
    }

    public void setInterventionDate(Date interventionDate) {
        this.interventionDate = interventionDate;
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

    public List<MaterialSchema> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialSchema> materials) {
        this.materials = materials;
    }
}
