package top.pcstar.website.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.pcstar.website.service.ExcelService;
import top.pcstar.website.vo.ResultModel;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @ClassName ExcelController
 * @Description: Excel控制类
 * @Author: panchao
 * @Date: Created in 19-1-11 下午1:56
 * @Version: 1.0
 */
@Controller
public class ExcelController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    private ExcelService excelService;

    @RequestMapping(value = "/exportExcelTemplate", method = RequestMethod.POST)
    @ResponseBody
    public void exportExcelTemplate(@RequestParam(value = "file") MultipartFile file,
                                    HttpServletResponse response,
                                    @RequestParam(value = "largeCuttingValue") BigDecimal largeCuttingValue,
                                    @RequestParam(value = "sampleNumber") int sampleNumber,
                                    @RequestParam(value = "fromNumber") int fromNumber) throws Exception {
        excelService.importSampleReqDatas(file, response, largeCuttingValue, sampleNumber,fromNumber);
    }

    @RequestMapping(value = "/checkNoLarge", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResultModel> checkNoLarge(@RequestParam(value = "file") MultipartFile file,
                                       @RequestParam(value = "largeCuttingValue") BigDecimal largeCuttingValue) throws Exception {
        ResultModel noLargeAmountNumber = excelService.getNoLargeAmountNumber(file, largeCuttingValue);
        return ResponseEntity.ok(noLargeAmountNumber);
    }
}
