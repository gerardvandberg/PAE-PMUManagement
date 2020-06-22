package com.pae.PMU.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class PumpInterventionKey implements Serializable {
    private String pumpId;
    private Date interventionDate;

    public PumpInterventionKey() {
    }

    public PumpInterventionKey(String pumpId, Date interventionDate) {
        this.pumpId = pumpId;
        this.interventionDate = interventionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PumpInterventionKey accountId = (PumpInterventionKey) o;
        return pumpId.equals(accountId.pumpId) &&
                interventionDate.equals(accountId.interventionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pumpId, interventionDate);
    }
}
