package com.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.test.common.ExcelThread;
import com.test.common.util.ExcelImportUtils;
import com.test.entity.TopicSubjectsDTO;
import com.test.entity.TopicSubjectsDictDTO;
import com.test.mapper.TopicSubjectsDictMapper;
import com.test.mapper.TopicSubjectsMapper;
import com.test.service.common.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: testWithMaven
 * @description: 多线程
 * @author: gwb
 * @create: 2022-01-12 16:53
 **/
@Controller
@Slf4j
public class AsycController {

    @Resource
    private RedisService redisService;
    @Resource
    private TopicSubjectsDictMapper topicSubjectsDictMapper;
    @Resource
    private TopicSubjectsMapper topicSubjectsMapper;


    @RequestMapping(value = "/subject")
    public String toSubjectList(){
        return "subject";
    }

    //@RequestMapping("/toImport")
    //public String toImport(@RequestParam(value = "impFilename") MultipartFile[] files, RedirectAttributes model)
    //        throws IOException {
    //    try {
    //        // 判断文件是否为空
    //        if (files != null && files.length > 0) {
    //            for (int i = 0; i < files.length; i++) {
    //                MultipartFile file = files[i];
    //                log.info(file.getOriginalFilename());
    //                redisService.set("file", i + "", file.getOriginalFilename());
    //            }
    //        }
    //    } catch (Exception e) {
    //        throw e;
    //    }
    //    return "subject";
    //}

    @RequestMapping("/toImport")
    //@Transactional(transactionManager = "subjectTransaction",rollbackFor = Exception.class)
    public String toImport(@RequestParam(value = "impFilename") MultipartFile[] files, RedirectAttributes model)
            throws IOException {
        try {
            // 判断文件是否为空
            String retCode = "fail";
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    if (file == null) {
                        model.addFlashAttribute("retCode", retCode);
                        model.addFlashAttribute("retMsg", "文件不能为空！");
                        return "subject";
                    }
                    // 获取文件名
                    String fileName = file.getOriginalFilename();
                    // 进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
                    long size = file.getSize();
                    if (StringUtils.isEmpty(fileName) || size == 0) {
                        model.addFlashAttribute("retCode", retCode);
                        model.addFlashAttribute("retMsg", "文件不能为空！");
                        return "subject";
                    }
                    // 批量导入
                    JSONObject obj = beginImport(fileName, file);
                    model.addFlashAttribute("retCode", obj.get("retCode"));
                    model.addFlashAttribute("retMsg", obj.get("retMsg"));
                }
            }
        } catch (Exception e) {
            log.error("Exception:", e);
        }
        return "subject";
    }
    /**
     * 功能描述: <br>
     * 〈开始导入、上传excel文件到临时目录后并开始解析〉
     * @Param:
     * @Return:
     * @Author: BaiYuTing
     * @Date:
     * @update: [自定义变更内容]
     */
    public JSONObject beginImport(String fileName, MultipartFile mfile) {

        File uploadDir = new File("D:\\fileupload");
        // 创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists())
            uploadDir.mkdirs();
        // 新建一个文件
        File tempFile = new File("D:\\fileupload\\" + new Date().getTime() + ".xlsx");
        // 初始化输入流
        InputStream is = null;
        try {
            // 将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);

            // 根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            // 根据版本选择创建Workbook的方式
            Workbook wb = null;
            // 根据文件名判断文件是2003版本还是2007版本
            if (ExcelImportUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            return readExcelValue(wb, tempFile, fileName);
        } catch (Exception e) {
            log.error("Exception:", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    log.error("Exception:", e);
                }
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("retCode", "fail");
        obj.put("retMsg", "导入出错！请检查数据格式！");
        return obj;
    }

    private JSONObject readExcelValue(Workbook wb, File tempFile, String fileName) {
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.SECONDS, linkedBlockingQueue, new ThreadPoolExecutor.DiscardOldestPolicy());
        String[] split = fileName.split("/");
        fileName = split[1];
        String subjects = fileName.substring(0, 2);
        QueryWrapper<TopicSubjectsDictDTO> subjectDictWrapper = Wrappers.query();
        subjectDictWrapper.select("id").eq("subjects", subjects).last("limit 1");
        Integer subjectDictId = topicSubjectsDictMapper.selectOne(subjectDictWrapper).getId();
        QueryWrapper<TopicSubjectsDTO> sortWrapper = Wrappers.query();
        sortWrapper.select("max(sorts) as sorts");
        Integer sorts;
        TopicSubjectsDTO topicSubjectsDTO = topicSubjectsMapper.selectOne(sortWrapper);
        if (topicSubjectsDTO == null){
            sorts = 1;
        }else {
            Integer subjectsSorts = topicSubjectsDTO.getSorts();
            sorts = subjectsSorts++;
        }
        QueryWrapper<TopicSubjectsDTO> subjectWrapper = Wrappers.query();
        subjectWrapper.eq("grade_id", 2).eq("subjects_dict_id", subjectDictId).last("limit 1");
        TopicSubjectsDTO topicSubjectsDTO1 = topicSubjectsMapper.selectOne(subjectWrapper);
        if (topicSubjectsDTO1 == null) {
            topicSubjectsDTO1 = TopicSubjectsDTO.builder().gradeId(2).subjectsDictId(subjectDictId).subjects(subjects).sorts(sorts)
                    .build();
            topicSubjectsMapper.insert(topicSubjectsDTO1);
        }
        Integer subjectsId = topicSubjectsDTO1.getId();
        // 错误信息接收器
        JSONObject errorMsg = new JSONObject();
        int lineNum = 0;
        int cols = 0;
        try {
            errorMsg.put("retCode", "succ");
            // 得到第一个shell
            Sheet sheet = wb.getSheetAt(0);
            // 得到Excel的行数
            int totalRows = sheet.getPhysicalNumberOfRows();
            // 总列数
            int totalCells = 0;
            // 得到Excel的列数(前提是有行数)，从第二行算起
            if (totalRows >= 2 && sheet.getRow(0) != null) {
                totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            }
            Vector<Row> rows = new Vector<>();
            //Iterator<Row> iterator = rows.iterator();
            //for (; iterator.hasNext() ; iterator.next()) {
            //
            //}
            // 循环Excel行数,从第二行开始。标题不入库
            for (int r = 1; r < totalRows; r++) {
                lineNum++;
                Row row = sheet.getRow(r);
                //rows.add(row);

                ExcelThread excelThread = new ExcelThread(errorMsg, totalCells, row, cols, subjectsId, fileName, lineNum, subjectDictId);
                threadPoolExecutor.submit(excelThread);
                //errorMsg = submit.get();
                //errorMsg = saveExcel(errorMsg, totalCells, row, cols, subjectsId, fileName, lineNum, subjectDictId);
                //String retCode = errorMsg.get("retCode").toString();
                //if (retCode.equals("fail")){
                //    return errorMsg;
                //}
            }
            //BigDecimal divide = new BigDecimal(totalRows).divide(new BigDecimal(4), 0, BigDecimal.ROUND_UP);
            //List<List<Row>> partition = Lists.partition(rows, divide.intValue());
            //List<List<Row>> partition = Lists.partition(rows, 100);
            //for (int i = 0; i < partition.size(); i++) {
            //    List<Row> rowList = partition.get(i);
            //    ExcelThread2 excelThread2 = new ExcelThread2(errorMsg, totalCells, rowList, cols, subjectsId, fileName, lineNum, subjectDictId);
            //    threadPoolExecutor.submit(excelThread2);
            //}
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (Exception e) {
            log.error("Exception:", e);
            errorMsg.put("retCode", "fail");
            errorMsg.put("retMsg", fileName + "中第" + (lineNum + 1) + "行、第" + cols + "列数据格式不正确，请仔细检查！");
            return errorMsg;
        }
        return errorMsg;
    }







}
