package top.pcstar.website.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName ExcelUtils
 * @Description: Excel工具类
 * @Author: panchao
 * @Date: Created in 19-1-15 下午3:14
 * @Version: 1.0
 */
public class ExcelUtils {
    /**
     * Excel类型
     */
    public enum XLSType {
        XLS("xls", "Excel 97-2003文件"), XLSX("xlsx", "Excel文件");
        private String code;
        private String message;

        XLSType(String type, String message) {
            this.code = type;
            this.message = message;
        }

        /**
         * 根据code获取Excel类型
         *
         * @param code
         * @return
         */
        public static XLSType getXLSTypeByCode(String code) {
            for (XLSType xlsType : XLSType.values()) {
                if (xlsType.getCode().equals(code)) {
                    return xlsType;
                }
            }
            return null;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * 通过Excel名称后缀获取Excel类型
     *
     * @param excleName
     * @return
     */
    public static XLSType getXLSTypeByExcleNameSuffix(String excleName) {
        if (excleName == null)
            return null;
        if (excleName.endsWith(XLSType.XLS.getCode()))
            return XLSType.XLS;
        if (excleName.endsWith(XLSType.XLSX.getCode()))
            return XLSType.XLSX;
        else
            return null;
    }

    /**
     * 根据Excel文件输入流和Excel文件类型获取Workbook
     *
     * @param in
     * @param xlsType
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook getWorkbookByInputStreamAndXLSType(InputStream in, XLSType xlsType) throws IOException, InvalidFormatException {
        if (in == null)
            return null;
        Workbook workbook = null;
        if (xlsType == XLSType.XLSX) {
            OPCPackage opc = OPCPackage.open(in);
            workbook = new XSSFWorkbook(opc);
        } else if (xlsType == XLSType.XLS)
            workbook = new HSSFWorkbook(in, false);
        return workbook;
    }
}
