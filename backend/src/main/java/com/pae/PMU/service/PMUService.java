package com.pae.PMU.service;

import com.pae.PMU.entity.*;
import com.pae.PMU.repository.MaterialRepository;
import com.pae.PMU.repository.PumpInterventionRepository;
import com.pae.PMU.repository.PumpRepository;
import com.pae.PMU.repository.ReportDropDownRepository;
import com.pae.PMU.schema.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class PMUService {
    @Autowired
    PumpRepository pumpRepository;
    @Autowired
    PumpInterventionRepository pumpInterventionRepository;
    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    PredictionService predictionService;
    @Autowired
    ReportDropDownRepository reportDropDownRepository;


    Double costPerKm=0.1;

    public PumpEntity getPump(String id) {
        return pumpRepository.getOne(id);
    }
    public PumpEntity postPump(PumpSchema pump) {
        PumpEntity entity=new PumpEntity(pump);
        return pumpRepository.save(entity);
    }

    public void addAdjacentPumps(String pumpId, List<String> adjacentPumps) {
        PumpEntity mainPump=pumpRepository.getOne(pumpId);
        List<PumpEntity> secondaryPumps=new ArrayList<>();
        for (String s:adjacentPumps)secondaryPumps.add(pumpRepository.getOne(s));
        Set<String> currentAdjacent=mainPump.getAdjacentPumps();
        currentAdjacent.addAll(adjacentPumps);
        mainPump.setAdjacentPumps(currentAdjacent);
        pumpRepository.save(mainPump);
        for (PumpEntity pump:secondaryPumps) {
            if (!pump.getAdjacentPumps().contains(pumpId)) {
                Set<String> adjacent=pump.getAdjacentPumps();
                adjacent.add(pumpId);
                pump.setAdjacentPumps(adjacent);
                pumpRepository.save(pump);
            }
        }
        pumpRepository.flush();
    }

    public void updatePump(PumpSchema pump) {
        PumpEntity oldPump=pumpRepository.getOne(pump.getPumpId());
        oldPump.update(pump);
    }

    public void postIntervention(InterventionSchema intervention) {
        PumpInterventionEntity interventionEntity= new PumpInterventionEntity(intervention);
        for(MaterialSchema material:intervention.getMaterials()) {
            if (!materialRepository.existsById(material.getMaterial())) {
                MaterialEntity newMaterial=new MaterialEntity(material);
                materialRepository.save(newMaterial);
            }
        }
        interventionEntity.setInterventionPrice(getPriceIntervention(interventionEntity));
        pumpInterventionRepository.save(interventionEntity);
    }

    public List<InterventionSchemaGET> getInterventions(String pumpId) {
        List<PumpInterventionEntity> interventions=pumpInterventionRepository.findByPumpId(pumpId);
        List<InterventionSchemaGET> result=new ArrayList<>();
        for(PumpInterventionEntity intervention:interventions) {
            Double price=getPriceIntervention(intervention);
            InterventionSchemaGET aux=new InterventionSchemaGET(intervention,price);
            result.add(aux);
        }
        Collections.sort(result);
        return result;
    }

    public void modifyMaterial(MaterialSchema material) {
        if (materialRepository.existsById(material.getMaterial())) {
            MaterialEntity item=materialRepository.getOne(material.getMaterial());
            item.setCostPerUnit(material.getTotalCost()/material.getUnits());
            materialRepository.save(item);
        }
        else {
            MaterialEntity item=new MaterialEntity(material);
            materialRepository.save(item);
        }
    }

    public Double getPriceIntervention(PumpInterventionEntity intervention){
        Double result=0.0;
        result+=intervention.getWorkers()*intervention.getCostPerWorker();
        result+=intervention.getDistanceTravelled()*costPerKm;
        for (MaterialSchema item:intervention.getMaterials()) {
            MaterialEntity material=materialRepository.getOne(item.getMaterial());
            result+=material.getCostPerUnit()*(item.getTotalCost()/item.getUnits());
        }
        return result;
    }

    public List<InterventionSchemaGET> getInterventionsBetweenDates(String pumpId, Date from, Date to) {
        List<InterventionSchemaGET> toFix=getInterventions(pumpId);
        List<InterventionSchemaGET> result=new ArrayList<>();
        for(InterventionSchemaGET intervention:toFix) {
            if (intervention.getInterventionDate().compareTo(from)>0 && intervention.getInterventionDate().compareTo(to)<0) {
                result.add(intervention);
            }
        }
        return result;
    }

    public FailureSchema getPredictedFailureDate(String pumpId) throws IOException {
        PumpEntity pump=pumpRepository.getOne(pumpId);
        FailureSchema failure=predictionService.predict(pump);
        return failure;
    }

    public Set<Integer> getAllIds() throws IOException {
        Set<Integer> ids=new TreeSet<>();
        for (PumpEntity pump: pumpRepository.findAll()) {
            ids.add(Integer.parseInt(pump.getId()));
        }
        return ids;
    }
    public ReportDropDownSchema getDropdownValue(String pumpId) {
        ReportDropDownEntity generic=reportDropDownRepository.getOne("0");
        ReportDropDownEntity entity;
        if (reportDropDownRepository.existsById(pumpId))  entity=reportDropDownRepository.getOne(pumpId);
        else entity=new ReportDropDownEntity();
        ReportDropDownSchema dropdown= new ReportDropDownSchema();
        TreeSet cause=new TreeSet(entity.getCause());
        TreeSet operation=new TreeSet(entity.getOperation());
        TreeSet problem=new TreeSet(entity.getProblem());
        cause.addAll(generic.getCause());
        operation.addAll(generic.getOperation());
        problem.addAll(generic.getProblem());
        dropdown.setCause(cause);
        dropdown.setOperation(operation);
        dropdown.setProblem(problem);
        return dropdown;

    }


    public void addDropdownValue(String id, ReportDropDownSchema reportDropDown) {
        ReportDropDownEntity entity;
        if (!reportDropDownRepository.existsById(id)) {
             entity=new ReportDropDownEntity();
             entity.setPumpId(id);
        }
        else {
             entity=reportDropDownRepository.getOne(id);
        }
        if (reportDropDown.getCause()!=null) {
            Set<String> causes=entity.getCause();
            causes.addAll(reportDropDown.getCause());
            entity.setCause(causes);
        }
        if (reportDropDown.getOperation()!=null) {
            Set<String> operation=entity.getOperation();
            operation.addAll(reportDropDown.getOperation());
            entity.setOperation(operation);
        }
        if (reportDropDown.getProblem()!=null) {
            Set<String> problem=entity.getProblem();
            problem.addAll(reportDropDown.getProblem());
            entity.setProblem(problem);
        }
        reportDropDownRepository.save(entity);
    }
    public void loadDropdowns() {
        Set problem = new TreeSet(Arrays.asList("A. No water",
                "B. Water flow delayed",
                "C. Reduced discharge",
                "D. Abnormal noise during operation",
                "E. Pump handle shaky",
                "F. Pump stand shaky",
                "G. Describe",
                "A. No water",
                "B. Water flow delayed",
                "C. Folding of chain during down-stroke",
                "D. Abnormal noise during operation",
                "E. Pump handle shaky",
                "F. Pump stand shaky"));
        Set operation = new TreeSet(Arrays.asList("1.Handle operation is easy",
                "3.Handle operation is difficult",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "3.Handle operation is difficult",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "4.Handle operation is inconvenient",
                "4.Handle operation is inconvenient",
                "4.Handle operation is inconvenient",
                "5.Handle is shaky",
                "5.Handle is shaky",
                "5.Handle is shaky",
                "6.Pump head is shaky",
                "7.Stand is shaky",
                "1.Handle operation is easy",
                "3.Handle operation is difficult",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "4.Handle operation is inconvenient",
                "4.Handle operation is inconvenient",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "2.Handle operation is normal",
                "5.Handle is shaky",
                "5.Handle is shaky",
                "5.Handle is shaky",
                "5.Handle is shaky",
                "5.Handle is shaky",
                "5.Handle is shaky",
                "6.Stand is shaky"));
        Set cause = new TreeSet(Arrays.asList("A.1 Pumprod disconnected",
                "A.3 Rising main disconnected",
                "A.2 Cup seal is defect",
                "A.2 Footvalve jammed (not closing)",
                "A.2 Borehole clocked (silt or sand)",
                "A.2 SWL below pump intake",
                "B.2 Leaking of valve bobbins",
                "B.2 Leaking of footvalve o-ring",
                "B.2 Rising main joints leaking",
                "B.2 Rising main perforated",
                "C.3 Cup seal too tight",
                "C.2 Full stroke is not possible",
                "C.2 Cup seal is worn",
                "C.2 Leaking of valve bobbins",
                "C.2 Leaking of the cylinder (cracked)",
                "D.2 Pump rods rubbing on raising main",
                "D.2 Pump rod centralizers worn",
                "D.4 Pump rods in contact with raising main",
                "D.4 Bearings worn",
                "D.4 Handle fork in contact with pump head",
                "E.5 Bearings worn",
                "E.5 Fulcrun pin loose",
                "E.5 Hanger pin loose",
                "E.6 Flanges are loose",
                "F.7 Platform cracked",
                "A.1 Pumprod disconnected",
                "A.3 Rising main disconnected",
                "A.2 Cup seal is defect",
                "A.2 Broken chain",
                "A.2 Footvalve jammed (not closing)",
                "A.2 Borehole clocked (silt or sand)",
                "A.2 SWL below pump intake",
                "B.2 Leaking of cylinder-to-reducer cup sealing rings",
                "B.2 Leaking of footvalve seating rubber",
                "B.2 Leaking of valve cup seals",
                "B.2 Rising main joints leaking",
                "B.2 Rising main perforated",
                "C.4 Plunger jammed inside cylinder",
                "C.4 Plunger seating on top of footvalve",
                "D.2 Pump rods rubbing on raising main",
                "D.2 Bearings worn",
                "D.2 Shaky foundation",
                "D.2 Lack of grease on chain",
                "E.5 Bearings worn",
                "E.5 Loose handle axle nuts",
                "E.5 Damaged axle",
                "E.5 Worn spacer",
                "E.5 Bearings worn",
                "E.5 Bearings loose - Handle assembly defect",
                "F.6 Platform cracked"));
        ReportDropDownEntity entity=new ReportDropDownEntity();
        entity.setPumpId("0");
        entity.setCause(cause);
        entity.setOperation(operation);
        entity.setProblem(problem);
        reportDropDownRepository.save(entity);
    }


    public InterventionSchemaGET getIntervention(String falseId) {
        PumpInterventionEntity ent=pumpInterventionRepository.findByFalseId(falseId);
        Double price=getPriceIntervention(ent);
        InterventionSchemaGET aux=new InterventionSchemaGET(ent,price);
        return aux;

    }
}
