package com.egatetutor.backend.repository;
import com.egatetutor.backend.model.ReportOverall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportOverallRepository extends CrudRepository<ReportOverall, Long> {

    @Query(value = "Select * from report_overall R where R.user_id=:user_id and R.course_id=:course_id",nativeQuery=true)
    ReportOverall findReportByCompositeId(@Param("user_id")Long userId, @Param("course_id") Long course_id);

//    @Query(value = "SELECT user_id, score, ROW_NUMBER() OVER(ORDER BY score desc) user_rank " +
//            "FROM report_overall WHERE STATUS = 'COMPLETE' AND course_id =:course_id", nativeQuery = true)
//    List<ReportOverall> findRankByCourseId(@Param("course_id") Long course_id);

}