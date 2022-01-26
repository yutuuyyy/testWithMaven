package com.test.common;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.test.common.util.SpringUtil;
import com.test.entity.TopicCatalogueDTO;
import com.test.entity.TopicOptionDTO;
import com.test.entity.TopicQuestionDTO;
import com.test.entity.TopicTypeDTO;
import com.test.mapper.TopicCatalogueMapper;
import com.test.mapper.TopicOptionMapper;
import com.test.mapper.TopicQuestionMapper;
import com.test.mapper.TopicTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @program: testWithMaven
 * @description:
 * @author: gwb
 * @create: 2022-01-25 17:01
 **/
@Slf4j
public class ExcelThread implements Callable<JSONObject> {

    private JSONObject errorMsg;
    private int totalCells;
    private Row row;
    private int cols;
    private Integer subjectsId;
    private String fileName;
    private int lineNum;
    private Integer subjectDictId;
    private TopicCatalogueMapper topicCatalogueMapper;
    private TopicTypeMapper topicTypeMapper;
    private TopicQuestionMapper topicQuestionMapper;
    private TopicOptionMapper topicOptionMapper;

    public ExcelThread(JSONObject errorMsg, int totalCells, Row row, int cols, Integer subjectsId, String fileName, int lineNum, Integer subjectDictId) {
        this.errorMsg = errorMsg;
        this.totalCells = totalCells;
        this.row = row;
        this.cols = cols;
        this.subjectsId = subjectsId;
        this.fileName = fileName;
        this.lineNum = lineNum;
        this.subjectDictId = subjectDictId;
        topicCatalogueMapper = SpringUtil.getBean(TopicCatalogueMapper.class);
        topicTypeMapper = SpringUtil.getBean(TopicTypeMapper.class);
        topicQuestionMapper = SpringUtil.getBean(TopicQuestionMapper.class);
        topicOptionMapper = SpringUtil.getBean(TopicOptionMapper.class);
    }

    @Override
    public JSONObject call() throws Exception {
        // 循环Excel的列
        String catalogue1 = ""; //一级目录
        String catalogue2 = ""; //二级目录
        String catalogue3 = ""; //三级目录
        String topicType = ""; //题类型
        String difficulty = ""; //难度:难度code（1001：简单；1002：一般；1003：困难）
        String question = ""; //题目
        String optionA = ""; //选项A
        String optionB = ""; //选项B
        String optionC = ""; //选项C
        String optionD = ""; //选项D
        String answerMark = ""; //答案
        String answerAnalysis = ""; //解析
        Integer page = null; //页码

        Integer catalogue1Id = null;
        Integer catalogue2Id = null;
        Integer catalogue3Id = null;
        Integer topicTypeCode = null;
        String difficultyCode = null;
        List<String> optionList = new ArrayList<>();

        for (int c = 0; c < totalCells; c++) {
            Cell cell = row.getCell(c);
            cols = c + 1;
            if (null == cell) {
                cell = row.createCell(c);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String cellVal = cell.getStringCellValue();
            if (c == 1) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    if (cellVal.trim().length() <= 255) {
                        catalogue1 = cellVal.trim();
                        QueryWrapper<TopicCatalogueDTO> checkWrapper = Wrappers.query();
                        checkWrapper.eq("subjects_id", subjectsId).eq("catalogue",catalogue1).eq("catalogue_level", 1).last("limit 1");
                        Integer count = topicCatalogueMapper.selectCount(checkWrapper);
                        TopicCatalogueDTO topicCatalogueDTO;
                        if (count == 0) {
                            QueryWrapper<TopicCatalogueDTO> catalogueSortWrapper = Wrappers.query();
                            catalogueSortWrapper.select("max(sorts) as sorts").eq("subjects_id", subjectsId)
                                    .eq("catalogue_level", 1);
                            Integer catalogueSort;
                            TopicCatalogueDTO catalogueDTO = topicCatalogueMapper.selectOne(catalogueSortWrapper);
                            if (catalogueDTO == null) {
                                catalogueSort = 1;
                            } else {
                                Integer catalogueSorts = catalogueDTO.getSorts();
                                catalogueSort = catalogueSorts++;
                            }
                            topicCatalogueDTO = TopicCatalogueDTO.builder().subjectsId(subjectsId).catalogueId(0)
                                    .catalogue(catalogue1).catalogueLevel(1)
                                    .sorts(catalogueSort).build();
                            topicCatalogueMapper.insert(topicCatalogueDTO);
                        }else {
                            topicCatalogueDTO = topicCatalogueMapper.selectOne(checkWrapper);
                        }
                        catalogue1Id = topicCatalogueDTO.getId();
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列知识点1最大为255位字符，请仔细检查！");
                        return errorMsg;
                    }
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列知识点1不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 2) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    if (cellVal.trim().length() <= 255) {
                        catalogue2 = cellVal.trim();
                        QueryWrapper<TopicCatalogueDTO> checkWrapper = Wrappers.query();
                        checkWrapper.eq("subjects_id", subjectsId).eq("catalogue",catalogue2).eq("catalogue_level", 2).last("limit 1");
                        Integer count = topicCatalogueMapper.selectCount(checkWrapper);
                        TopicCatalogueDTO topicCatalogueDTO;
                        if (count == 0) {
                            QueryWrapper<TopicCatalogueDTO> catalogueSortWrapper = Wrappers.query();
                            catalogueSortWrapper.select("max(sorts) as sorts").eq("subjects_id", subjectsId)
                                    .eq("catalogue_id", catalogue1Id);
                            Integer catalogueSort;
                            TopicCatalogueDTO catalogueDTO = topicCatalogueMapper.selectOne(catalogueSortWrapper);
                            if (catalogueDTO == null) {
                                catalogueSort = 1;
                            } else {
                                Integer catalogueSorts = catalogueDTO.getSorts();
                                catalogueSort = catalogueSorts++;
                            }
                            topicCatalogueDTO = TopicCatalogueDTO.builder().subjectsId(subjectsId).catalogueId(catalogue1Id)
                                    .catalogue(catalogue2).catalogueLevel(2)
                                    .sorts(catalogueSort).build();
                            topicCatalogueMapper.insert(topicCatalogueDTO);
                        }else {
                            topicCatalogueDTO = topicCatalogueMapper.selectOne(checkWrapper);
                        }
                        catalogue2Id = topicCatalogueDTO.getId();
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列知识点2最大为255位字符，请仔细检查！");
                        return errorMsg;
                    }
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列知识点2不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 3) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    if (cellVal.trim().length() <= 255) {
                        catalogue3 = cellVal.trim();
                        QueryWrapper<TopicCatalogueDTO> checkWrapper = Wrappers.query();
                        checkWrapper.eq("subjects_id", subjectsId).eq("catalogue",catalogue3).eq("catalogue_level", 3).last("limit 1");
                        Integer count = topicCatalogueMapper.selectCount(checkWrapper);
                        TopicCatalogueDTO topicCatalogueDTO;
                        if (count == 0) {
                            QueryWrapper<TopicCatalogueDTO> catalogueSortWrapper = Wrappers.query();
                            catalogueSortWrapper.select("max(sorts) as sorts").eq("subjects_id", subjectsId)
                                    .eq("catalogue_id", catalogue2Id);
                            Integer catalogueSort;
                            TopicCatalogueDTO catalogueDTO = topicCatalogueMapper.selectOne(catalogueSortWrapper);
                            if (catalogueDTO == null) {
                                catalogueSort = 1;
                            } else {
                                Integer catalogueSorts = catalogueDTO.getSorts();
                                catalogueSort = catalogueSorts++;
                            }
                            topicCatalogueDTO = TopicCatalogueDTO.builder().subjectsId(subjectsId).catalogueId(catalogue2Id)
                                    .catalogue(catalogue3).catalogueLevel(3)
                                    .sorts(catalogueSort).build();
                            topicCatalogueMapper.insert(topicCatalogueDTO);
                        }else {
                            topicCatalogueDTO = topicCatalogueMapper.selectOne(checkWrapper);
                        }
                        catalogue3Id = topicCatalogueDTO.getId();
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列知识点3最大为255位字符，请仔细检查！");
                        return errorMsg;
                    }
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列知识点3不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 4) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    if (cellVal.trim().length() <= 255) {
                        topicType = cellVal.trim();
                        QueryWrapper<TopicTypeDTO> typeWrapper = Wrappers.query();
                        typeWrapper.select("id").eq("subjects_dict_id", subjectDictId).eq("type", topicType).last("limit 1");
                        TopicTypeDTO topicTypeDTO = topicTypeMapper.selectOne(typeWrapper);
                        if (topicTypeDTO == null){
                            topicTypeDTO = TopicTypeDTO.builder().subjectsDictId(subjectDictId).type(topicType).build();
                            topicTypeMapper.insert(topicTypeDTO);
                        }
                        topicTypeCode = topicTypeDTO.getId();
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列题型最大为255位字符，请仔细检查！");
                        return errorMsg;
                    }
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列题型不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 5) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    if (cellVal.trim().length() <= 2) {
                        difficulty = cellVal.trim();
                        switch (difficulty) {
                            case "简单":
                                difficultyCode = "1001";
                                break;
                            case "一般":
                                difficultyCode = "1002";
                                break;
                            case "困难":
                                difficultyCode = "1003";
                                break;
                            default:
                                errorMsg.put("retCode", "fail");
                                errorMsg.put("retMsg",
                                        fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列难度格式不正确，请仔细检查！");
                                return errorMsg;
                        }
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列难度最大为2位字符，请仔细检查！");
                        return errorMsg;
                    }
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列难度不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 6) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    question = cellVal.trim();
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列题目不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 7) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    optionA = cellVal.trim();
                    optionList.add(optionA);
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列选项A不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 8) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    optionB = cellVal.trim();
                    optionList.add(optionB);
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列选项B不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 9) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    optionC = cellVal.trim();
                    optionList.add(optionC);
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列选项C不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 10) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    optionD = cellVal.trim();
                    optionList.add(optionD);
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列选项D不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 11) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    answerMark = cellVal.trim();
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列答案不能为空，请仔细检查！");
                    return errorMsg;
                }
            } else if (c == 12) {
                answerAnalysis = cellVal.trim();
            } else if (c == 13) {
                if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                    String pageNumber = cellVal.trim();
                    try {
                        page = Integer.parseInt(pageNumber);
                    } catch (NumberFormatException e) {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列页码格式不正确，请仔细检查！");
                        return errorMsg;
                    }
                } else {
                    errorMsg.put("retCode", "fail");
                    errorMsg.put("retMsg",
                            fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列页码不能为空，请仔细检查！");
                    return errorMsg;
                }
            }
        }
        TopicQuestionDTO topicQuestionDTO = new TopicQuestionDTO(null, catalogue3Id, question, answerMark, answerAnalysis, topicType, topicTypeCode,
                difficulty, difficultyCode, new Date(), new Date(), page);

        topicQuestionMapper.insert(topicQuestionDTO);

        Integer questionId = topicQuestionDTO.getId();
        for (int i = 0; i < optionList.size(); i++) {
            String options = optionList.get(i);
            TopicOptionDTO topicOptionDTO = TopicOptionDTO.builder().options(options).questionId(questionId).sorts(i + 1).build();
            topicOptionMapper.insert(topicOptionDTO);
        }
        return errorMsg;
    }
}
