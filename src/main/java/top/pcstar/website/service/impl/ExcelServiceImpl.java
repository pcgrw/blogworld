package top.pcstar.website.service.impl;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.pcstar.website.service.ExcelService;
import top.pcstar.website.utils.ExcelUtils;
import top.pcstar.website.utils.ExcelUtils.XLSType;
import top.pcstar.website.vo.ResultModel;
import top.pcstar.website.vo.SampleExcel;

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
        XLSType xlsType = ExcelUtils.getXLSTypeByExcleNameSuffix(file.getOriginalFilename());
        Workbook workbook = ExcelUtils.getWorkbookByInputStreamAndXLSType(file.getInputStream(), xlsType);
        //解析Excel数据
        SampleExcel sampleExcel = parseExcelData(workbook);

        /**
         * 分离大额数据和非大额数据
         */
        partLargeAndNoLargeData(largeCuttingValue, sampleExcel);

        List<Map<String, Object>> systemSampleDatas = new ArrayList<>();//系统抽样数据
        int size = sampleExcel.getNoLargeAmountDatas().size();//总大小
        int step = (size - fromNumber) / sampleNumber;//步长
        StringBuilder sampleNumberStringBuilder = new StringBuilder();
        for (int i = fromNumber - 1, j = 0; i < size && j < sampleNumber; i = i + step, j++) {
            Map<String, Object> map = sampleExcel.getNoLargeAmountDatas().get(i);
            map.put("cell" + sampleExcel.getIsSampleCol(), "√");
            map.put("cell" + sampleExcel.getSampleMethodCol(), "系统抽样");
            systemSampleDatas.add(map);
            sampleNumberStringBuilder.append(MapUtils.getIntValue(map, "cell" + sampleExcel.getOrderNumberCol()));
            if (j < (sampleNumber - 1)) {
                sampleNumberStringBuilder.append(",");
            }
        }
        if (workbook instanceof XSSFWorkbook) {
            workbook = new SXSSFWorkbook((XSSFWorkbook) workbook, 10000);//内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘
        }
        Sheet totalSheet = workbook.createSheet("全量数据");
        List<Map<String, Object>> totalDatas = new ArrayList<>();//全量数据
        totalDatas.add(sampleExcel.getTitleRowMaps());
        totalDatas.addAll(sampleExcel.getLargeAmountDatas());
        totalDatas.addAll(sampleExcel.getNoLargeAmountDatas());
        for (int i = 0; i < totalDatas.size(); i++) {
            Row totalSheetRow = totalSheet.createRow(i);
            for (int j = 0; j < sampleExcel.getTotalCol(); j++) {
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
        finalDatas.add(sampleExcel.getTitleRowMaps());
        finalDatas.addAll(sampleExcel.getLargeAmountDatas());
        finalDatas.addAll(systemSampleDatas);
        for (int i = 0; i < finalDatas.size(); i++) {
            Row sampleSheetRow = sampleSheet.createRow(i);
            for (int j = 0; j < sampleExcel.getTotalCol(); j++) {
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
                .append("2、期末余额低于").append(largeCuttingValue).append("元的，共计").append(sampleExcel.getNoLargeAmountDatas().size())
                .append("个样本，采用系统抽样共抽取").append(sampleNumber).append("个，自第").append(fromNumber)
                .append("号起，间隔为").append(step).append("，选择序号为").append(sampleNumberStringBuilder).append("的样本").append("\r\n");
        CellRangeAddress callRangeAddress1 = new CellRangeAddress(finalDatas.size() + 4, finalDatas.size() + 6, 0, sampleExcel.getTotalCol() * 2);
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
        XLSType xlsType = ExcelUtils.getXLSTypeByExcleNameSuffix(file.getOriginalFilename());
        Workbook workbook = ExcelUtils.getWorkbookByInputStreamAndXLSType(file.getInputStream(), xlsType);
        //解析Excel数据
        SampleExcel sampleExcel = parseExcelData(workbook);
        //分离大额数据和非大额数据
        partLargeAndNoLargeData(largeCuttingValue, sampleExcel);
        resultModel.setCode(1);
        resultModel.setMessage("数据总共有" + (sampleExcel.getData().size()) + "条；大额数据有" +
                sampleExcel.getLargeAmountDatas().size() + "条；非大额数据有" + sampleExcel.getNoLargeAmountDatas().size() + "条。");
        return resultModel;
    }

    /**
     * 分离大额数据和非大额数据
     *
     * @param largeCuttingValue
     * @param sampleExcel
     */
    private void partLargeAndNoLargeData(BigDecimal largeCuttingValue, SampleExcel sampleExcel) {
        int orderNumber = 0;
        for (int i = 0; i < sampleExcel.getData().size(); i++) {
            Map<String, Object> rowMaps = sampleExcel.getData().get(i);
            double doubleValue = MapUtils.getDoubleValue(rowMaps, "cell" + sampleExcel.getAmountCol());
            BigDecimal amount = new BigDecimal(doubleValue).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (amount.compareTo(largeCuttingValue) == 1) {
                rowMaps.put("cell" + sampleExcel.getIsSampleCol(), "√");
                rowMaps.put("cell" + sampleExcel.getSampleMethodCol(), "大额");
                sampleExcel.getLargeAmountDatas().add(rowMaps);
            } else {
                rowMaps.put("cell" + sampleExcel.getOrderNumberCol(), ++orderNumber);
                rowMaps.put("cell" + sampleExcel.getSampleMethodCol(), "未抽取");
                sampleExcel.getNoLargeAmountDatas().add(rowMaps);
            }
        }
    }

    /**
     * 解析Excel数据
     *
     * @param workbook
     * @return
     */
    private SampleExcel parseExcelData(Workbook workbook) {
        SampleExcel sampleExcel = new SampleExcel();
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
        Map<String, Object> titleRowMaps = excelDatas.remove(0);
        int amountCol = -1;//金额列
        int orderNumberCol = -1;//序号列
        int isSampleCol = -1;//是否选样
        int sampleMethodCol = -1;//选样方式
        for (int i = 0; i < totalCol; i++) {
            String cellValue = MapUtils.getString(titleRowMaps, "cell" + i);
            if (StringUtils.equals(cellValue, "金额")) {
                amountCol = i;
            }
            if (StringUtils.equals(cellValue, "序号")) {
                orderNumberCol = i;
            }
            if (StringUtils.equals(cellValue, "是否选样")) {
                isSampleCol = i;
            }
            if (StringUtils.equals(cellValue, "选样方式")) {
                sampleMethodCol = i;
            }
        }
        sampleExcel.setTotalCol(totalCol);
        sampleExcel.setTitleRowMaps(titleRowMaps);
        sampleExcel.setData(excelDatas);
        sampleExcel.setAmountCol(amountCol);
        sampleExcel.setOrderNumberCol(orderNumberCol);
        sampleExcel.setIsSampleCol(isSampleCol);
        sampleExcel.setSampleMethodCol(sampleMethodCol);
        return sampleExcel;
    }
}
