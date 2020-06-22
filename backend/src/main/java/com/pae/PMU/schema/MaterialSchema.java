package com.pae.PMU.schema;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Embeddable;

@Embeddable
public class MaterialSchema {
    @ApiModelProperty(notes = "Id of the material.", example = "Ball bearings", required = true)
    private String material;
    @ApiModelProperty(notes = "Total cost of the material.", example = "200", required = true)
    private Double totalCost;
    @ApiModelProperty(notes = "Units bought.", example = "5", required = true)
    private Double units;

    public MaterialSchema() {

    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
    }
}
