package com.pgaur.ppmtool.controller;

import com.pgaur.ppmtool.domain.ProjectTask;
import com.pgaur.ppmtool.service.MapValidationErrorService;
import com.pgaur.ppmtool.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;


    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addProjectToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                 BindingResult result, @PathVariable String backlogId) {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationSerivce(result);
        if (errorMap != null) return errorMap;

        ProjectTask createdProjectTask = projectTaskService.addProjectTask(backlogId, projectTask);
        return new ResponseEntity<>(createdProjectTask, HttpStatus.CREATED);

    }

    @GetMapping("/{backlogId}")
    public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String backlogId) {
        return new ResponseEntity<>(projectTaskService.findBacklogById(backlogId), HttpStatus.OK);
    }

    @GetMapping("/{backlogId}/{projectId}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String projectId) {
        ProjectTask projectTask = projectTaskService.findProjectBySequence(backlogId, projectId);
        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{projectId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask updatedProjectTask, BindingResult result,
                                               @PathVariable String backlogId, @PathVariable String projectId) {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationSerivce(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(updatedProjectTask, backlogId, projectId);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{projectId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String projectId) {
        projectTaskService.deleteByProjectSequence(backlogId, projectId);
        return new ResponseEntity<>("Project Task " + projectId + " deleted successfully", HttpStatus.OK);
    }
}