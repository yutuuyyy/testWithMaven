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
public class ExcelThread2 implements Callable<JSONObject> {

    private JSONObject errorMsg;
    private int totalCells;
    private List<Row> rowList;
    private int cols;
    private Integer subjectsId;
    private String fileName;
    private int lineNum;
    private Integer subjectDictId;
    private TopicCatalogueMapper topicCatalogueMapper;
    private TopicTypeMapper topicTypeMapper;
    private TopicQuestionMapper topicQuestionMapper;
    private TopicOptionMapper topicOptionMapper;

    public ExcelThread2(JSONObject errorMsg, int totalCells, List<Row> rowList, int cols, Integer subjectsId, String fileName, int lineNum, Integer subjectDictId) {
        this.errorMsg = errorMsg;
        this.totalCells = totalCells;
        this.rowList = rowList;
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
        log.warn("threadId===============" + Thread.currentThread().getName());

        for (int i = 0; i < rowList.size(); i++) {
            Row row = rowList.get(i);
            // ??????Excel??????
            String catalogue1 = ""; //????????????
            String catalogue2 = ""; //????????????
            String catalogue3 = ""; //????????????
            String topicType = ""; //?????????
            String difficulty = ""; //??????:??????code???1001????????????1002????????????1003????????????
            String question = ""; //??????
            String optionA = ""; //??????A
            String optionB = ""; //??????B
            String optionC = ""; //??????C
            String optionD = ""; //??????D
            String answerMark = ""; //??????
            String answerAnalysis = ""; //??????
            Integer page = null; //??????

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
                            checkWrapper.eq("subjects_id", subjectsId).eq("catalogue", catalogue1).eq("catalogue_level", 1).last("limit 1");
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
                            } else {
                                topicCatalogueDTO = topicCatalogueMapper.selectOne(checkWrapper);
                            }
                            catalogue1Id = topicCatalogueDTO.getId();
                        } else {
                            errorMsg.put("retCode", "fail");
                            errorMsg.put("retMsg",
                                    fileName + "??????" + (lineNum + 1) + "?????????" + cols + "????????????1?????????255??????????????????????????????");
                            return errorMsg;
                        }
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "????????????1?????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 2) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        if (cellVal.trim().length() <= 255) {
                            catalogue2 = cellVal.trim();
                            QueryWrapper<TopicCatalogueDTO> checkWrapper = Wrappers.query();
                            checkWrapper.eq("subjects_id", subjectsId).eq("catalogue", catalogue2).eq("catalogue_level", 2).last("limit 1");
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
                            } else {
                                topicCatalogueDTO = topicCatalogueMapper.selectOne(checkWrapper);
                            }
                            catalogue2Id = topicCatalogueDTO.getId();
                        } else {
                            errorMsg.put("retCode", "fail");
                            errorMsg.put("retMsg",
                                    fileName + "??????" + (lineNum + 1) + "?????????" + cols + "????????????2?????????255??????????????????????????????");
                            return errorMsg;
                        }
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "????????????2?????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 3) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        if (cellVal.trim().length() <= 255) {
                            catalogue3 = cellVal.trim();
                            QueryWrapper<TopicCatalogueDTO> checkWrapper = Wrappers.query();
                            checkWrapper.eq("subjects_id", subjectsId).eq("catalogue", catalogue3).eq("catalogue_level", 3).last("limit 1");
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
                            } else {
                                topicCatalogueDTO = topicCatalogueMapper.selectOne(checkWrapper);
                            }
                            catalogue3Id = topicCatalogueDTO.getId();
                        } else {
                            errorMsg.put("retCode", "fail");
                            errorMsg.put("retMsg",
                                    fileName + "??????" + (lineNum + 1) + "?????????" + cols + "????????????3?????????255??????????????????????????????");
                            return errorMsg;
                        }
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "????????????3?????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 4) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        if (cellVal.trim().length() <= 255) {
                            topicType = cellVal.trim();
                            QueryWrapper<TopicTypeDTO> typeWrapper = Wrappers.query();
                            typeWrapper.select("id").eq("subjects_dict_id", subjectDictId).eq("type", topicType).last("limit 1");
                            TopicTypeDTO topicTypeDTO = topicTypeMapper.selectOne(typeWrapper);
                            if (topicTypeDTO == null) {
                                topicTypeDTO = TopicTypeDTO.builder().subjectsDictId(subjectDictId).type(topicType).build();
                                topicTypeMapper.insert(topicTypeDTO);
                            }
                            topicTypeCode = topicTypeDTO.getId();
                        } else {
                            errorMsg.put("retCode", "fail");
                            errorMsg.put("retMsg",
                                    fileName + "??????" + (lineNum + 1) + "?????????" + cols + "??????????????????255??????????????????????????????");
                            return errorMsg;
                        }
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "??????????????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 5) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        if (cellVal.trim().length() <= 2) {
                            difficulty = cellVal.trim();
                            switch (difficulty) {
                                case "??????":
                                    difficultyCode = "1001";
                                    break;
                                case "??????":
                                    difficultyCode = "1002";
                                    break;
                                case "??????":
                                    difficultyCode = "1003";
                                    break;
                                default:
                                    errorMsg.put("retCode", "fail");
                                    errorMsg.put("retMsg",
                                            fileName + "??????" + (lineNum + 1) + "?????????" + cols + "?????????????????????????????????????????????");
                                    return errorMsg;
                            }
                        } else {
                            errorMsg.put("retCode", "fail");
                            errorMsg.put("retMsg",
                                    fileName + "??????" + (lineNum + 1) + "?????????" + cols + "??????????????????2??????????????????????????????");
                            return errorMsg;
                        }
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "??????????????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 6) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        question = cellVal.trim();
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "??????????????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 7) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        optionA = cellVal.trim();
                        optionList.add(optionA);
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "?????????A?????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 8) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        optionB = cellVal.trim();
                        optionList.add(optionB);
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "?????????B?????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 9) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        optionC = cellVal.trim();
                        optionList.add(optionC);
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "?????????C?????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 10) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        optionD = cellVal.trim();
                        optionList.add(optionD);
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "?????????D?????????????????????????????????");
                        return errorMsg;
                    }
                } else if (c == 11) {
                    if (!cellVal.equals("null") && !cellVal.equals("NULL") && cellVal.trim().length() > 0) {
                        answerMark = cellVal.trim();
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "??????????????????????????????????????????");
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
                                    fileName + "??????" + (lineNum + 1) + "?????????" + cols + "?????????????????????????????????????????????");
                            return errorMsg;
                        }
                    } else {
                        errorMsg.put("retCode", "fail");
                        errorMsg.put("retMsg",
                                fileName + "??????" + (lineNum + 1) + "?????????" + cols + "??????????????????????????????????????????");
                        return errorMsg;
                    }
                }
            }
            TopicQuestionDTO topicQuestionDTO = new TopicQuestionDTO(null, catalogue3Id, question, answerMark, answerAnalysis, topicType, topicTypeCode,
                    difficulty, difficultyCode, new Date(), new Date(), page);

            topicQuestionMapper.insert(topicQuestionDTO);

            Integer questionId = topicQuestionDTO.getId();
            for (int j = 0; j < optionList.size(); j++) {
                String options = optionList.get(j);
                TopicOptionDTO topicOptionDTO = TopicOptionDTO.builder().options(options).questionId(questionId).sorts(j + 1).build();
                topicOptionMapper.insert(topicOptionDTO);
            }
        }
        return errorMsg;
    }
}
