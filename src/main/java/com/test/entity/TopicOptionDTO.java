package com.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: winshonsubject
 * @description: 题库-题-选项
 * @author: gwb
 * @create: 2021-09-29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("topic_option")
public class TopicOptionDTO {

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /** 题id（topic_question） */
    private Integer questionId;
    /** 选项 */
    private String options;
    /** 排序 */
    private Integer sorts;
    /** 创建时间 */
    @Builder.Default
    private Date gmtCreate = new Date();
    /** 更新时间 */
    @Builder.Default
    private Date gmtModified = new Date();

}