package com.egatetutor.backend.controller;


import com.egatetutor.backend.enumType.CoursesStatus;
import com.egatetutor.backend.model.*;
import com.egatetutor.backend.model.compositekey.ReportDetailPK;
import com.egatetutor.backend.model.compositekey.ReportOverallPK;
import com.egatetutor.backend.model.responsemodel.ReportDetailRequest;
import com.egatetutor.backend.model.responsemodel.ReportOverallRequest;
import com.egatetutor.backend.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/reportDetail")
public class ReportDetailController {
    @Autowired
    ReportDetailRepository reportDetailRepository;

    @Autowired
    ReportOverallRepository reportOverallRepository;

    @Autowired
    QuestionLayoutRepository questionLayoutRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoursesDescriptionRepository coursesDescriptionRepository;

    @PostMapping("/saveReportQuestionWise")
    public ResponseEntity<ReportDetail> saveReportQuestionwise (@Valid @RequestBody ReportDetailRequest reportDetailRequest)
            throws Exception
    {
        ModelMapper modelMapper = new ModelMapper();
         modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
         ReportDetail reportDetail = modelMapper.map(reportDetailRequest, ReportDetail.class);
         Optional<UserInfo> user =  userRepository.findById(reportDetailRequest.getUserId());
         Optional<CoursesDescription> coursesDescription = coursesDescriptionRepository.findById(reportDetailRequest.getCourseId());
        Optional<QuestionLayout> questionLayout = questionLayoutRepository.findById(reportDetailRequest.getQuestionId());
        if(!questionLayout.isPresent()){throw new Exception("Question id is invalid");}
        if(!user.isPresent()) {
            throw new Exception("User doesn't exist");
        }
        if(!coursesDescription.isPresent()){
            throw new Exception("Course/Test doesn't exist");
        }
        ReportOverall reportOverall = reportOverallRepository.findReportByCompositeId(reportDetailRequest.getUserId(),
                reportDetailRequest.getCourseId());
        if(reportOverall == null ){
            reportOverall = new ReportOverall();
            reportOverall.setUserId(user.get());
            reportOverall.setCourseId(coursesDescription.get());
            reportOverall.setStatus(CoursesStatus.PROGRESS.name());
            reportOverall.setTotalTime(reportDetail.getTimeTaken());
            reportOverall.setReportOverallPK(new ReportOverallPK(coursesDescription.get().getId(), user.get().getId()) );
            reportOverallRepository.save(reportOverall);
        }
        reportDetail.setReportId(reportOverall);
       reportDetail.setQuestion_id(questionLayout.get());
       reportDetail.setReportDetailPK(new ReportDetailPK(reportOverall.getReportOverallPK(), questionLayout.get().getId()));
//        reportOverall.setUserId(user.get());
     return ResponseEntity.status(HttpStatus.OK).body(reportDetailRepository.save(reportDetail));
    }
}
