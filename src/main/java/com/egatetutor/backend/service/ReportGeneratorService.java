package com.egatetutor.backend.service;

import com.egatetutor.backend.model.ReportOverall;
import com.egatetutor.backend.model.responsemodel.QuestionAnalysis;
import com.egatetutor.backend.model.responsemodel.TestAnalytics;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ReportGeneratorService {
    List<QuestionAnalysis> getQuestionAnalysis(Long userId, Long courseId) throws Exception;
    TestAnalytics getTestAnalytics(Long userId, Long courseId) throws Exception;
    List<ReportOverall> getRankWiseReport(Long courseId);
}
