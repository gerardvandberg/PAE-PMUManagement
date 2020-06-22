package com.pae.PMU.schema;

import com.pae.PMU.entity.PumpInterventionEntity;
import io.swagger.annotations.ApiModelProperty;

public class InterventionSchemaGET extends InterventionSchema  implements Comparable<InterventionSchemaGET> {
    @ApiModelProperty(notes = "Price of the intervention.", example = "400", required = true)
    double price;

    public InterventionSchemaGET(PumpInterventionEntity intervention,Double price) {
        super(intervention);
        this.price=price;
    }

    public InterventionSchemaGET(PumpInterventionEntity intervention) {
        super(intervention);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int compareTo(InterventionSchemaGET o) {
        if (super.getInterventionDate() == null || o.getInterventionDate() == null)
            return 0;
        return o.getInterventionDate().compareTo(getInterventionDate());
    }
}
