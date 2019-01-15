package top.pcstar.website.service;

import org.springframework.web.multipart.MultipartFile;
import top.pcstar.website.vo.ResultModel;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @ClassName ExcelService
 * @Description: Excel服务接口
 * @Author: panchao
 * @Date: Created in 19-1-11 下午9:17
 * @Version: 1.0
 */
public interface ExcelService {
    /**
     *
     * @param file
     * @param response
     * @param largeCuttingValue
     * @param sampleNumber
     * @param fromNumber
     * @throws Exception
     */
    void importSampleReqDatas(MultipartFile file, HttpServletResponse response, BigDecimal largeCuttingValue,
                              int sampleNumber, int fromNumber) throws Exception;

    ResultModel getNoLargeAmountNumber(MultipartFile file, BigDecimal largeCuttingValue) throws Exception;
}
