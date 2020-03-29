package com.pgaur.ppmtool.service;

import com.pgaur.ppmtool.domain.Project;
import com.pgaur.ppmtool.exception.ProjectIdException;
import com.pgaur.ppmtool.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception ex){
            throw new ProjectIdException("Project ID : "+project.getProjectIdentifier().toUpperCase()+" already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Project ID : "+projectId+" doesn't exists");
        }

        return projectRepository.findByProjectIdentifier(projectId.toUpperCase());
    }

    public List<Project> findAllProject(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Can't delete Project ID : "+projectId+" doesn't exists");
        }

        projectRepository.delete(project);
    }

}
