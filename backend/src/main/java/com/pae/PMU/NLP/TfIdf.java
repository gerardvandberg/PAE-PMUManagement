package com.pae.PMU.NLP;





import com.pae.PMU.entity.PumpInterventionEntity;

import java.io.IOException;
import java.util.*;


public class TfIdf {

    private Double cutoffParameter = 0.0; //This can be set to different values for different selectivity (more or less keywords)
    private HashMap<String, Integer> corpusFrequency = new HashMap<>();

    public TfIdf(Double cutoff) {
        cutoffParameter=cutoff;
    }

    private Map<String, Integer> tf(List<String> doc) {
        Map<String, Integer> frequency = new HashMap<>();
        for (String s : doc) {
            if (frequency.containsKey(s)) frequency.put(s, frequency.get(s) + 1);
            else {
                frequency.put(s, 1);
                if (corpusFrequency.containsKey(s)) corpusFrequency.put(s, corpusFrequency.get(s) + 1);
                else corpusFrequency.put(s, 1);
            }

        }
        return frequency;
    }


    private double idf(Integer size, Integer frequency) {
        return Math.log(size.doubleValue() / frequency.doubleValue() + 1.0);
    }


    private List<String> analyze(String text, PorterStem stemmer, PreprocessorBasic preprocessor) throws IOException {
        text = preprocessText(text, preprocessor);
        List<String> auxText= new ArrayList<>();
        for (String s:text.split(" ")) {
            auxText.add(stemmer.stem(s)+" ");
        }
        return auxText;
    }

    public Map<String, Map<String, Double>> computeTFIDF(List<PumpInterventionEntity> corpus) throws IOException {
        List<List<String>> docs = new ArrayList<>();
        PorterStem stemmer= new PorterStem();
        PreprocessorBasic preprocessor=new PreprocessorBasic();
        for (PumpInterventionEntity r : corpus) {
            docs.add(analyze(r.getRemarks(),stemmer, preprocessor));
        }
        List<Map<String, Double>> res = tfIdf(docs);
        int counter = 0;
        return tfIdfMatrix(corpus, res, counter);

    }

    private Map<String, Map<String, Double>> tfIdfMatrix(List<PumpInterventionEntity> corpus, List<Map<String, Double>> res, int counter) {
        Map<String, Map<String, Double>> ret = new HashMap<>();
        for (PumpInterventionEntity r : corpus) {
            ret.put(r.getInterventionDate().toString()+r.getPumpId(), res.get(counter));
            counter++;
        }
        return ret;
    }

    private List<Map<String, Double>> tfIdf(List<List<String>> docs) {
        List<Map<String, Double>> tfidfComputed = new ArrayList<>();
        List<Map<String, Integer>> wordBag = new ArrayList<>();
        for (List<String> doc : docs) {
            wordBag.add(tf(doc));
        }
        int i = 0;
        for (List<String> doc : docs) {
            HashMap<String, Double> aux = new HashMap<>();
            for (String s : doc) {
                Double idf = idf(docs.size(), corpusFrequency.get(s));
                Integer tf = wordBag.get(i).get(s);
                Double tfidf = idf * tf;
                if (tfidf >= cutoffParameter && s.length() > 1) aux.put(s, tfidf);
            }
            tfidfComputed.add(aux);
            ++i;
        }
        return tfidfComputed;

    }

    private String preprocessText(String text, PreprocessorBasic preprocessor) throws IOException {
        text = preprocessor.preprocess(text);
        return text;
    }


    public HashMap<String, Integer> getCorpusFrequency() {
        return corpusFrequency;
    }

    public void setCorpusFrequency(HashMap<String, Integer> corpusFrequency) {
        this.corpusFrequency = corpusFrequency;
    }

    public Double getCutoffParameter() {
        return cutoffParameter;
    }

    public void setCutoffParameter(Double cutoffParameter) {
        this.cutoffParameter = cutoffParameter;
    }



}