package top.pcstar.website.service.impl;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.pcstar.website.service.ExcelService;
import top.pcstar.website.vo.ResultModel;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExcelServiceImpl
 * @Description: Excel服务实现类
 * @Author: panchao
 * @Date: Created in 19-1-11 下午9:19
 * @Version: 1.0
 */
@Service
public class ExcelServiceImpl implements ExcelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelServiceImpl.class);

    @Override
    public void importSampleReqDatas(MultipartFile file, HttpServletResponse response, BigDecimal largeCuttingValue,
                                     int sampleNumber, int fromNumber) throws Exception {
        OPCPackage opc = OPCPackage.open(file.getInputStream());
        Workbook workbook = new XSSFWorkbook(opc);
        int sheetNums = workbook.getNumberOfSheets();
        LOGGER.info("总sheet数：" + sheetNums);
        Sheet sheet = workbook.getSheetAt(0);
        int rowNums = sheet.getLastRowNum() + 1;
        LOGGER.info("总行数：" + rowNums);
        List<Map<String, Object>> excelDatas = new ArrayList<>();
        short totalCol = -1;//总列数
        for (int i = 0; i < rowNums; i++) {
            Row row = sheet.getRow(i);
            if (i == 0) {
                totalCol = row.getLastCellNum();
                LOGGER.info("总列数：" + totalCol);
            }
            Map<String, Object> rowMaps = new HashMap<>();
            for (int j = 0; j < totalCol; j++) {
                Object value = null;
                Cell cell = row.getCell(j);
                if (cell == null) {
                    value = null;
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    value = cell.getNumericCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    value = cell.getStringCellValue();
                }
                rowMaps.put("cell" + j, value);
            }
            excelDatas.add(rowMaps);
        }
        int amountCol = -1;//金额列
        int orderNumberCol = -1;//序号列
        int isSample = -1;//是否选样
        int sampleMethod = -1;//选样方式
        Map<String, Object> titleRowMaps = excelDatas.remove(0);
        for (int i = 0; i < totalCol; i++) {
            String cellValue = MapUtils.getString(titleRowMaps, "cell" + i);
            if (StringUtils.equals(cellValue, "金额")) {
                amountCol = i;
            }
            if (StringUtils.equals(cellValue, "序号")) {
                orderNumberCol = i;
            }
            if (StringUtils.equals(cellValue, "是否选样")) {
                isSample = i;
            }
            if (StringUtils.equals(cellValue, "选样方式")) {
                sampleMethod = i;
            }
        }
        List<Map<String, Object>> largeAmountDatas = new ArrayList<>();//大额数据
        List<Map<String, Object>> noLargeAmountDatas = new ArrayList<>();//非大额数据
        int orderNumber = 0;
        for (int i = 0; i < excelDatas.size(); i++) {
            Map<String, Object> rowMaps = excelDatas.get(i);
            double doubleValue = MapUtils.getDoubleValue(rowMaps, "cell" + amountCol);
            BigDecimal amount = new BigDecimal(doubleValue).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (amount.compareTo(largeCuttingValue) == 1) {
                rowMaps.put("cell" + isSample, "√");
                rowMaps.put("cell" + sampleMethod, "大额");
                largeAmountDatas.add(rowMaps);
            } else {
                rowMaps.put("cell" + orderNumberCol, ++orderNumber);
                rowMaps.put("cell" + sampleMethod, "未抽取");
                noLargeAmountDatas.add(rowMaps);
            }
        }

        List<Map<String, Object>> systemSampleDatas = new ArrayList<>();//系统抽样数据
//        List<Map<String, Object>> nosample = new ArrayList<>();//未抽样数据
        int size = noLargeAmountDatas.size();//总大小
        int step = (size - fromNumber) / sampleNumber;//步长
        StringBuilder sampleNumberStringBuilder = new StringBuilder();
        for (int i = fromNumber - 1, j = 0; i < size && j < sampleNumber; i = i + step, j++) {
            Map<String, Object> map = noLargeAmountDatas.get(i);
            map.put("cell" + isSample, "√");
            map.put("cell" + sampleMethod, "系统抽样");
            systemSampleDatas.add(map);
            sampleNumberStringBuilder.append(MapUtils.getIntValue(map, "cell" + orderNumberCol));
            if (j < (sampleNumber - 1)) {
                sampleNumberStringBuilder.append(",");
            }
        }
        Sheet totalSheet = workbook.createSheet("全量数据");
        List<Map<String, Object>> totalDatas = new ArrayList<>();//全量数据
        totalDatas.add(titleRowMaps);
        totalDatas.addAll(largeAmountDatas);
        totalDatas.addAll(noLargeAmountDatas);
        for (int i = 0; i < totalDatas.size(); i++) {
            Row totalSheetRow = totalSheet.createRow(i);
            for (int j = 0; j < totalCol; j++) {
                Cell totalSheetRowCell = totalSheetRow.createCell(j);
                Object oValue = totalDatas.get(i).get("cell" + j);
                if (oValue == null) {
                    totalSheetRowCell.setCellValue("");
                } else if (oValue instanceof BigDecimal) {
                    totalSheetRowCell.setCellValue(((BigDecimal) oValue).doubleValue());
                } else if (oValue instanceof Double) {
                    totalSheetRowCell.setCellValue((Double) oValue);
                } else {
                    totalSheetRowCell.setCellValue(String.valueOf(oValue));
                }
            }
        }
        Sheet sampleSheet = workbook.createSheet("选样数据");
        List<Map<String, Object>> finalDatas = new ArrayList<>();//最终数据
        finalDatas.add(titleRowMaps);
        finalDatas.addAll(largeAmountDatas);
        finalDatas.addAll(systemSampleDatas);
        for (int i = 0; i < finalDatas.size(); i++) {
            Row sampleSheetRow = sampleSheet.createRow(i);
            for (int j = 0; j < totalCol; j++) {
                Cell sampleSheetRowCell = sampleSheetRow.createCell(j);
                Object oValue = finalDatas.get(i).get("cell" + j);
                if (oValue == null) {
                    sampleSheetRowCell.setCellValue("");
                } else if (oValue instanceof BigDecimal) {
                    sampleSheetRowCell.setCellValue(((BigDecimal) oValue).doubleValue());
                } else if (oValue instanceof Double) {
                    sampleSheetRowCell.setCellValue((Double) oValue);
                } else {
                    sampleSheetRowCell.setCellValue(String.valueOf(oValue));
                }
            }
        }
        /**
         * 选样说明：
         * 1、余额超过10万元以上均选取，
         * 2、期末余额低于10万元的，共计15个样本，采用系统抽样共抽取5个，自第2号起，间隔为3，选择序号为1，4，7，10，13的样本
         */
        StringBuilder description = new StringBuilder();
        description.append("选样说明：").append("\r\n")
                .append("1、余额超过").append(largeCuttingValue).append("元以上均选取，").append("\r\n")
                .append("2、期末余额低于").append(largeCuttingValue).append("元的，共计").append(noLargeAmountDatas.size())
                .append("个样本，采用系统抽样共抽取").append(sampleNumber).append("个，自第").append(fromNumber)
                .append("号起，间隔为").append(step).append("，选择序号为").append(sampleNumberStringBuilder).append("的样本").append("\r\n");
        CellRangeAddress callRangeAddress1 = new CellRangeAddress(finalDatas.size() + 4, finalDatas.size() + 6, 0, totalCol * 2);
        sampleSheet.addMergedRegion(callRangeAddress1);
        Row descriptionSheetRow = sampleSheet.createRow(finalDatas.size() + 4);
        Cell sampleSheetRowCell = descriptionSheetRow.createCell(0);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        sampleSheetRowCell.setCellStyle(cellStyle);
        sampleSheetRowCell.setCellValue(new XSSFRichTextString(description.toString()));
        // 设置response的编码方式
        response.setContentType("application/x-msdownload");
        // 设置附加文件名
        String fileName = file.getOriginalFilename();
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName.substring(0, fileName.lastIndexOf(".")) + "(选样结果)" + fileName.substring(fileName.lastIndexOf("."))));
        try (OutputStream output = response.getOutputStream()) {
            workbook.write(output);
            output.flush();
        }
    }

    @Override
    public ResultModel getNoLargeAmountNumber(MultipartFile file, BigDecimal largeCuttingValue) throws Exception {
        ResultModel<Map<String, Integer>> resultModel = new ResultModel<>();
        OPCPackage opc = OPCPackage.open(file.getInputStream());
        Workbook workbook = new XSSFWorkbook(opc);
        int sheetNums = workbook.getNumberOfSheets();
        LOGGER.info("总sheet数：" + sheetNums);
        Sheet sheet = workbook.getSheetAt(0);
        int rowNums = sheet.getLastRowNum() + 1;
        LOGGER.info("总行数：" + rowNums);
        List<Map<String, Object>> excelDatas = new ArrayList<>();
        short totalCol = -1;//总列数
        for (int i = 0; i < rowNums; i++) {
            Row row = sheet.getRow(i);
            if (i == 0) {
                totalCol = row.getLastCellNum();
                LOGGER.info("总列数：" + totalCol);
            }
            Map<String, Object> rowMaps = new HashMap<>();
            for (int j = 0; j < totalCol; j++) {
                Object value = null;
                Cell cell = row.getCell(j);
                if (cell == null) {
                    value = null;
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    value = cell.getNumericCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    value = cell.getStringCellValue();
                }
                rowMaps.put("cell" + j, value);
            }
            excelDatas.add(rowMaps);
        }
        int amountCol = -1;//金额列
        int orderNumberCol = -1;//序号列
        int isSample = -1;//是否选样
        int sampleMethod = -1;//选样方式
        Map<String, Object> titleRowMaps = excelDatas.remove(0);
        for (int i = 0; i < totalCol; i++) {
            String cellValue = MapUtils.getString(titleRowMaps, "cell" + i);
            if (StringUtils.equals(cellValue, "金额")) {
                amountCol = i;
            }
            if (StringUtils.equals(cellValue, "序号")) {
                orderNumberCol = i;
            }
            if (StringUtils.equals(cellValue, "是否选样")) {
                isSample = i;
            }
            if (StringUtils.equals(cellValue, "选样方式")) {
                sampleMethod = i;
            }
        }
        List<Map<String, Object>> largeAmountDatas = new ArrayList<>();//大额数据
        List<Map<String, Object>> noLargeAmountDatas = new ArrayList<>();//非大额数据
        int orderNumber = 0;
        for (int i = 0; i < excelDatas.size(); i++) {
            Map<String, Object> rowMaps = excelDatas.get(i);
            double doubleValue = MapUtils.getDoubleValue(rowMaps, "cell" + amountCol);
            BigDecimal amount = new BigDecimal(doubleValue).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (amount.compareTo(largeCuttingValue) == 1) {
                rowMaps.put("cell" + isSample, "√");
                rowMaps.put("cell" + sampleMethod, "大额");
                largeAmountDatas.add(rowMaps);
            } else {
                rowMaps.put("cell" + orderNumberCol, ++orderNumber);
                rowMaps.put("cell" + sampleMethod, "未抽取");
                noLargeAmountDatas.add(rowMaps);
            }
        }
        resultModel.setCode(1);
        resultModel.setMessage("数据总共有" + (rowNums - 1) + "条；大额数据有" +
                largeAmountDatas.size() + "条；非大额数据有" + noLargeAmountDatas.size() + "条。");
        return resultModel;
    }
}
