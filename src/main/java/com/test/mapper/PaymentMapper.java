package com.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

    Payment getPaymentById(Integer id);

    List<Payment> getPayments(Integer option);

    List<Payment> getPaymentsByList(@Param("t") List<Integer> option, @Param("id") Integer id);
}
