package com.egatetutor.backend.service;

import com.egatetutor.backend.enumType.QuestionType;
import com.egatetutor.backend.model.QuestionLayout;
import com.egatetutor.backend.model.ReportDetail;
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
        List<ReportDetail> reportDetailList = reportDetailRepository.findReportDetailByCompositeId(userId, courseId);
        if (reportDetailList == null) {
            throw new Exception("QuestWise report is not yet generated");
        }
        List<QuestionLayout> questionLayoutList = questionLayoutRepository.findQuestionsById(courseId);
        List<QuestionAnalysis> questionAnalysesList = new ArrayList<>();

        for (QuestionLayout question : questionLayoutList) {
            QuestionAnalysis questionAnalysis = new QuestionAnalysis();
            questionAnalysis.setQuestionId(question.getId());
            questionAnalysis.setDifficultyLevel(question.getQuestionDifficulty());
            Optional<ReportDetail> reportDetail = reportDetailList.stream().
                    filter(p -> p.getQuestion_id().equals(question)).
                    findFirst();
            if (!reportDetail.isPresent()) {
                questionAnalysis.setYourTime("0");
                questionAnalysis.setYourAttempt(null);
                questionAnalysis.setMarkSecured(0d);
            } else {
                questionAnalysis.setYourTime(reportDetail.get().getTimeTaken());
                if (reportDetail.get().getAnswerSubmitted() == null) {
                    questionAnalysis.setMarkSecured(0d);
                    questionAnalysis.setCorrect(false);
                } else {
                    boolean isCorrect = checkCorrectAns(question.getQuestionType(), question.getAnswer(), reportDetail.get().getAnswerSubmitted());
                    double markSecured = (isCorrect) ? question.getMarks() : question.getNegativeMarks();
                    questionAnalysis.setMarkSecured(markSecured);
                    questionAnalysis.setCorrect(isCorrect);
                }
            }
            List<ReportDetail> questWiseAllUserReport = reportDetailRepository.findAllReportDetailByQuestion(question.getId(), courseId);
            questionAnalysis.setTotalAttempt(questWiseAllUserReport.size()); /* Number of Report generated for user*/

            List<ReportDetail> correctSolutionReport = questWiseAllUserReport.stream().filter(
                    p -> checkCorrectAns(question.getQuestionType(), question.getAnswer(), p.getAnswerSubmitted())
            ).collect(Collectors.toList());
            questionAnalysis.setCorrectAttempt(correctSolutionReport.size());
            int unAttemptQ = (int) questWiseAllUserReport.stream().filter(
                    p -> p.getAnswerSubmitted() == null
            ).count();
            questionAnalysis.setUnAttempt(unAttemptQ);

            int inCorrect = questionAnalysis.getTotalAttempt() - (questionAnalysis.getCorrectAttempt() + unAttemptQ);
            questionAnalysis.setInCorrectAttempt(inCorrect);

            ReportDetail minTimeTakenReport = correctSolutionReport.stream().min(Comparator.comparing(ReportDetail::getTimeTaken))
                    .orElseThrow(NoSuchElementException::new);
            questionAnalysis.setTopperTime(minTimeTakenReport.getTimeTaken());
            OptionalDouble averageTime = questWiseAllUserReport.stream().mapToDouble(p -> Double.parseDouble(p.getTimeTaken()))
                    .average();
                    //.collect(Collectors.toList());
            //double averagTime = timeTakenList.stream().mapToDouble(val -> val).average().orElse(0.0);
            questionAnalysis.setAverageTime(averageTime + "");
            questionAnalysesList.add(questionAnalysis);
        }
        return questionAnalysesList;
    }

    @Override
    public TestAnalytics getTestAnalytics(Long userId, Long courseId) throws Exception {
        List<QuestionAnalysis> questionAnalysisList = getQuestionAnalysis(userId, courseId);
        TestAnalytics testAnalytics = new TestAnalytics();
        int correctAns  = (int) questionAnalysisList.stream().filter(QuestionAnalysis::isCorrect).count();
        int unAttempt = (int) questionAnalysisList.stream().filter(p->p.getYourAttempt() == null).count();
        int totalQ = questionAnalysisList.size();
        testAnalytics.setCorrect(correctAns);
        testAnalytics.setInCorrect(totalQ-(correctAns+unAttempt));
        testAnalytics.setUnAttempt(unAttempt);
        double markSecured = questionAnalysisList.stream().mapToDouble(QuestionAnalysis::getMarkSecured)
                .sum();
        testAnalytics.setMarksSecured(markSecured);


        return testAnalytics;
    }

    private boolean checkCorrectAns(String qType, String answer, String answerSubmitted) {
        if (answerSubmitted == null) return false;
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
