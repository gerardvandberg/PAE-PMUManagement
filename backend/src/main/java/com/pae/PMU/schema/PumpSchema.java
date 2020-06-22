package com.pae.PMU.schema;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@ApiModel(description = "Class representing a pump")
public class PumpSchema {
    @ApiModelProperty(notes = "Id of the pump.", example = "1", required = true)
    private String pumpId;
    @ApiModelProperty(notes = "Name of the pump.", example = "Pumpy", required = true)
    private String name;
    @ApiModelProperty(notes = "Territory where the pump is.", example = "Turkana", required = true)
    private String territory;
    @ApiModelProperty(notes = "Organization who built the pump.", example = "Organization who builds pumps", required = true)
    private String builtBy;
    @ApiModelProperty(notes = "Maintainer of the pump.", example = "Community", required = true)
    private String maintainedBy;
    @ApiModelProperty(notes = "Whether the pump is under the PMU program.", example = "true", required = true)
    private boolean underPMU;
    @ApiModelProperty(notes = "Location of the pump", example = "City in turkana", required = true)
    private String location;
    @ApiModelProperty(notes = "Degrees minutes seconds.", example = "50 52 10", required = true)
    private String northings;
    @ApiModelProperty(notes = "Degrees minutes seconds.", example = "50 52 10", required = true)
    private String eastings;
    @ApiModelProperty(notes = "Type of pump.", example = "Wind", required = true)
    private String type;
    @ApiModelProperty(notes = "Model of the pump.", example = "Model 38U", required = true)
    private String model;
    @ApiModelProperty(notes = "Size of the pump.", example = "2", required = true)
    private String pumpSize;
    @ApiModelProperty(notes = "Remarks about the pump.", example = "[\"Breaks often\"]", required = true)
    private List<String> remarks;
    @ApiModelProperty(notes = "Running balance of the pump.", example = "38", required = false)
    private Double runningBalance;
    @ApiModelProperty(notes = "Comitee contracts of the pump.", example = "[\"Contract with comitee1\"]", required = false)
    private List<String> comiteeContracts;
    @ApiModelProperty(notes = "Company who drilled the pump.", example = "Pump Drillers", required = false)
    private String drillingCompany;
    @ApiModelProperty(notes = "Which year was the pump drilled.", example = "1999", required = false)
    private String drillingYear;
    @ApiModelProperty(notes = "Depths of the borehole.", example = "2.5", required = false)
    private Double boreholeDepth;
    @ApiModelProperty(notes = "Diameter of the borehole.", example = "2.5", required = true)
    private Double boreholeDiameter;
    @ApiModelProperty(notes = "Static water level of the pump.", example = "3", required = true)
    private Double staticWaterLevel;
    @ApiModelProperty(notes = "Dynamic water level of the pump.", example = "2", required = true)
    private Double dynamicWaterLevel;
    @ApiModelProperty(notes = "Tested yield of the pump.", example = "3", required = false)
    private Double testedYield;
    @ApiModelProperty(notes = "Inlet flow of the pump.", example = "10", required = false)
    private Double inlet;
    @ApiModelProperty(notes = "Tested inlet flow of the pump.", example = "7", required = false)
    private Double inletYieldTest;
    @ApiModelProperty(notes = "Water quality of the pump.", example = "Good", required = false)
    private String waterQuality;

    public String getPumpId() {
        return pumpId;
    }

    public void setPumpId(String pumpId) {
        this.pumpId = pumpId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getBuiltBy() {
        return builtBy;
    }

    public void setBuiltBy(String builtBy) {
        this.builtBy = builtBy;
    }

    public String getMaintainedBy() {
        return maintainedBy;
    }

    public void setMaintainedBy(String maintainedBy) {
        this.maintainedBy = maintainedBy;
    }

    public boolean isUnderPMU() {
        return underPMU;
    }

    public void setUnderPMU(boolean underPMU) {
        this.underPMU = underPMU;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNorthings() {
        return northings;
    }

    public void setNorthings(String northings) {
        this.northings = northings;
    }

    public String getEastings() {
        return eastings;
    }

    public void setEastings(String eastings) {
        this.eastings = eastings;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPumpSize() {
        return pumpSize;
    }

    public void setPumpSize(String pumpSize) {
        this.pumpSize = pumpSize;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }

    public Double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Double runningBalance) {
        this.runningBalance = runningBalance;
    }

    public List<String> getComiteeContracts() {
        return comiteeContracts;
    }

    public void setComiteeContracts(List<String> comiteeContracts) {
        this.comiteeContracts = comiteeContracts;
    }

    public String getDrillingCompany() {
        return drillingCompany;
    }

    public void setDrillingCompany(String drillingCompany) {
        this.drillingCompany = drillingCompany;
    }

    public String getDrillingYear() {
        return drillingYear;
    }

    public void setDrillingYear(String drillingYear) {
        this.drillingYear = drillingYear;
    }

    public Double getBoreholeDepth() {
        return boreholeDepth;
    }

    public void setBoreholeDepth(Double boreholeDepth) {
        this.boreholeDepth = boreholeDepth;
    }

    public Double getBoreholeDiameter() {
        return boreholeDiameter;
    }

    public void setBoreholeDiameter(Double boreholeDiameter) {
        this.boreholeDiameter = boreholeDiameter;
    }

    public Double getStaticWaterLevel() {
        return staticWaterLevel;
    }

    public void setStaticWaterLevel(Double staticWaterLevel) {
        this.staticWaterLevel = staticWaterLevel;
    }

    public Double getDynamicWaterLevel() {
        return dynamicWaterLevel;
    }

    public void setDynamicWaterLevel(Double dynamicWaterLevel) {
        this.dynamicWaterLevel = dynamicWaterLevel;
    }

    public Double getTestedYield() {
        return testedYield;
    }

    public void setTestedYield(Double testedYield) {
        this.testedYield = testedYield;
    }

    public Double getInlet() {
        return inlet;
    }

    public void setInlet(Double inlet) {
        this.inlet = inlet;
    }

    public Double getInletYieldTest() {
        return inletYieldTest;
    }

    public void setInletYieldTest(Double inletYieldTest) {
        this.inletYieldTest = inletYieldTest;
    }

    public String getWaterQuality() {
        return waterQuality;
    }

    public void setWaterQuality(String waterQuality) {
        this.waterQuality = waterQuality;
    }
}
