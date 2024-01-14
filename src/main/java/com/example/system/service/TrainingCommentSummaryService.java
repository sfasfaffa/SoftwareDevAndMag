package com.example.system.service;

import com.example.system.dao.TrainingCommentSummaryDao;
import com.example.system.dao.TrainingPlanDao;
import com.example.system.entity.TrainingCommentSummary;
import com.example.system.entity.TrainingPlan;
import com.example.system.model.Result;
import com.example.system.request.TrainingCommentSummaryRequest;
import com.example.system.response.TrainingCommentSummaryResponse;
import com.example.system.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingCommentSummaryService {
    @Autowired
    TrainingPlanDao trainingPlanDao;
    @Autowired
    TrainingCommentSummaryDao trainingCommentSummaryDao;
    public Result create(TrainingCommentSummaryRequest trainingCommentSummaryRequest){
        TrainingCommentSummary trainingCommentSummary = new TrainingCommentSummary();
        trainingCommentSummary.setSatisfactionSummary(trainingCommentSummaryRequest.getSatisfactionSummary());
        trainingCommentSummary.setSuggestionCollect(trainingCommentSummaryRequest.getSuggestionCollect());
        trainingCommentSummary.setName(trainingCommentSummaryRequest.getName());
//        System.out.println(trainingCommentSummary.getSatisfactionSummary());
//        System.out.println(trainingCommentSummary.getSuggestionCollect());
        System.out.println("create");
        trainingCommentSummaryDao.save(trainingCommentSummary);
        return ResultUtil.success();
    }

    public Result get() {
        List<TrainingCommentSummary> trainingCommentSummaries= trainingCommentSummaryDao.findAll();
        List<TrainingCommentSummaryResponse> trainingCommentSummaryResponses = new ArrayList<>();
        for(TrainingCommentSummary trainingCommentSummary:trainingCommentSummaries){
            TrainingCommentSummaryResponse response = TrainingCommentSummaryResponse
                    .builder()
                    .satisfactionSummary(trainingCommentSummary.getSatisfactionSummary())
                    .suggestionCollect(trainingCommentSummary.getSuggestionCollect())
                    .name(trainingCommentSummary.getName())
                    .build();
            //                    .trainingPlanName(trainingCommentSummary.getTrainingPlan().getCourseName())
            try{
                if (trainingCommentSummary.getTrainingPlan().getCourseName() != null){
                    response.setTrainingPlanName(trainingCommentSummary.getTrainingPlan().getCourseName());
                }

            }catch (Exception e){

            }
            trainingCommentSummaryResponses.add(response);
        }
        return ResultUtil.success(trainingCommentSummaryResponses);
    }
}
