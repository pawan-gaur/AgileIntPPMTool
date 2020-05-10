package com.pgaur.ppmtool.repository;

import com.pgaur.ppmtool.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByProjectIdentifier(String projectID);

    List<Project> findAllByIsActive(boolean status);
}
