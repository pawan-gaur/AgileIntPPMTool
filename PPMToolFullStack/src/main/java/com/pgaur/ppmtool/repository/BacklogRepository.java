package com.pgaur.ppmtool.repository;

import com.pgaur.ppmtool.domain.Backlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long> {

    Backlog findByProjectIdentifier(String Identifier);

}
