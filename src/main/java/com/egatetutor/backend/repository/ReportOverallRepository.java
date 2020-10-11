package com.egatetutor.backend.repository;
import com.egatetutor.backend.model.ReportOverall;
import com.egatetutor.backend.model.responsemodel.ReportOverallRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportOverallRepository extends CrudRepository<ReportOverall, Long> {

    @Query(value = "Select * from report_overall R where R.user_id=:user_id and R.course_id=:course_id",nativeQuery=true)
    ReportOverall findReportByCompositeId(@Param("user_id")Long userId, @Param("course_id") Long course_id);

    @Query(value = "SELECT *" +
            "FROM report_overall R WHERE R.STATUS = 'COMPLETED' AND R.course_id =:course_id", nativeQuery = true)
    List<ReportOverall> findRankByCourseId(@Param("course_id") Long course_id);

    @Modifying
    @Query(value="UPDATE report_overall R SET R.score=:score WHERE R.user_id=:user_id and R.course_id=:course_id",nativeQuery = true)
    void udateReportOverall(@Param("score") double score ,@Param("user_id") Long user_id,@Param("course_id") Long course_id);


}