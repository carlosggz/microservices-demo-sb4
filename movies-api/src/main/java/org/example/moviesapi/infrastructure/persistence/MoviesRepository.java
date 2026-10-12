package org.example.moviesapi.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends JpaRepository<MovieEntity, String> {

    //optimizar
    Page<MovieEntity> findAll(Specification<MovieEntity> specification, Pageable pageable);
}
