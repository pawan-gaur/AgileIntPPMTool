package com.pgaur.ppmtool.controller;

import com.pgaur.ppmtool.domain.Project;
import com.pgaur.ppmtool.service.MapValidationErrorService;
import com.pgaur.ppmtool.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin()
public class ProjectController {

    Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationSerivce(result);
        if (errorMap != null) return errorMap;

        Project savedProject = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProject(Principal principal) {
        logger.info("get all projects for : {}", principal.getName());
        List<Project> projectList = projectService.findAllProject(principal.getName());
        logger.info("total projects : {}", projectList.size());
        return new ResponseEntity<>(projectList, HttpStatus.OK);
    }

    @GetMapping("/all/{status}")
    public ResponseEntity<?> getAllActiveProject(@PathVariable boolean status) {
        List<Project> projectList = projectService.findAllProjectByActive(true);
        return new ResponseEntity<>(projectList, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProjectById(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<>("Project ID : " + projectId + " is deleted Successfully", HttpStatus.OK);
    }

}
