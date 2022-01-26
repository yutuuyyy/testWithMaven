package com.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: winshonsubject
 * @description: 题库-题
 * @author: gwb
 * @create: 2021-09-29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("topic_question")
public class TopicQuestionDTO {

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /** 题库-目录id（topic_catalogue） */
    private Integer catalogueId;
    /** 题干 */
    private String topic;
    /** 答案 */
    private String answerMark;
    /** 解析 */
    private String answerAnalysis;
    /** 题类型 */
    private String topicType;
    /** 题类型code */
    private Integer topicTypeCode;
    /** 难度（简单；一般；困难） */
    private String difficulty;
    /** 难度code（1001：简单；1002：一般；1003：困难） */
    private String difficultyCode;
    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;
    /** 页码 */
    private Integer pages;

}