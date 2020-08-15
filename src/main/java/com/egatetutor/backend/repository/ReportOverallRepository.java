package com.egatetutor.backend.repository;
import com.egatetutor.backend.model.ReportOverall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportOverallRepository extends CrudRepository<ReportOverall, Long> {

    @Query(value = "Select * from report_overall R where R.user_id=:user_id and R.course_id=:course_id",nativeQuery=true)
    ReportOverall findReportByCompositeId(@Param("user_id")Long userId, @Param("course_id") Long course_id);


}