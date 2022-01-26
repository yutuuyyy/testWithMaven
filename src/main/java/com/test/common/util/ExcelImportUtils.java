package com.test.common.util;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.test.common.myEnum.ExcelVersionEnum;
import com.test.entity.ExcelSheet;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelImportUtils {
    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    // @描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public static boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            return false;
        }
        return true;
    }

    /**
     * 标题样式
     */
    private final static String STYLE_HEADER = "header";
    /**
     * 表头样式
     */
    private final static String STYLE_TITLE = "title";
    /**
     * 数据样式
     */
    private final static String STYLE_DATA = "data";

    /**
     * 存储样式
     */
    private static final HashMap<String, CellStyle> cellStyleMap = new HashMap<>();

    /**
     * 服务器读取excel文件里面的内容 支持日期，数字，字符，函数公式，布尔类型
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ExcelSheet> readExcel(MultipartFile mulFile, Integer rowCount, Integer columnCount)
            throws FileNotFoundException, IOException {
        File file = null;
        if (mulFile.equals("") || mulFile.getSize() <= 0) {
            mulFile = null;
        } else {
            InputStream ins = null;
            ins = mulFile.getInputStream();
            //代表为选用文件夹方式上传
            String[] tempArr = mulFile.getOriginalFilename().split("/");
            file = new File(tempArr[tempArr.length - 1]);
            inputStreamToFile(ins, file);
            ins.close();
        }
        String fileName = file.getName();
        // 根据后缀名称判断excel的版本
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        Workbook wb = null;
        if (ExcelVersionEnum.V2003.getSuffix().equals(extName)) {
            wb = new HSSFWorkbook(new FileInputStream(file));

        } else if (ExcelVersionEnum.V2007.getSuffix().equals(extName)) {
            wb = new XSSFWorkbook(new FileInputStream(file));

        } else {
            // 无效后缀名称，这里之能保证excel的后缀名称，不能保证文件类型正确，不过没关系，在创建Workbook的时候会校验文件格式
            throw new IllegalArgumentException("Invalid excel version");
        }
        // 开始读取数据
        List<ExcelSheet> sheetPOs = new ArrayList<>();
        // 解析sheet
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            List<List<Object>> dataList = new ArrayList<>();
            ExcelSheet sheetPO = new ExcelSheet();
            sheetPO.setSheetName(sheet.getSheetName());
            sheetPO.setDataList(dataList);
            int readRowCount = 0;
            if (rowCount == null || rowCount > sheet.getPhysicalNumberOfRows()) {
                readRowCount = sheet.getPhysicalNumberOfRows();
            } else {
                readRowCount = rowCount;
            }
            // 解析sheet 的行（第二行开始解析）
            for (int j = sheet.getFirstRowNum() + 1; j < readRowCount; j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                if (row.getFirstCellNum() < 0) {
                    continue;
                }
                int readColumnCount = 0;
                if (columnCount == null || columnCount > row.getLastCellNum()) {
                    readColumnCount = (int) row.getLastCellNum();
                } else {
                    readColumnCount = columnCount;
                }
                List<Object> rowValue = new LinkedList<Object>();
                // 解析sheet 的列
                for (int k = 0; k < readColumnCount; k++) {
                    Cell cell = row.getCell(k);
                    rowValue.add(getCellValue(wb, cell));
                }
                dataList.add(rowValue);
            }
            sheetPOs.add(sheetPO);
        }
        if (!file.delete()) {
            throw new IllegalArgumentException("文件删除失败");
        }
        return sheetPOs;
    }

    /**
     * 本地读取excel文件里面的内容 支持日期，数字，字符，函数公式，布尔类型
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ExcelSheet> readExcel(File file, Integer rowCount, Integer columnCount)
            throws FileNotFoundException, IOException {

        String fileName = file.getName();
        // 根据后缀名称判断excel的版本
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        Workbook wb = null;
        if (ExcelVersionEnum.V2003.getSuffix().equals(extName)) {
            wb = new HSSFWorkbook(new FileInputStream(file));

        } else if (ExcelVersionEnum.V2007.getSuffix().equals(extName)) {
            wb = new XSSFWorkbook(new FileInputStream(file));

        } else {
            // 无效后缀名称，这里之能保证excel的后缀名称，不能保证文件类型正确，不过没关系，在创建Workbook的时候会校验文件格式
            throw new IllegalArgumentException("Invalid excel version");
        }
        // 开始读取数据
        List<ExcelSheet> sheetPOs = new ArrayList<>();
        // 解析sheet
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            List<List<Object>> dataList = new ArrayList<>();
            ExcelSheet sheetPO = new ExcelSheet();
            sheetPO.setSheetName(sheet.getSheetName());
            sheetPO.setDataList(dataList);
            int readRowCount = 0;
            if (rowCount == null || rowCount > sheet.getPhysicalNumberOfRows()) {
                readRowCount = sheet.getPhysicalNumberOfRows();
            } else {
                readRowCount = rowCount;
            }
            // 解析sheet 的行（第二行开始解析）
            for (int j = sheet.getFirstRowNum() + 1; j < readRowCount; j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                if (row.getFirstCellNum() < 0) {
                    continue;
                }
                int readColumnCount = 0;
                if (columnCount == null || columnCount > row.getLastCellNum()) {
                    readColumnCount = (int) row.getLastCellNum();
                } else {
                    readColumnCount = columnCount;
                }
                List<Object> rowValue = new LinkedList<Object>();
                // 解析sheet 的列
                for (int k = 0; k < readColumnCount; k++) {
                    Cell cell = row.getCell(k);
                    rowValue.add(getCellValue(wb, cell));
                }
                dataList.add(rowValue);
            }
            sheetPOs.add(sheetPO);
        }
        return sheetPOs;
    }


    public static Object getCellValue(Workbook wb, Cell cell) {
        Object columnValue = null;
        if (cell != null) {
            DecimalFormat df = new DecimalFormat("0");// 格式化 number
            // String
            // 字符
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    columnValue = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                        columnValue = df.format(cell.getNumericCellValue());
                    } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                        columnValue = cell.getNumericCellValue();
                        //防止int后面有个.0
                        String v = String.format("%.1f", columnValue);      //默认的整数后边是一个小数点，和一个零
                        columnValue = (v).replaceAll("\\.0*$", "");//整数作为浮点数格式化以后，删除结尾的点零
                    } else {
                        columnValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    columnValue = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    columnValue = "";
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    // 格式单元格
                    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                    evaluator.evaluateFormulaCell(cell);
                    CellValue cellValue = evaluator.evaluate(cell);
                    columnValue = cellValue.getNumberValue();
                    break;
                default:
                    columnValue = cell.toString();
            }
        }
        return columnValue;
    }

    /**
     * 在硬盘上写入excel文件
     *
     * @throws IOException
     */
    public static void createWorkbookAtDisk(ExcelVersionEnum version, List<ExcelSheet> excelSheets, String filePath)
            throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filePath);
        createWorkbookAtOutStream(version, excelSheets, fileOut, true);
    }

    /**
     * 将Excel写入到response的输出流中，供页面下载
     *
     * @throws IOException
     */
    public static void createWorkbookAtOutput(ExcelVersionEnum version, List<ExcelSheet> excelSheets, String fileName, HttpServletResponse response)
            throws IOException {
        //生成文件的文件名
        fileName = new String((fileName + ".xlsx").getBytes("UTF-8"), "iso-8859-1");
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");
        //这后面可以设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        //刷新缓冲
        response.flushBuffer();
        //workbook将Excel写入到response的输出流中，供页面下载
        OutputStream os = response.getOutputStream();
        createWorkbookAtOutStream(version, excelSheets, os, true);
    }

    /**
     * 把excel表格写入输出流中，输出流会被关闭
     *
     * @throws IOException
     */
    public static void createWorkbookAtOutStream(ExcelVersionEnum version, List<ExcelSheet> excelSheets,
                                                 OutputStream outStream, boolean closeStream) throws IOException {
        if (CollectionUtils.isNotEmpty(excelSheets)) {
            Workbook wb = createWorkBook(version, excelSheets);
            wb.write(outStream);
            if (closeStream) {
                outStream.close();
            }
        }
    }

    private static Workbook createWorkBook(ExcelVersionEnum version, List<ExcelSheet> excelSheets) {
        Workbook wb = createWorkbook(version);
        for (int i = 0; i < excelSheets.size(); i++) {
            ExcelSheet excelSheetPO = excelSheets.get(i);
            if (excelSheetPO.getSheetName() == null) {
                excelSheetPO.setSheetName("sheet" + i);
            }
            // 过滤特殊字符
            Sheet tempSheet = wb.createSheet(WorkbookUtil.createSafeSheetName(excelSheetPO.getSheetName()));
            buildSheetData(wb, tempSheet, excelSheetPO, version);
        }
        return wb;
    }

    private static void buildSheetData(Workbook wb, Sheet sheet, ExcelSheet excelSheetPO, ExcelVersionEnum version) {
        sheet.setDefaultRowHeight((short) 400);
        sheet.setDefaultColumnWidth((short) 10);
        createTitle(sheet, excelSheetPO, wb, version);
        createHeader(sheet, excelSheetPO, wb, version);
        createBody(sheet, excelSheetPO, wb, version);
    }

    private static void createBody(Sheet sheet, ExcelSheet excelSheetPO, Workbook wb, ExcelVersionEnum version) {
        int rownum = 1;//表头
        if (null != excelSheetPO && null != excelSheetPO.getTitle() && !excelSheetPO.getTitle().equals("")) {
            rownum = 2;//带标题
        }
        List<List<Object>> dataList = excelSheetPO.getDataList();
        for (int i = 0; i < dataList.size() && i < version.getMaxRow(); i++) {
            List<Object> values = dataList.get(i);
            Row row = sheet.createRow(rownum + i);
            for (int j = 0; j < values.size() && j < version.getMaxColumn(); j++) {
                Cell cell = row.createCell(j);
                //cell.setCellStyle(getStyle(STYLE_DATA, wb));导致速度过慢
                //cell.setCellValue(values.get(j).toString());
                //根据不同类型，导出不同格式
                Object param = values.get(j);
                if (param instanceof Integer) {
                    int value = ((Integer) param).intValue();
                    cell.setCellValue(value);
                } else if (param instanceof String) {
                    String value = (String) param;
                    cell.setCellValue(value);
                } else if (param instanceof Double) {
                    double value = ((Double) param).doubleValue();
                    cell.setCellValue(value);
                } else if (param instanceof Float) {
                    float value = ((Float) param).floatValue();
                    cell.setCellValue(value);
                } else if (param instanceof Long) {
                    long value = ((Long) param).longValue();
                    cell.setCellValue(value);
                } else if (param instanceof Boolean) {
                    boolean value = ((Boolean) param).booleanValue();
                    cell.setCellValue(value);
                } else if (param instanceof Date) {
                    Date value = (Date) param;
                    cell.setCellValue(value);
                }
            }
        }

    }

    private static void createHeader(Sheet sheet, ExcelSheet excelSheetPO, Workbook wb, ExcelVersionEnum version) {
        int rownum = 0;
        if (null != excelSheetPO && null != excelSheetPO.getTitle() && !excelSheetPO.getTitle().equals("")) {
            rownum = 1;
        }
        String[] headers = excelSheetPO.getHeaders();
        Row row = sheet.createRow(rownum);
        for (int i = 0; i < headers.length && i < version.getMaxColumn(); i++) {
            Cell cellHeader = row.createCell(i);
            cellHeader.setCellStyle(getStyle(STYLE_HEADER, wb));
            cellHeader.setCellValue(headers[i]);
        }

    }

    private static void createTitle(Sheet sheet, ExcelSheet excelSheetPO, Workbook wb, ExcelVersionEnum version) {
        if (null != excelSheetPO && null != excelSheetPO.getTitle() && !excelSheetPO.getTitle().equals("")) {
            Row titleRow = sheet.createRow(0);
            Cell titleCel = titleRow.createCell(0);
            titleCel.setCellValue(excelSheetPO.getTitle());
            titleCel.setCellStyle(getStyle(STYLE_TITLE, wb));
            // 限制最大列数
//		int column = excelSheetPO.getDataList().size() > version.getMaxColumn() ? version.getMaxColumn()
//				: excelSheetPO.getDataList().size();
            int column = excelSheetPO.getHeaders().length > version.getMaxColumn() ? version.getMaxColumn()
                    : excelSheetPO.getHeaders().length;
            if (column == 0) {//没有数据时column为0，合并列错误：java.lang.IllegalArgumentException: Minimum column number is 0
                column = 1;
            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, column - 1));
        }
    }

    private static CellStyle getStyle(String type, Workbook wb) {
//		if (cellStyleMap.containsKey(type)) {
//			return cellStyleMap.get(type);
//		}
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
//		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setWrapText(true);

        if (STYLE_HEADER == type) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 11);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
        } else if (STYLE_TITLE == type) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 11);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
        } else if (STYLE_DATA == type) {
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
        }
        cellStyleMap.put(type, style);
        return style;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Workbook createWorkbook(ExcelVersionEnum version) {
        switch (version) {
            case V2003:
                return new HSSFWorkbook();
            case V2007:
                return new XSSFWorkbook();
        }
        return null;
    }

    public static String getStringCellVal(Row row, int c) {
        Cell cell = row.getCell(c);
        if (cell == null) {
            cell = row.createCell(c);
        }
        String cellVal = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellVal = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                cellVal = "";
                break;
            default:
                cellVal = "needText";
        }
        return cellVal;
    }

    public static Map<String, Object> getWorkbook(MultipartFile mulFile) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            File file = null;
            if (mulFile.equals("") || mulFile.getSize() <= 0) {
                mulFile = null;
            } else {
                InputStream ins = null;
                ins = mulFile.getInputStream();
                //代表为选用文件夹方式上传
                String[] tempArr = mulFile.getOriginalFilename().split("/");
                file = new File(tempArr[tempArr.length - 1]);
                ExcelImportUtils.inputStreamToFile(ins, file);
                ins.close();
            }
            String fileName = file.getName();
            // 根据后缀名称判断excel的版本
            String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
            Workbook wb = null;
            if (ExcelVersionEnum.V2003.getSuffix().equals(extName)) {
                wb = new HSSFWorkbook(new FileInputStream(file));

            } else if (ExcelVersionEnum.V2007.getSuffix().equals(extName)) {
                wb = new XSSFWorkbook(new FileInputStream(file));

            } else {
                // 无效后缀名称，这里之能保证excel的后缀名称，不能保证文件类型正确，不过没关系，在创建Workbook的时候会校验文件格式
                throw new IllegalArgumentException("Invalid excel version");
            }
            map.put("file", file);
            map.put("workbook", wb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
