package com.egatetutor.backend.controller;

import com.egatetutor.backend.model.CoursesOffered;
import com.egatetutor.backend.model.SubscriptionDetails;
import com.egatetutor.backend.model.UserInfo;
import com.egatetutor.backend.repository.CoursesOfferedRepository;
import com.egatetutor.backend.repository.SubscriptionRepository;
import com.egatetutor.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptionDetail")
public class SubscriptionDetailController {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoursesOfferedRepository coursesOfferedRepository;

    @PostMapping("/addNewSubscription")
    public SubscriptionDetails addNewSubscription(String emailId, Long examId) throws Exception
    {
        SubscriptionDetails subscriptionDetails = new SubscriptionDetails();
        UserInfo userInfo = userRepository.findByEmailId(emailId);
        if(userInfo == null) throw new Exception("User doesn't exist with this mail id");
        subscriptionDetails.setUserId(userInfo);
        Optional<CoursesOffered> coursesOffered = coursesOfferedRepository.findById(examId);
        if(!coursesOffered.isPresent()) throw new Exception("Courses Offered doesn't exist with exam Id: "+ examId);
        subscriptionDetails.setCourseOfferedId(coursesOffered.get());
        return subscriptionRepository.save(subscriptionDetails);
    }
}
