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
 * @description: 题库-目录（自关联多级）
 * @author: gwb
 * @create: 2021-09-29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("topic_catalogue")
public class TopicCatalogueDTO {

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /** 题库科目id（topic_subjects） */
    private Integer subjectsId;
    /** 自关联id */
    private Integer catalogueId;
    /** 目录 */
    private String catalogue;
    /** 目录级别，几级目录（父级自增加一，默认为1） */
    private Integer catalogueLevel;
    /** 排序 */
    private Integer sorts;
    /** 创建时间 */
    @Builder.Default
    private Date gmtCreate = new Date();
    /** 更新时间 */
    @Builder.Default
    private Date gmtModified = new Date();

}