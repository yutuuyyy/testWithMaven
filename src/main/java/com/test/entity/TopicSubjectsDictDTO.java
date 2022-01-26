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
 * @description: 科目字典
 * @author: gwb
 * @create: 2021-09-29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("topic_subjects_dict")
public class TopicSubjectsDictDTO {

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /** 科目 */
    private String subjects;
    /** 创建时间 */
    private Date gmtCreate;
    /** 更新时间 */
    private Date gmtModified;

}