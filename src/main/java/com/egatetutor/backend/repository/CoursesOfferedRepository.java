package com.egatetutor.backend.repository;

import com.egatetutor.backend.model.CoursesOffered;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoursesOfferedRepository extends CrudRepository<CoursesOffered, Long> {

	@Query(value = "SELECT * FROM courses_offered e where e.exam_id = ?1",nativeQuery = true)
    CoursesOffered findByExamCode(String service);

	@Query(value = "SELECT * FROM courses_offered",nativeQuery = true)
	CoursesOffered[] findCoursesOffereds();

	@Query(value = "SELECT * from courses_offered e where e.branch=?", nativeQuery = true)
	CoursesOffered[]  findByBranch(@Param("branch")String branch);

}
