package com.pgaur.ppmtool.service;

import com.pgaur.ppmtool.domain.Backlog;
import com.pgaur.ppmtool.domain.ProjectTask;
import com.pgaur.ppmtool.exception.ProjectNotFoundException;
import com.pgaur.ppmtool.repository.BacklogRepository;
import com.pgaur.ppmtool.repository.ProjectTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    Logger logger = LoggerFactory.getLogger(ProjectTaskService.class);

    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String userName) {

//        try {
        //PTs to be added to a specific project, project != null, BL exists
        //Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, userName).getBacklog();
                //set the bl to pt
        projectTask.setBacklog(backlog);
        //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
        Integer backlogSequence = backlog.getPTSequence();
        // Update the BL SEQUENCE
        backlogSequence++;

        backlog.setPTSequence(backlogSequence);

        //Add Sequence to Project Task
        projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //INITIAL priority when priority null

        //INITIAL status when status is null
        if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
            projectTask.setStatus("TO_DO");
        }

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) { //In the future we need projectTask.getPriority()== 0 to handle the form
            projectTask.setPriority(3);
        }

        return projectTaskRepository.save(projectTask);
//        } catch (Exception e) {
//            throw new ProjectNotFoundException("Project not Found");
//        }

    }

    public List<ProjectTask> findBacklogById(String backlogId, String userName) {

        projectService.findProjectByIdentifier(backlogId, userName);

        List<ProjectTask> projectTaskList = projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);

        if (projectTaskList.isEmpty()) {
            throw new ProjectNotFoundException("Project with ID : " + backlogId + " doesn't exists");
        }
        return projectTaskList;
    }

    public ProjectTask findProjectBySequence(String backlogId, String projectId, String userName) {

        //make sure we are searching on an existing backlog
//        Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
//        if (backlog == null) {
//            logger.error("Project with ID : {} doesn't exists", backlogId);
//            throw new ProjectNotFoundException("Project with ID : " + backlogId + " doesn't exists");
//        }
        projectService.findProjectByIdentifier(backlogId, userName);

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectId);
        if (projectTask == null) {
            logger.error("Project Task : {} doesn't exists", projectId);
            throw new ProjectNotFoundException("Project Task : " + projectId + " not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if (!projectTask.getProjectIdentifier().equals(backlogId)) {
            logger.error("Project Task : {} doesn't exists in project : {}", projectId, backlogId);
            throw new ProjectNotFoundException("Project Task : " + projectId + " doesn't exists in project :" + backlogId);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String projectId, String userName) {

        //ProjectTask projectTask = projectTaskRepository.findByProjectSequence(updatedTask.getProjectSequence());
        ProjectTask projectTask = findProjectBySequence(backlogId, projectId, userName);
        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deleteByProjectSequence(String backlogId, String projectId, String userName) {

        ProjectTask projectTask = findProjectBySequence(backlogId, projectId, userName);
        projectTaskRepository.delete(projectTask);

    }


}