package com.egatetutor.backend.service;

import com.egatetutor.backend.enumType.CoursesStatus;
import com.egatetutor.backend.enumType.QuestionStatus;
import com.egatetutor.backend.enumType.QuestionType;
import com.egatetutor.backend.model.QuestionLayout;
import com.egatetutor.backend.model.ReportDetail;
import com.egatetutor.backend.model.ReportOverall;
import com.egatetutor.backend.model.responsemodel.QuestionAnalysis;
import com.egatetutor.backend.model.responsemodel.TestAnalytics;
import com.egatetutor.backend.repository.QuestionLayoutRepository;
import com.egatetutor.backend.repository.ReportDetailRepository;
import com.egatetutor.backend.repository.ReportOverallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportGeneratorServiceImpl implements ReportGeneratorService {

    @Autowired
    ReportDetailRepository reportDetailRepository;

    @Autowired
    ReportOverallRepository reportOverallRepository;

    @Autowired
    QuestionLayoutRepository questionLayoutRepository;

    @Override
    public List<QuestionAnalysis> getQuestionAnalysis(Long userId, Long courseId) throws Exception {
        List<ReportDetail> reportDetailList = reportDetailRepository.findReportDetailByCompositeId(userId, courseId); /*User Report detail list for all question of courseId*/
        ReportOverall reportOverall = reportOverallRepository.findReportByCompositeId(userId, courseId);
        if (reportOverall == null || !reportOverall.getStatus().equals(CoursesStatus.COMPLETE.name()))
            throw new Exception("Exam is not finished yet");
        if (reportDetailList == null) {
            throw new Exception("QuestWise report is not yet generated");
        }
        List<QuestionLayout> questionLayoutList = questionLayoutRepository.findQuestionsById(courseId);        /*Question List of Course Id */
        List<QuestionAnalysis> questionAnalysesList = new ArrayList<>();
        /*TODO: incorrect Attempt is not showing correctly, marks calculated! */
        for (QuestionLayout question : questionLayoutList) {
            QuestionAnalysis questionAnalysis = new QuestionAnalysis();
            questionAnalysis.setQuestionId(question.getId());
            questionAnalysis.setDifficultyLevel(question.getQuestionDifficulty());
            Optional<ReportDetail> reportDetail = reportDetailList.stream().                               /* finding question specific report for userId*/
                    filter(p -> p.getQuestion_id().equals(question)).
                    findFirst();
            if (!reportDetail.isPresent()) {
                questionAnalysis.setYourTime("0");
                questionAnalysis.setYourAttempt(QuestionStatus.NO_ANS.name());
                questionAnalysis.setCorrect(false);
                questionAnalysis.setMarkSecured(0d);
            } else {
                questionAnalysis.setYourTime(reportDetail.get().getTimeTaken());
                if (reportDetail.get().getQuestionStatus().equals(QuestionStatus.NO_ANS.name())) {
                    questionAnalysis.setMarkSecured(0d);
                    questionAnalysis.setCorrect(false);
                    questionAnalysis.setYourAttempt(QuestionStatus.NO_ANS.name());
                } else {
                    boolean isCorrect = checkCorrectAns(question.getQuestionType(), question.getAnswer(), reportDetail.get().getAnswerSubmitted());
                    double markSecured = (isCorrect) ? question.getMarks() : question.getNegativeMarks();
                    questionAnalysis.setMarkSecured(markSecured);
                    questionAnalysis.setCorrect(isCorrect);
                    questionAnalysis.setYourAttempt(reportDetail.get().getQuestionStatus());
                }
            }
            List<ReportDetail> questWiseAllUserReport = reportDetailRepository.findAllReportDetailByQuestion(question.getId(), courseId);
            questionAnalysis.setTotalAttempt(questWiseAllUserReport.size()); /* Number of Report generated for user*/

            List<ReportDetail> correctSolutionReport = questWiseAllUserReport.stream().filter(
                    p -> checkCorrectAns(question.getQuestionType(), question.getAnswer(), p.getAnswerSubmitted())
            ).collect(Collectors.toList());
            questionAnalysis.setCorrectAttempt(correctSolutionReport.size());
            int unAttemptQ = (int) questWiseAllUserReport.stream().filter(
                    p -> (p.getAnswerSubmitted() == null || p.getAnswerSubmitted().isEmpty())
            ).count();
            questionAnalysis.setUnAttempt(unAttemptQ);

            int inCorrect = questionAnalysis.getTotalAttempt() - (questionAnalysis.getCorrectAttempt() + unAttemptQ);
            questionAnalysis.setInCorrectAttempt(inCorrect);

            ReportDetail minTimeTakenReport = correctSolutionReport.stream().min(Comparator.comparing(ReportDetail::getTimeTaken))
                    .orElse(null);
            questionAnalysis.setTopperTime(minTimeTakenReport == null ? null : minTimeTakenReport.getTimeTaken());
           OptionalDouble averageTimeOptional = questWiseAllUserReport.stream().mapToDouble(p -> Double.parseDouble(p.getTimeTaken())).average();
            if(averageTimeOptional.isPresent()) {  double averageTime = averageTimeOptional.getAsDouble();
            questionAnalysis.setAverageTime(averageTime+"");}
            questionAnalysesList.add(questionAnalysis);
        }
        return questionAnalysesList;
    }

    @Override
    public TestAnalytics getTestAnalytics(Long userId, Long courseId) throws Exception {
        List<QuestionAnalysis> questionAnalysisList = getQuestionAnalysis(userId, courseId);
        TestAnalytics testAnalytics = new TestAnalytics();
        int correctAns = (int) questionAnalysisList.stream().filter(QuestionAnalysis::isCorrect).count();
        int unAttempt = (int) questionAnalysisList.stream().filter(p -> p.getYourAttempt().equals(QuestionStatus.NO_ANS.name())).count();
        int totalQ = questionAnalysisList.size();
        testAnalytics.setCorrect(correctAns);
        testAnalytics.setInCorrect(totalQ - (correctAns + unAttempt));
        testAnalytics.setUnAttempt(unAttempt);
        double markSecured = questionAnalysisList.stream().mapToDouble(QuestionAnalysis::getMarkSecured)
                .sum();
        testAnalytics.setMarksSecured(markSecured);


        return testAnalytics;
    }

    private boolean checkCorrectAns(String qType, String answer, String answerSubmitted) {
        if (answerSubmitted == null || answerSubmitted.isEmpty()) return false;
        QuestionType questionType = QuestionType.find(qType);
        boolean isCorrect = false;
        switch (questionType) {
            case MCQ:
                isCorrect = answer.equalsIgnoreCase(answerSubmitted);
                break;
            case MSQ:
                String a1[] = answer.split("[,]", 0);
                String a2[] = answerSubmitted.split("[,]", 0);
                Arrays.sort(a1);
                Arrays.sort(a2);
                isCorrect = Arrays.equals(a1, a2);
                break;
            case NAT:
                answer = answer.replaceAll("[\\[\\](){}]", "");
                String ans[] = answer.split("[,]", 0);
                double lowerLimit = Double.parseDouble(ans[0]);
                double upperLimit = Double.parseDouble(ans[1]);
                double ansSub = Double.parseDouble(answerSubmitted);
                isCorrect = ansSub <= upperLimit && ansSub >= lowerLimit;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + questionType);
        }
        return isCorrect;
    }
}
