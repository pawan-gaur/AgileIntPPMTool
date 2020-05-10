package com.pgaur.ppmtool.repository;

import com.pgaur.ppmtool.domain.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {

    List<ProjectTask> findByProjectIdentifierOrderByPriority(String backlogId);

    ProjectTask findByProjectSequence(String projectId);

}
