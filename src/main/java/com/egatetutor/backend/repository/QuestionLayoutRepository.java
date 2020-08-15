package com.egatetutor.backend.repository;

import com.egatetutor.backend.model.QuestionLayout;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionLayoutRepository extends CrudRepository<QuestionLayout, Long> {
    /*
    TODO: Add Group by and Order by Condition for Sorting in Question Label
     */
    @Query(value = "SELECT * FROM question_layout q WHERE q.course_Id=:course_Id",nativeQuery = true)
    List<QuestionLayout> findQuestionsById(@Param("course_Id") Long courseId);

}
