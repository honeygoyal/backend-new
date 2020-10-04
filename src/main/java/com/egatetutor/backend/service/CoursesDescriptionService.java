package com.egatetutor.backend.service;


import com.egatetutor.backend.model.CoursesDescription;
import com.egatetutor.backend.model.CoursesOffered;
import com.egatetutor.backend.model.responsemodel.CourseDescStatusResponse;

import java.util.List;

public interface CoursesDescriptionService {
	List<CourseDescStatusResponse> getCourses(Long courseOfferedId, String email) throws Exception;
	void createTest(CoursesDescription coursesDescription) throws Exception;

	List<CourseDescStatusResponse> getCoursesByExamCode(String examCode, String email) throws Exception;
	List<String> getCourseIdListForAdmin(String examCode) throws Exception;
}
