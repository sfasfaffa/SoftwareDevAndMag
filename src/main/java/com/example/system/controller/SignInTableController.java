package com.example.system.controller;

import com.example.system.model.Result;
import com.example.system.request.RegisterTableRequest;
import com.example.system.request.SignInTableRequest;
import com.example.system.service.SignInTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signInTable")
public class SignInTableController {
    @Autowired
    SignInTableService signInTableService;
    @PostMapping("/get/all")
    public Result getAll(){
        return signInTableService.getSignInTable();
    }
    @PostMapping("/modify/regis")
    public Result modifyRe(@RequestBody SignInTableRequest signInTableRequest){
        return signInTableService.modifyReg(signInTableRequest);
    }
    @PostMapping("/modify/pay")
    public Result modifyPay(@RequestBody SignInTableRequest signInTableRequest){
        return signInTableService.modifyPay(signInTableRequest);
    }
    @PostMapping(value = "/fee/payment")
    public Result feePayment(@RequestBody SignInTableRequest signInTableRequest){
        return signInTableService.modifyFee(signInTableRequest);
    }
}
