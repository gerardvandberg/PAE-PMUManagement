package com.pae.PMU.service;

import com.pae.PMU.NLP.CosineSimilarity;
import com.pae.PMU.NLP.TfIdf;
import com.pae.PMU.entity.PumpEntity;
import com.pae.PMU.entity.PumpInterventionEntity;
import com.pae.PMU.entity.ReportDropDownEntity;
import com.pae.PMU.repository.PumpInterventionRepository;
import com.pae.PMU.repository.PumpRepository;
import com.pae.PMU.repository.ReportDropDownRepository;
import com.pae.PMU.schema.FailureSchema;
import com.pae.PMU.schema.InterventionSchemaGET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PredictionService {
    @Autowired
    PumpInterventionRepository pumpInterventionRepository;
    @Autowired
    PumpRepository pumpRepository;

    public FailureSchema predict(PumpEntity pump) throws IOException {
        List<PumpInterventionEntity> interventions=pumpInterventionRepository.findByType(pump.getType());
        PumpInterventionEntity auxiliar=new PumpInterventionEntity();
        String remarks="";
        for (String s:pump.getRemarks()) {
            remarks+=" "+s;
        }
        auxiliar.setRemarks(remarks);
        Date current=new Date();
        auxiliar.setInterventionDate(current);
        auxiliar.setFailureDate(current);
        auxiliar.setPumpId(pump.getId());
        interventions.add(auxiliar);
        PumpInterventionEntity closest=null;
        Double distance=null;
        TfIdf tfidf=new TfIdf(0.0);
        Map<String, Map<String, Double>> tfidfMatrix=tfidf.computeTFIDF(interventions);
        String auxId=current.toString()+pump.getId();
        for (PumpInterventionEntity intervention:interventions) {
            String interiorId=intervention.getInterventionDate().toString()+intervention.getPumpId();
            Double cosSim=CosineSimilarity.cosineSimilarity(tfidfMatrix.get(auxId),tfidfMatrix.get(interiorId));
            if (distance==null || distance<cosSim) {
                distance=cosSim;
                closest=intervention;
            }
        }
        List<InterventionSchemaGET> ordered = getInterventions(pump.getId());
        InterventionSchemaGET nearestInTime=null;
        if (ordered.size()>0)  nearestInTime = ordered.get(0);
        else {
            PumpInterventionEntity aux=new PumpInterventionEntity();
            aux.setInterventionDate(pump.getLastUpdated());
            nearestInTime=new InterventionSchemaGET(aux);
        }


        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

        if (closest!=null) {
            List<InterventionSchemaGET> orderedByDate = getInterventions(closest.getPumpId());
            Date currentFailureDate = null;
            for (InterventionSchemaGET inter : orderedByDate) {
                if (inter.getFailureDate().compareTo(closest.getFailureDate()) > 0) {
                    currentFailureDate = inter.getFailureDate();
                    break;
                }
            }
            FailureSchema failure = new FailureSchema();
            if (currentFailureDate != null) {
                BigInteger diff = new BigInteger(String.valueOf(closest.getFailureDate().getTime() - currentFailureDate.getTime()));
                failure.setEstimatedDate(new Date(nearestInTime.getInterventionDate().getTime() +Math.abs(diff.longValueExact())));

            }
            else {
                Calendar c = Calendar.getInstance();
                c.setTime(nearestInTime.getInterventionDate());
                c.add(Calendar.DATE, 7*12);
                failure.setEstimatedDate(c.getTime());
            }
            return failure;
        }
        else {
            Calendar c = Calendar.getInstance();
            c.setTime(nearestInTime.getInterventionDate());
            c.add(Calendar.DATE, 7*12);
            FailureSchema failure = new FailureSchema();
            failure.setEstimatedDate(c.getTime());
            return failure;

        }
    }


    public List<InterventionSchemaGET> getInterventions(String pumpId) {
        List<PumpInterventionEntity> interventions=pumpInterventionRepository.findByPumpId(pumpId);
        List<InterventionSchemaGET> result=new ArrayList<>();
        for(PumpInterventionEntity intervention:interventions) {
            InterventionSchemaGET aux=new InterventionSchemaGET(intervention);
            result.add(aux);
        }
        Collections.sort(result);
        return result;
    }
    public Set<String> getAllIds() throws IOException {
        Set<String> ids=new HashSet<>();
        for (PumpEntity pump: pumpRepository.findAll()) {
            ids.add(pump.getId());
        }
        return ids;
    }
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
    @Transactional(readOnly=true)
    public List<String> getOptimalRoute(String initialPump) throws IOException {
        PumpEntity pump=pumpRepository.getOne(initialPump);
        Set<String> adjacentPumps=pump.getAdjacentPumps();
        SortedMap<Double,List<List<String>>> sortedMap=new TreeMap<Double, List<List<String>>>();
        Set<String> allPumps=getAllIds();
        boolean completable=findIfCompletable(pump);
        if (!completable) {
            List<String> list=new ArrayList<>();
            list.add("There is no possible route that covers all pumps, add adjacencies if needed");
            return list;
        }
        for (String s:adjacentPumps) {
            PumpEntity toAnalyze=pumpRepository.getOne(s);
            FailureSchema predictedDate=predict(toAnalyze);
            Double factor=Math.abs(new Double(((getDifferenceDays(predictedDate.getEstimatedDate(), new Date())) +1)) )/10.0;
            Double value=factor*getDistance(toAnalyze,pump);
            List<String> toInsert=new ArrayList<>();
            toInsert.add(initialPump);
            toInsert.add(s);
            if (sortedMap.containsKey(value)) {
                List<List<String>> aux=sortedMap.get(value);
                aux.add(toInsert);
                sortedMap.put(value,aux);
            }
            else {
                List<List<String>> aux=new ArrayList<>();
                aux.add(toInsert);
                sortedMap.put(value,aux);
            }
        }
        List<String> result=new ArrayList<>();
        allPumps.remove(initialPump);
        boolean finished=false;
        while (!finished) {
            List<List<String>> pass = sortedMap.get(sortedMap.firstKey());
            Double oldValue=sortedMap.firstKey();
            sortedMap.remove(sortedMap.firstKey());
            for (List<String> individual : pass) {
                Set<String> allPumpsAux=new HashSet<String>(allPumps);
                allPumpsAux.removeAll(individual);
                if (allPumpsAux.size()==0){
                    result=individual;
                    finished=true;
                    break;
                }
                String lastPump=individual.get(individual.size()-1);
                pump=pumpRepository.getOne(lastPump);
                adjacentPumps=pump.getAdjacentPumps();
                for (String s : adjacentPumps) {
                    PumpEntity toAnalyze = pumpRepository.getOne(s);
                    FailureSchema predictedDate = predict(toAnalyze);
                    Double factor = Math.abs(new Double(((getDifferenceDays(predictedDate.getEstimatedDate(), new Date())) +1)) )/ 10.0;
                    Double value = (factor * getDistance(toAnalyze, pump))+oldValue;
                    List<String> toInsert = new ArrayList<>(individual);
                    toInsert.add(s);
                    if (sortedMap.containsKey(value)) {
                        List<List<String>> aux = sortedMap.get(value);
                        aux.add(toInsert);
                        sortedMap.put(value, aux);
                    } else {
                        List<List<String>> aux = new ArrayList<>();
                        aux.add(toInsert);
                        sortedMap.put(value, aux);
                    }
                }
            }
        }
        return result;
    }
    @Transactional(readOnly=true)
    private boolean findIfCompletable(PumpEntity initialPump) throws IOException {
        Set<String> allPumps = getAllIds();
        allPumps.remove(initialPump.getId());
        Set<String> adjacentPumps = initialPump.getAdjacentPumps();
        if (adjacentPumps.size()==0 ) {
            if (allPumps.size()>0) return false;
            else return true;
        }
        List<String> toAnalyze = new ArrayList<>(adjacentPumps);
        while (toAnalyze.size()>0) {
            String s=toAnalyze.get(0);
            toAnalyze.remove(0);
            if (allPumps.contains(s)) {
                PumpEntity pump = pumpRepository.getOne(s);
                allPumps.remove(s);
                toAnalyze.addAll(pump.getAdjacentPumps());
            }
        }
        if (allPumps.size()==0) return true;
        else return false;
    }

    private Double getDistance(PumpEntity toAnalyze, PumpEntity pump) {
        Deg2LonLat toAnalyzeDeg=new Deg2LonLat(toAnalyze.getEastings(),toAnalyze.getNorthings());
        Deg2LonLat pumpDeg=new Deg2LonLat(pump.getEastings(),pump.getNorthings());
        return distance(toAnalyzeDeg.latitude,pumpDeg.latitude,toAnalyzeDeg.longitude,pumpDeg.longitude,0,0)+1;
    }

    public double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    private class Deg2LonLat
    {
        double longitude;
        double latitude;
        public Deg2LonLat(String Lat, String Lon)
        {
            String[] splitLat=Lat.split(" ");
            String[] splitLon=Lon.split(" ");

            Double nDegree = Double.parseDouble(splitLat[0]);
            Double nMinute = Double.parseDouble(splitLat[1]);
            Double nSecond = Double.parseDouble(splitLat[2]);

            Double eDegree = Double.parseDouble(splitLon[0]);
            Double eMinute = Double.parseDouble(splitLon[1]);
            Double eSecond = Double.parseDouble(splitLon[2]);

            longitude = nDegree + (double) nMinute/60 + (double) nSecond/3600;
            latitude = eDegree + (double) eMinute/60 + (double) eSecond/3600;
        }
    }



}
