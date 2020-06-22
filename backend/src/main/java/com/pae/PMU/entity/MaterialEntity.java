package com.pae.PMU.entity;

import com.pae.PMU.schema.MaterialSchema;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="materials")
public class MaterialEntity {
    @Id
    private String material;
    private Double costPerUnit;

    public MaterialEntity() {

    }

    public MaterialEntity(MaterialSchema material) {
        this.material=material.getMaterial();
        this.costPerUnit=material.getTotalCost()/material.getUnits();
    }

    public Double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(Double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
