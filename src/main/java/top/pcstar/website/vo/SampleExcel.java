package top.pcstar.website.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SampleExcelAttribute
 * @Description: 选样Excel数据和属性
 * @Author: panchao
 * @Date: Created in 19-1-15 下午4:16
 * @Version: 1.0
 */
public class SampleExcel {
    /**
     * 总列数
     */
    private int totalCol;
    /**
     * 金额列index
     */
    private int amountCol;
    /**
     * 序号列index
     */
    private int orderNumberCol;
    /**
     * 是否选样列index
     */
    private int isSampleCol;
    /**
     * 选样方式index
     */
    private int sampleMethodCol;
    /**
     * 选样Excel标题行数据
     */
    private Map<String, Object> titleRowMaps = new HashMap<>();
    /**
     * 选样Excel具体数据(不包括标题行数据)
     */
    private List<Map<String, Object>> data = new ArrayList<>();
    /**
     * 选样Excel具体数据(不包括标题行数据)-大额数据
     */
    private List<Map<String, Object>> largeAmountDatas = new ArrayList<>();
    /**
     * 选样Excel具体数据(不包括标题行数据)-非大额数据
     */
    private List<Map<String, Object>> noLargeAmountDatas = new ArrayList<>();

    public int getTotalCol() {
        return totalCol;
    }

    public void setTotalCol(int totalCol) {
        this.totalCol = totalCol;
    }

    public int getAmountCol() {
        return amountCol;
    }

    public void setAmountCol(int amountCol) {
        this.amountCol = amountCol;
    }

    public int getOrderNumberCol() {
        return orderNumberCol;
    }

    public void setOrderNumberCol(int orderNumberCol) {
        this.orderNumberCol = orderNumberCol;
    }

    public int getIsSampleCol() {
        return isSampleCol;
    }

    public void setIsSampleCol(int isSampleCol) {
        this.isSampleCol = isSampleCol;
    }

    public int getSampleMethodCol() {
        return sampleMethodCol;
    }

    public void setSampleMethodCol(int sampleMethodCol) {
        this.sampleMethodCol = sampleMethodCol;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public Map<String, Object> getTitleRowMaps() {
        return titleRowMaps;
    }

    public void setTitleRowMaps(Map<String, Object> titleRowMaps) {
        this.titleRowMaps = titleRowMaps;
    }

    public List<Map<String, Object>> getLargeAmountDatas() {
        return largeAmountDatas;
    }

    public void setLargeAmountDatas(List<Map<String, Object>> largeAmountDatas) {
        this.largeAmountDatas = largeAmountDatas;
    }

    public List<Map<String, Object>> getNoLargeAmountDatas() {
        return noLargeAmountDatas;
    }

    public void setNoLargeAmountDatas(List<Map<String, Object>> noLargeAmountDatas) {
        this.noLargeAmountDatas = noLargeAmountDatas;
    }
}
