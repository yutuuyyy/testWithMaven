package com.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.entity.Payment;
import com.test.entity.PaymentEx;
import com.test.mapper.PaymentMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: testWithMaven
 * @description:
 * @author: gwb
 * @create: 2021-12-30 11:05
 **/
@Controller
public class TestController {

    @Resource
    private PaymentMapper paymentMapper;

    @GetMapping(value = "/test/{id}")
    @ResponseBody
    public Payment test(@PathVariable("id") Integer id){
        Payment payment = paymentMapper.getPaymentById(id);
        try {
            Field serial = payment.getClass().getDeclaredField("serial");
            if (serial != null){
                serial.setAccessible(true);
                JsonIgnore annotation = serial.getAnnotation(JsonIgnore.class);
                if (annotation != null){
                    InvocationHandler ih = Proxy.getInvocationHandler(annotation);
                    Field memberValues = ih.getClass().getDeclaredField("memberValues");
                    memberValues.setAccessible(true);
                    Map memberValuesMap = (Map) memberValues.get(ih);
                    memberValuesMap.put("value", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payment;
    }

    @GetMapping(value = "/jsonTest/{option}")
    @ResponseBody
    public List<Payment> getPayments(@PathVariable("option") Integer option){
        List<Integer> optionList = new ArrayList<>();
        optionList.add(2);
        optionList.add(8);
        Integer id = null;
        //List<Payment> payments = paymentMapper.getPayments(option);
        List<Payment> payments = paymentMapper.getPaymentsByList(optionList, id);
        return payments;
    }

    @GetMapping(value = "/jsonTest/{serial}")
    @ResponseBody
    public Boolean isExist(@PathVariable("serial") String serial){
        QueryWrapper<Payment> paymentWrapper = Wrappers.query();
        paymentWrapper.select("id").eq("serial", serial).last("limit 1");
        paymentMapper.selectOne(paymentWrapper);

        return null;
    }


    @GetMapping(value = "/otherTest")
    @ResponseBody
    public Object otherTest() throws Exception{
        PaymentEx paymentEx = new PaymentEx();
        Object invoke;
        try {
            Method method = paymentEx.getClass().getSuperclass().getDeclaredMethod("getPrivateMethod");
            method.setAccessible(true);
            invoke = method.invoke(paymentEx);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return invoke;
    }


    //@Transactional(rollbackFor = Exception.class)
    //public int addFreshStudentsNew2(List<FreshStudentAndStudentModel> list, String schoolNo) {
    //    if (list == null || list.isEmpty()) {
    //        return 0;
    //    }
    //    List<StudentEntity> studentEntityList = new LinkedList<>();
    //    List<EnrollStudentEntity> enrollStudentEntityList = new LinkedList<>();
    //    List<AllusersEntity> allusersEntityList = new LinkedList<>();
    //
    //
    //    for (FreshStudentAndStudentModel freshStudentAndStudentModel : list) {
    //
    //
    //        EnrollStudentEntity enrollStudentEntity = new EnrollStudentEntity();
    //        StudentEntity studentEntity = new StudentEntity();
    //        BeanUtils.copyProperties(freshStudentAndStudentModel, studentEntity);
    //        BeanUtils.copyProperties(freshStudentAndStudentModel, enrollStudentEntity);
    //        String operator = TenancyContext.UserID.get();
    //        String studentId = BaseUuidUtils.base58Uuid();
    //        enrollStudentEntity.setId(BaseUuidUtils.base58Uuid());
    //        enrollStudentEntity.setStudentId(studentId);
    //        enrollStudentEntity.setIdentityCardId(freshStudentAndStudentModel.getIdCard());
    //        enrollStudentEntity.setOperator(operator);
    //        studentEntity.setId(studentId);
    //        studentEntity.setIdentityCardId(freshStudentAndStudentModel.getIdCard());
    //        studentEntity.setOperator(operator);
    //        studentEntityList.add(studentEntity);
    //        enrollStudentEntityList.add(enrollStudentEntity);
    //
    //
    //        AllusersEntity allusersEntity = new AllusersEntity();
    //        allusersEntity.setId(enrollStudentEntity.getId());
    //        allusersEntity.setUserCode(enrollStudentEntity.getNemtCode());
    //        allusersEntity.setUserName(enrollStudentEntity.getName());
    //        allusersEntity.setSchoolNo(schoolNo);
    //        allusersEntity.setTelNum(enrollStudentEntity.getTelNum());
    //        allusersEntity.setPassword(enrollStudentEntity.getNemtCode());  //密码设置为考生号
    //        allusersEntityList.add(allusersEntity);
    //    }
    //    enResult = enrollStudentDao.insertAll(enrollStudentEntityList);
    //    stuResult = studentDao.insertAll(studentEntityList);
    //    allResult = allusersFacade.insertUserList(allusersEntityList);
    //
    //
    //    if (enResult > 0 && stuResult > 0 && allResult) {
    //        return 10;
    //    }
    //    return -10;
    //}


    //
    //@Transactional(rollbackFor = Exception.class)
    //public int addFreshStudentsNew2(List<FreshStudentAndStudentModel> list, String schoolNo) {
    //    if (list == null || list.isEmpty()) {
    //        return 0;
    //    }
    //    List<StudentEntity> studentEntityList = new LinkedList<>();
    //    List<EnrollStudentEntity> enrollStudentEntityList = new LinkedList<>();
    //    List<AllusersEntity> allusersEntityList = new LinkedList<>();
    //
    //
    //    for (FreshStudentAndStudentModel freshStudentAndStudentModel : list) {
    //
    //
    //        EnrollStudentEntity enrollStudentEntity = new EnrollStudentEntity();
    //        StudentEntity studentEntity = new StudentEntity();
    //        BeanUtils.copyProperties(freshStudentAndStudentModel, studentEntity);
    //        BeanUtils.copyProperties(freshStudentAndStudentModel, enrollStudentEntity);
    //        String operator = TenancyContext.UserID.get();
    //        String studentId = BaseUuidUtils.base58Uuid();
    //        enrollStudentEntity.setId(BaseUuidUtils.base58Uuid());
    //        enrollStudentEntity.setStudentId(studentId);
    //        enrollStudentEntity.setIdentityCardId(freshStudentAndStudentModel.getIdCard());
    //        enrollStudentEntity.setOperator(operator);
    //        studentEntity.setId(studentId);
    //        studentEntity.setIdentityCardId(freshStudentAndStudentModel.getIdCard());
    //        studentEntity.setOperator(operator);
    //        studentEntityList.add(studentEntity);
    //        enrollStudentEntityList.add(enrollStudentEntity);
    //
    //
    //        AllusersEntity allusersEntity = new AllusersEntity();
    //        allusersEntity.setId(enrollStudentEntity.getId());
    //        allusersEntity.setUserCode(enrollStudentEntity.getNemtCode());
    //        allusersEntity.setUserName(enrollStudentEntity.getName());
    //        allusersEntity.setSchoolNo(schoolNo);
    //        allusersEntity.setTelNum(enrollStudentEntity.getTelNum());
    //        allusersEntity.setPassword(enrollStudentEntity.getNemtCode());  //密码设置为考生号
    //        allusersEntityList.add(allusersEntity);
    //    }
    //
    //
    //    int nThreads = 50;
    //
    //
    //    int size = enrollStudentEntityList.size();
    //    ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
    //    List<Future<Integer>> futures = new ArrayList<Future<Integer>>(nThreads);
    //
    //
    //    for (int i = 0; i < nThreads; i++) {
    //        final List<EnrollStudentEntity> EnrollStudentEntityImputList = enrollStudentEntityList.subList(size / nThreads * i, size / nThreads * (i + 1));
    //        final List<StudentEntity> studentEntityImportList = studentEntityList.subList(size / nThreads * i, size / nThreads * (i + 1));
    //        final List<AllusersEntity> allusersEntityImportList = allusersEntityList.subList(size / nThreads * i, size / nThreads * (i + 1));
    //
    //
    //        Callable<Integer> task1 = () -> {
    //            studentSave.saveStudent(EnrollStudentEntityImputList,studentEntityImportList,allusersEntityImportList);
    //            return 1;
    //        };
    //        futures.add(executorService.submit(task1));
    //    }
    //    executorService.shutdown();
    //    if (!futures.isEmpty() && futures != null) {
    //        return 10;
    //    }
    //    return -10;
    //}
}
