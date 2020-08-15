package com.egatetutor.backend.controller;


import com.egatetutor.backend.model.CoursesDescription;
import com.egatetutor.backend.model.QuestionLayout;
import com.egatetutor.backend.model.ReportOverall;
import com.egatetutor.backend.model.UserInfo;
import com.egatetutor.backend.model.compositekey.ReportOverallPK;
import com.egatetutor.backend.model.responsemodel.CourseDescStatusResponse;
import com.egatetutor.backend.model.responsemodel.ReportOverallRequest;
import com.egatetutor.backend.repository.CoursesDescriptionRepository;
import com.egatetutor.backend.repository.QuestionLayoutRepository;
import com.egatetutor.backend.repository.ReportOverallRepository;
import com.egatetutor.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/reportOverall")
public class ReportOverallController {

    @Autowired
    ReportOverallRepository reportOverallRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    CoursesDescriptionRepository coursesDescriptionRepository;

    @GetMapping("/getReportByUserId")
    public ResponseEntity<ReportOverall> getReportByUserId(@RequestParam("user_id") Long userId,
                                                           @RequestParam("course_id")Long courseId) {
       return ResponseEntity.status(HttpStatus.OK).body(reportOverallRepository.findReportByCompositeId(userId,  courseId)) ;
    }

    @PostMapping("/saveOverallReport")
    public ResponseEntity<ReportOverall> saveOverallReport (ReportOverallRequest reportOverallRequest)
    throws Exception
    {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ReportOverall reportOverall = modelMapper.map(reportOverallRequest, ReportOverall.class);
        Optional<UserInfo> user =  userRepository.findById(reportOverallRequest.getUserId());
        Optional<CoursesDescription> coursesDescription = coursesDescriptionRepository.findById(reportOverallRequest.getCourseId());
        if(!user.isPresent()) {
            throw new Exception("User doesn't exist");
        }
        if(!coursesDescription.isPresent()){
            throw new Exception("Course/Test doesn't exist");
        }
        reportOverall.setCourseId(coursesDescription.get());
        reportOverall.setUserId(user.get());
        reportOverall.setReportOverallPK(new ReportOverallPK(reportOverallRequest.getCourseId(), reportOverallRequest.getUserId()));
        return ResponseEntity.status(HttpStatus.OK).body(reportOverallRepository.save(reportOverall));
    }
}
