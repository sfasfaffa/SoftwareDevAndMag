package com.example.system.service;

import com.example.system.dao.*;
import com.example.system.entity.*;
import com.example.system.model.Result;
import com.example.system.model.ResultEnum;
import com.example.system.request.TrainingPlanRequest;
import com.example.system.response.TrainingPlanResponse;
import com.example.system.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingPlanService {
    @Autowired
    private TrainingPlanDao trainingPlanDao;
    @Autowired
    private TrainingContentDao trainingContentDao;
    @Autowired
    private TrainingFeeDao trainingFeeDao;
    @Autowired
    private LecturerDao lecturerDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private EmailDao emailDao;
    @Autowired
    private ExecutorDao executorDao;
    @Autowired
    private SignInTableDao signInTableDao;

    public Result<String> createTrainingPlan(TrainingPlanRequest trainingPlanRequest) {
        TrainingPlan trainingPlan = TrainingPlan
                .builder()
                .courseName(trainingPlanRequest.getCourseName())
                .trainingNum(trainingPlanRequest.getTrainingNum())
                .startTime(trainingPlanRequest.getStartTime())
                .endTime(trainingPlanRequest.getEndTime())
                .trainingPlace(trainingPlanRequest.getTrainingPlace())
                .lecturer(lecturerDao.findById(trainingPlanRequest.getLecturerId()).isPresent()?lecturerDao.findById(trainingPlanRequest.getLecturerId()).get():null)
                .executor(executorDao.findById(trainingPlanRequest.getExecutorId()).isPresent()?executorDao.findById(trainingPlanRequest.getExecutorId()).get():null)
                .submit(false)
                .build();
        TrainingContent trainingContent = TrainingContent
                .builder()
                .skillStack(trainingPlanRequest.getSkillStack())
                .trainingGoal(trainingPlanRequest.getTrainingGoal())
                .comment(trainingPlanRequest.getTrainingContentComment())
                .trainingPlan(trainingPlan)
                .build();
        TrainingFee trainingFee = TrainingFee
                .builder()
                .unitFee(trainingPlanRequest.getUnitFee())
                .totalFee(trainingPlanRequest.getTotalFee())
                .comment(trainingPlanRequest.getTrainingFeeComment())
                .trainingPlan(trainingPlan)
                .build();
        trainingPlanDao.save(trainingPlan);
        trainingContentDao.save(trainingContent);
        trainingPlan.setTrainingContent(trainingContent);
        trainingPlan.setTrainingFee(trainingFee);
        trainingFeeDao.save(trainingFee);
        return new Result<>(ResultEnum.SUCCESS.getCode(), "创建培训计划成功","");
    }

    public Result<List<TrainingPlan>> getTrainingPlanList() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Integer executorId = user.getExecutor().getId();
        List<TrainingPlan> trainingPlanList = new ArrayList<>();
        if(!executorDao.findById(executorId).isPresent()){
            return new Result<>(ResultEnum.NOT_FOUND.getCode(), "执行人不存在",null);
        }
        else {
            Executor executor = executorDao.findById(executorId).get();
            trainingPlanList = trainingPlanDao.getAllByExecutor(executor);
        }
        return new Result<>(ResultEnum.SUCCESS.getCode(), "获取培训计划列表成功",trainingPlanList);
    }
    public Result<List<TrainingPlanResponse>> getAll(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        String perms = user.getPerms();
        List<TrainingPlan> trainingPlanList;
        if(perms.equals("executor")){
            Executor executor = user.getExecutor();
            trainingPlanList = trainingPlanDao.getAllByExecutor(executor);
        }
        else {
            trainingPlanList = trainingPlanDao.findAll();
        }
        List<TrainingPlanResponse> trainingPlanResponseList = new ArrayList<>();
        for(TrainingPlan trainingPlan:trainingPlanList){
            TrainingPlanResponse trainingPlanResponse = TrainingPlanResponse
                    .builder()
                    .id(trainingPlan.getId())
                    .courseName(trainingPlan.getCourseName())
                    .skillStack(trainingPlan.getTrainingContent().getSkillStack())
                    .trainingGoal(trainingPlan.getTrainingContent().getTrainingGoal())
                    .trainingContentComment(trainingPlan.getTrainingContent().getComment())
                    .unitFee(trainingPlan.getTrainingFee().getUnitFee())
                    .totalFee(trainingPlan.getTrainingFee().getTotalFee())
                    .trainingFeeComment(trainingPlan.getTrainingFee().getComment())
                    .trainingNum(trainingPlan.getTrainingNum())
                    .startTime(trainingPlan.getStartTime())
                    .endTime(trainingPlan.getEndTime())
                    .trainingPlace(trainingPlan.getTrainingPlace())
                    .executorName(trainingPlan.getExecutor().getName())
                    .build();
            if (trainingPlan.getLecturer() != null){
                trainingPlanResponse.setLecturerName(trainingPlan.getLecturer().getName());
            }else {
                trainingPlanResponse.setLecturerName(null);
            }
            trainingPlanResponseList.add(trainingPlanResponse);
        }
        return new Result<>(ResultEnum.SUCCESS.getCode(), "获取全部培训计划成功",trainingPlanResponseList);
    }

    public Result<String> updateTrainingPlan(TrainingPlanRequest trainingPlanRequest) {
        TrainingPlan trainingPlan = trainingPlanDao.getById(trainingPlanRequest.getId());
        TrainingContent trainingContent = trainingPlan.getTrainingContent();
        TrainingFee trainingFee = trainingPlan.getTrainingFee();
        trainingPlan.setCourseName(trainingPlanRequest.getCourseName());
        trainingPlan.setTrainingNum(trainingPlanRequest.getTrainingNum());
        trainingPlan.setStartTime(trainingPlanRequest.getStartTime());
        trainingPlan.setEndTime(trainingPlanRequest.getEndTime());
        trainingPlan.setTrainingPlace(trainingPlanRequest.getTrainingPlace());
        trainingPlan.setLecturer(lecturerDao.findById(trainingPlanRequest.getId()).isPresent()?lecturerDao.findById(trainingPlanRequest.getId()).get():null);

        trainingContent.setSkillStack(trainingPlanRequest.getSkillStack());
        trainingContent.setTrainingGoal(trainingPlanRequest.getTrainingGoal());
        trainingContent.setComment(trainingPlanRequest.getTrainingContentComment());

        trainingFee.setUnitFee(trainingPlanRequest.getUnitFee());
        trainingFee.setTotalFee(trainingPlanRequest.getTotalFee());
        trainingFee.setComment(trainingPlanRequest.getTrainingFeeComment());

        trainingPlanDao.save(trainingPlan);
        trainingContentDao.save(trainingContent);
        trainingFeeDao.save(trainingFee);
        return new Result<>(ResultEnum.SUCCESS.getCode(),"修改成功","");
    }

    public Result<String> deleteTrainingPlan(TrainingPlanRequest trainingPlanRequest) {
        Integer trainingPlanId = trainingPlanRequest.getId();
        if(!trainingPlanDao.findById(trainingPlanId).isPresent()){
            return new Result<>(ResultEnum.NOT_FOUND.getCode(), "培训计划不存在","");
        }
        else {
            TrainingPlan trainingPlan = trainingPlanDao.getById(trainingPlanId);
            signInTableDao.deleteAll(trainingPlan.getSignInTableList());
            if(trainingPlanDao.findById(trainingPlanId).isPresent()){
                trainingPlanDao.deleteById(trainingPlanId);
            }
            return new Result<>(ResultEnum.SUCCESS.getCode(), "删除成功","");
        }
    }

    public Result<String> publish(TrainingPlanRequest trainingPlanRequest){
        TrainingPlan trainingPlan = trainingPlanDao.getById(trainingPlanRequest.getId());
        trainingPlan.setSubmit(true);
        List<Student> students = studentDao.findAll();
        for (Student student : students) {
            String post = student.getPost();
            Email email = new Email();
            email.setTheme("我们的新课程发布了！");
            email.setMainBody("我们的新课程："+trainingPlan.getCourseName()+"已经发布了,主要内容是"+trainingPlan.getTrainingContent().getTrainingGoal()+",主讲人是："+trainingPlan.getLecturer().getName()+",希望你能参与到课程中，与我们一起进步！");
            email.setRecipientAddress(post);
            //此处本应调用邮件系统接口发送邮件，由于没有此系统所以不写
            emailDao.save(email);
        }
        return ResultUtil.success();
    }

    public Result<List<TrainingPlan>> getVisible(){
        List<TrainingPlan> trainingPlans = trainingPlanDao.findAll();
        List<TrainingPlan> trainingPlans2 = new ArrayList<>();
        for (TrainingPlan trainingPlan : trainingPlans) {
            if (trainingPlan.isSubmit()){
                trainingPlans2.add(trainingPlan);
            }
        }
        return ResultUtil.success(trainingPlans2);
    }
}
