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
 * @description: 题库-科目
 * @author: gwb
 * @create: 2021-09-29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("topic_subjects")
public class TopicSubjectsDTO {

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /** 题库年级id（topic_grade） */
    private Integer gradeId;
    /** 科目字典id */
    private Integer subjectsDictId;
    /** 科目 */
    private String subjects;
    /** 封面图片 */
    private String cover;
    /** 排序 */
    private Integer sorts;
    /** 创建时间 */
    @Builder.Default
    private Date gmtCreate = new Date();
    /** 更新时间 */
    @Builder.Default
    private Date gmtModified = new Date();

}