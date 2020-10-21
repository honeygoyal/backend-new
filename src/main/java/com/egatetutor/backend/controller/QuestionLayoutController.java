package com.egatetutor.backend.controller;

import com.egatetutor.backend.enumType.AttributeType;
import com.egatetutor.backend.model.CoursesDescription;
import com.egatetutor.backend.model.QuestionLayout;
import com.egatetutor.backend.model.ReportDetail;
import com.egatetutor.backend.model.responsemodel.QuestionLayoutResponse;
import com.egatetutor.backend.repository.CoursesDescriptionRepository;
import com.egatetutor.backend.repository.QuestionLayoutRepository;
import com.egatetutor.backend.repository.ReportDetailRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/questionLayout")
public class QuestionLayoutController {

    @Autowired
    private QuestionLayoutRepository questionRepository;

    @Autowired
    private CoursesDescriptionRepository coursesDescriptionRepository;

    @Autowired
    private ReportDetailRepository reportDetailRepository;

    @Autowired
    private Environment env;


    @GetMapping("/getQuestions")
    public ResponseEntity<Map<String, List<QuestionLayoutResponse>>> getQuestionForTest(@RequestParam("courseId") String courseId,
                                                                                        @RequestParam("userId") Long userId)
            throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Optional<CoursesDescription> coursesDescription= coursesDescriptionRepository.findCoursesDescriptionByCourseId(courseId);
        if(!coursesDescription.isPresent()){
            throw new Exception("courseId is not present. Please create test");
        }
        List<QuestionLayout> questionList = questionRepository.findQuestionsById(coursesDescription.get().getId());
        List<QuestionLayoutResponse> questionLayoutResponseList = modelMapper.map(questionList, new TypeToken<List<QuestionLayoutResponse>>() {}.getType());

        Map<String, List<QuestionLayoutResponse>> questionMap  = new HashMap<>();
        List<ReportDetail> reportDetailList = reportDetailRepository.findReportDetailByCompositeId(userId, coursesDescription.get().getId());

        for (QuestionLayoutResponse questionLayout : questionLayoutResponseList) {
            Path quePath = Paths.get(questionLayout.getQuestion() + ".png");
            Path solPath = Paths.get(questionLayout.getSolution() + ".png");
            byte[] imageque = Files.readAllBytes(quePath);
            byte[] imagesol = Files.readAllBytes(solPath);
            String encodedQuestion = Base64.getEncoder().encodeToString(imageque);
            String encodedSolution = Base64.getEncoder().encodeToString(imagesol);
            List<QuestionLayoutResponse> tempList = new ArrayList<>();
            questionLayout.setQuestion(encodedQuestion);
            questionLayout.setSolution(encodedSolution);
            if(reportDetailList!=null){
                Optional<ReportDetail> reportDetail = reportDetailList.stream().filter(p->p.getQuestion_id().getId() == questionLayout.getId()).findFirst();
                reportDetail.ifPresent(detail -> questionLayout.setAnswerSubmitted(detail.getAnswerSubmitted()));
            }
            if (questionMap.containsKey(questionLayout.getSection())) {
                tempList = questionMap.get(questionLayout.getSection());
                tempList.add(questionLayout);
                questionMap.put(questionLayout.getSection(), tempList);
            } else {
                tempList.add(questionLayout);
                questionMap.put(questionLayout.getSection(), tempList);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(questionMap);
    }

    @PostMapping("/upload")
    @ApiOperation(value = "Make a POST request to upload the file",
            produces = "text/plain", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The POST call is Successful"),
            @ApiResponse(code = 500, message = "The POST call is Failed"),
            @ApiResponse(code = 404, message = "The API could not be found")
    })
    public ResponseEntity<String> uploadFile(
            @ApiParam(name = "file", value = "Select the file to Upload", required = true)
            @RequestPart("file") MultipartFile file,
            String courseId
    )  {

        try{
            File testFile = new File("test");
            FileUtils.writeByteArrayToFile(testFile, file.getBytes());
            String BASE_URL = env.getProperty("image_base_url");
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(testFile));
            Iterator bodyElementIterator = xdoc.getBodyElementsIterator();
            List<QuestionLayout> questionsList = new ArrayList();
            QuestionLayout question = new QuestionLayout();
            Optional<CoursesDescription> t = coursesDescriptionRepository.findCoursesDescriptionByCourseId(courseId);
            CoursesDescription coursesDescription = null;
            if (!t.isPresent()) {
                throw new NoSuchElementException("No such course is present");
            }
            coursesDescription = t.get();
            question.setCourseId(coursesDescription);
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = (IBodyElement) bodyElementIterator.next();

                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    for (XWPFTable table : tableList) {
                        int k = 1;
                        for (int i = 0; i < table.getRows().size(); i++) {
                            String typeString = table.getRow(i).getCell(0).getText();
                            String actualText = table.getRow(i).getCell(1).getText();
                            AttributeType type = AttributeType.find(typeString);
                            String s = "";
                            switch (type) {
                                case SECTION:
                                    question.setSection(actualText);
                                    break;
                                case MARKS:
                                    question.setMarks(Double.parseDouble(actualText));
                                    break;
                                case NEGATIVE_MARKS:
                                    question.setNegativeMarks(Double.parseDouble(actualText));
                                    break;
                                case QUESTION_TYPE:
                                    question.setQuestionType(actualText);
                                    break;
                                case QUESTION_LABEL:
                                    question.setQuestionLabel(Integer.parseInt(typeString));
                                    String URL = BASE_URL + "/" + question.getCourseId().getCourseId() + "_" + question.getSection() + "_Question_" + question.getQuestionLabel();
                                    /*TODO: Save Image*/
                                    for (XWPFParagraph p : table.getRow(i).getCell(1).getParagraphs()) {
                                        for (XWPFRun run : p.getRuns()) {
                                            for (XWPFPicture pic : run.getEmbeddedPictures()) {
                                                byte[] pictureData = pic.getPictureData().getData();
                                               // URL = URL+ count[c];
                                                BufferedImage imag=ImageIO.read(new ByteArrayInputStream(pictureData));
                                                ImageIO.write(imag, "png", new File(URL +".png"));
                                            }
                                        }

                                    }
                                    question.setQuestion(URL);
                                    break;
                                case ANSWER:
                                    question.setAnswer(actualText);
                                    break;
                                case SOLUTION:
                                    String SURL = BASE_URL + "/" + question.getCourseId().getCourseId()  + "_" + question.getSection() + "_Solution_" + question.getQuestionLabel();
                                    /*TODO: Save Image*/
                                    for (XWPFParagraph p : table.getRow(i).getCell(1).getParagraphs()) {
                                        for (XWPFRun run : p.getRuns()) {
                                            for (XWPFPicture pic : run.getEmbeddedPictures()) {
                                                byte[] pictureData = pic.getPictureData().getData();
                                                BufferedImage imag=ImageIO.read(new ByteArrayInputStream(pictureData));
                                                ImageIO.write(imag, "png", new File(SURL +".png"));
                                               // break;
                                                //c++;
                                            }
                                        }

                                    }
                                    question.setSolution(SURL);
                                    break;
                                case DIFFICULTY:
                                    question.setQuestionDifficulty(actualText);
                                    break;
                                case VIDEO_LINK:
                                    question.setVideoLink(actualText);
                                    break;
                            }

                            k++;
                            if (k == 10) {
                                questionsList.add(question);
                                k = 1;
                                question = new QuestionLayout();
                                question.setCourseId(coursesDescription);
                            }
                        }
                    }
                }
            }


            for(QuestionLayout q: questionsList){
                questionRepository.save(q);
            }

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Failed ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Done", HttpStatus.OK);
    }



}
