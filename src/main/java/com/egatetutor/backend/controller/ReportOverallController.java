package com.egatetutor.backend.controller;


import com.egatetutor.backend.enumType.CoursesStatus;
import com.egatetutor.backend.model.*;
import com.egatetutor.backend.model.compositekey.ReportOverallPK;
import com.egatetutor.backend.model.responsemodel.ReportOverallRequest;
import com.egatetutor.backend.model.responsemodel.TestAnalytics;
import com.egatetutor.backend.repository.*;
import com.egatetutor.backend.service.ReportGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/reportOverall")
public class ReportOverallController {

    @Autowired
    ReportOverallRepository reportOverallRepository;

    @Autowired
    ReportGeneratorService reportGeneratorService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    CoursesDescriptionRepository coursesDescriptionRepository;

    @GetMapping("/getOverallReportByUserId")
    public ResponseEntity<TestAnalytics> getReportByUserId(@RequestParam("user_id") Long userId,
                                                           @RequestParam("course_id")Long courseId)
    throws Exception
    {

        ReportOverall reportOverall = reportOverallRepository.findReportByCompositeId(
               userId, courseId);
        if(reportOverall == null){
            throw new Exception("Composite Id of userId:"+ userId +"& courseId:"+courseId+" doesn't exist");
        }
        if(!reportOverall.getStatus().equals(CoursesStatus.COMPLETED.name())){
            throw new Exception("Exam is not complete");
        }
       TestAnalytics testAnalytics = reportGeneratorService.getTestAnalytics(userId, courseId);
        Optional<CoursesDescription> coursesDescription = coursesDescriptionRepository.findById(reportOverall.getCourseId().getId());
        if(!coursesDescription.isPresent()){
            throw new Exception("Course/Test doesn't exist");
        }
        testAnalytics.setTotalMarks(coursesDescription.get().getTotalMarks());
        /*Set the User Rank*/
        List<ReportOverall> reportOverallList = reportGeneratorService.getRankWiseReport(courseId);
        Optional<ReportOverall> reportForRank = reportOverallList.stream().
                filter(p-> (p.getUserId().getId() == userId)).findFirst();
      reportForRank.ifPresent(overall -> testAnalytics.setRank(overall.getUserRank()));
        return ResponseEntity.status(HttpStatus.OK).body(testAnalytics) ;
    }

    @GetMapping("/getTopRank")
    public ResponseEntity<List<ReportOverall>> getTopRank(@RequestParam("course_id")Long courseId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(reportGeneratorService.getRankWiseReport(courseId));
    }
    @PostMapping("/saveOverallReport")
    public ResponseEntity<String> saveOverallReport (@Valid @RequestBody ReportOverallRequest reportOverallRequest)
    throws Exception
    {
        Optional<UserInfo> user =  userRepository.findById(reportOverallRequest.getUserId());
        Optional<CoursesDescription> coursesDescription = coursesDescriptionRepository.findById(reportOverallRequest.getCourseId());
        if(!user.isPresent()) {
            throw new Exception("User doesn't exist");
        }
        if(!coursesDescription.isPresent()){
            throw new Exception("Course/Test doesn't exist");
        }
        ReportOverall reportOverall = reportOverallRepository.findReportByCompositeId(
                reportOverallRequest.getUserId(), reportOverallRequest.getCourseId());
      if(reportOverall == null){
          reportOverall = new ReportOverall();
          reportOverall.setCourseId(coursesDescription.get());
          reportOverall.setUserId(user.get());
          reportOverall.setReportOverallPK(new ReportOverallPK(reportOverallRequest.getCourseId(), reportOverallRequest.getUserId()));
      }
        reportOverall.setStatus(reportOverallRequest.getStatus());
        reportOverall.setTotalTime(reportOverallRequest.getTotalTime());
        reportOverallRepository.save(reportOverall);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
