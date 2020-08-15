package com.egatetutor.backend.controller;


import com.egatetutor.backend.model.CoursesDescription;
import com.egatetutor.backend.model.CoursesOffered;
import com.egatetutor.backend.model.responsemodel.CourseDescStatusResponse;
import com.egatetutor.backend.model.responsemodel.CourseDescriptionResponse;
import com.egatetutor.backend.repository.CoursesOfferedRepository;
import com.egatetutor.backend.service.CoursesDescriptionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/coursesDetail")
public class CoursesDescriptionController {

	@Autowired
	private CoursesDescriptionService coursesDescriptionService;

	@Autowired
	private CoursesOfferedRepository coursesOfferedRepository;

	@Autowired
	private Environment env;

	@GetMapping("/getCoursesDescriptionByExamId")
	public ResponseEntity<List<CourseDescStatusResponse>> getCoursesDescriptionByExamId(@RequestParam("exam_id") Long examId,
													   @RequestParam("email")String email) throws Exception {
		List<CourseDescStatusResponse> coursesDescriptions = coursesDescriptionService.getCourses(examId,email);
		return ResponseEntity.status(HttpStatus.OK).body(coursesDescriptions);
	}


	@GetMapping("/getCoursesDescriptionByExamCode")
	public ResponseEntity<List<CourseDescStatusResponse>> getCoursesDescriptionByExamCode(@RequestParam("exam_code") String examCode,
																				@RequestParam("email")String email)throws Exception  {
		List<CourseDescStatusResponse> coursesDescriptions = coursesDescriptionService.getCoursesByExamCode(examCode,email);
		return ResponseEntity.status(HttpStatus.OK).body(coursesDescriptions);
	}
	@PostMapping("/createTest")
	public ResponseEntity<String> createTest(@Valid @RequestBody CourseDescriptionResponse coursesDescriptionResponse, String examCode) throws Exception
	{
		try {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			CoursesDescription coursesDescription = modelMapper.map(coursesDescriptionResponse, CoursesDescription.class);
			CoursesOffered coursesOffered = coursesOfferedRepository.findByExamCode(examCode);
			if(coursesOffered == null) throw new Exception("Exam Code is not valid");
			coursesDescription.setExamId(coursesOffered);
			coursesDescriptionService.createTest(coursesDescription);
		} catch (Exception ex) {
			return  new ResponseEntity<String>( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new ResponseEntity<String>("DONE", HttpStatus.OK);
	}


}

