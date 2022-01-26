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
 * @description: 题库-题类型
 * @author: gwb
 * @create: 2021-09-29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("topic_type")
public class TopicTypeDTO {

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /** 科目字典id */
    private Integer subjectsDictId;
    /** 题类型 */
    private String type;
    /** 创建时间 */
    @Builder.Default
    private Date gmtCreate = new Date();
    /** 更新时间 */
    @Builder.Default
    private Date gmtModified = new Date();

}