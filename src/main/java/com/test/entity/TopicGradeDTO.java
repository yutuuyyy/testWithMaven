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
 * @description: 题库-年级
 * @author: gwb
 * @create: 2021-09-29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("topic_grade")
public class TopicGradeDTO {

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /** 年级 */
    private String grade;
    /** 排序 */
    private Integer sorts;
    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;

}