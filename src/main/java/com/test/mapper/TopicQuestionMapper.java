package com.test.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.entity.TopicQuestionDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 功能描述: 题库-题
 *
 * @Author: gwb
 * @Date: 2021-09-29
 * @update: [序号] [时间] [修改人] [自定义变更内容]
 */
public interface TopicQuestionMapper extends BaseMapper<TopicQuestionDTO> {

    void insertTopicQuestion(@Param("t") TopicQuestionDTO topicQuestionDTO);
}