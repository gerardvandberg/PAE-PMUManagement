package com.pae.PMU.schema;

import com.pae.PMU.entity.PumpInterventionEntity;

import java.util.ArrayList;
import java.util.List;

public class TrainModelsSchema {
    List<InterventionSchema> interventions;


    public TrainModelsSchema() {
    }

    public TrainModelsSchema(List<PumpInterventionEntity> interventions) {
        this.interventions=new ArrayList<>();
        for (PumpInterventionEntity intervention: interventions) {
            this.interventions.add(new InterventionSchema(intervention));
        }
    }

    public List<InterventionSchema> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<InterventionSchema> interventions) {
        this.interventions = interventions;
    }
}
