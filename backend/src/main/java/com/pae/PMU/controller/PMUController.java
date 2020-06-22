package com.pae.PMU.controller;

import com.pae.PMU.entity.PumpEntity;
import com.pae.PMU.schema.ReportDropDownSchema;
import com.pae.PMU.schema.*;
import com.pae.PMU.service.PMUService;
import com.pae.PMU.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/")
@Api(value = "Stakeholders Recommender API", produces = MediaType.APPLICATION_JSON_VALUE)
public class PMUController {

    @Autowired
    PMUService PMUService;
    @Autowired
    PredictionService PredictionService;


    @RequestMapping(value = "pump", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a pump by ID", notes = "")
    public ResponseEntity getPump(@ApiParam(value = "The pump id.", example = "1", required = true) @RequestParam("pumpId") String pumpId) {
        PumpEntity toRet=PMUService.getPump(pumpId);
        return new ResponseEntity<>(toRet, HttpStatus.OK);
    }

    @RequestMapping(value = "pump", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new pump", notes = "")
    public ResponseEntity postPump(@ApiParam(value = "The pump id.", example = "1", required = true) @RequestBody PumpSchema pump) {
        PMUService.postPump(pump);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "pump", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update pump values", notes = "")
    public ResponseEntity updatePump(@ApiParam(value = "The pump id.", example = "1", required = true) @RequestBody PumpSchema pump) {
        PMUService.updatePump(pump);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "addAdjacentPumps", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add adjacent pumps", notes = "")
    public ResponseEntity updatePump(@ApiParam(value = "The pump to which the other pumps will be added as adjacent.", example = "1", required = true) @RequestParam("pumpId") String pumpId,@ApiParam(value = "The pumps, identified by their id, to add as adjacent.", example = "2", required = true) @RequestBody List<String> adjcentPumps) {
        PMUService.addAdjacentPumps(pumpId,adjcentPumps);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "postIntervention", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Post intervention", notes = "")
    public ResponseEntity postIntervention(@ApiParam(value = "The intervention schema.", required = true) @RequestBody InterventionSchema intervention) {
        PMUService.postIntervention(intervention);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "postMaterial", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Post a material, computes cost per unit", notes = "")
    public ResponseEntity postMaterial(@ApiParam(value = "The material schema.", required = true) @RequestBody MaterialSchema material) {
        PMUService.modifyMaterial(material);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "getInterventions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all interventions of a pump", notes = "")
    public ResponseEntity getInterventions(@ApiParam(value = "The pump id.", example="1",required = true) @RequestParam String pumpId) {
        List<InterventionSchemaGET> res=PMUService.getInterventions(pumpId);
        return new ResponseEntity<List<InterventionSchemaGET>>(res,HttpStatus.OK);
    }

    @RequestMapping(value = "getIntervention", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get interventions of a pump", notes = "")
    public ResponseEntity getIntervention(@ApiParam(value = "The intervention id.", example="1",required = true) @RequestParam String falseId) throws ParseException {
        InterventionSchemaGET res=PMUService.getIntervention(falseId);
        return new ResponseEntity<InterventionSchemaGET>(res,HttpStatus.OK);
    }

    @RequestMapping(value = "getInterventionsBetweenDates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all interventions of a pump between the dates specified", notes = "")
    public ResponseEntity getInterventionsBetweenDates(@ApiParam(value = "The pump id.", required = true) @RequestParam String pumpId, @ApiParam(value = "From this date.",example = "2020-02-02T20:50:12.123Z", required = true) @RequestParam String from, @ApiParam(value = "Up to this date.", required = true,example = "2020-02-02T20:50:12.123Z") @RequestParam String to) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
        Date fromDate = sdf.parse(from);
        Date toDate = sdf.parse(to);
        List<InterventionSchemaGET> res=PMUService.getInterventionsBetweenDates(pumpId,fromDate,toDate);
        return new ResponseEntity<List<InterventionSchemaGET>>(res,HttpStatus.OK);
    }

    @RequestMapping(value = "getPredictedFailureDate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get the predicted failure date", notes = "")
    public ResponseEntity getPredictedFailureDate(@ApiParam(value = "The pump id.", required = true) @RequestParam String pumpId) throws IOException {
        FailureSchema res=PMUService.getPredictedFailureDate(pumpId);
        return new ResponseEntity<FailureSchema>(res,HttpStatus.OK);
    }

    @RequestMapping(value = "getAllIds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all ids of pumps", notes = "")
    public ResponseEntity getAllIds() throws IOException {
        Set<Integer> res=PMUService.getAllIds();
        return new ResponseEntity<Set<Integer>>(res,HttpStatus.OK);
    }

    @RequestMapping(value = "getOptimalRoute", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get optimal route from this pump", notes = "")
    public ResponseEntity getOptimalRoute(@ApiParam(value = "The pump id.", required = true) @RequestParam String pumpId) throws IOException {
        List<String> res= PredictionService.getOptimalRoute(pumpId);
        return new ResponseEntity<List<String>>(res,HttpStatus.OK);
    }

    @RequestMapping(value = "getDropdownValue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get values for dropdown", notes = "")
    public ResponseEntity getDropdownValue(@ApiParam(value = "The pump id.", required = true) @RequestParam String pumpId) throws IOException {
        ReportDropDownSchema res= PMUService.getDropdownValue(pumpId);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @RequestMapping(value = "addDropdownValue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add values for dropdown", notes = "")
    public ResponseEntity addDropdownValue(@ApiParam(value = "The pump id.", required = true) @RequestParam String pumpId,@RequestBody ReportDropDownSchema report) throws IOException {
        PMUService.addDropdownValue(pumpId,report);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "loadDropdown", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add values for dropdown", notes = "")
    public ResponseEntity loadDropdown() throws IOException {
        PMUService.loadDropdowns();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

