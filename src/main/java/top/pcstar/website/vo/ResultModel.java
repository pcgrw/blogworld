package top.pcstar.website.vo;

/**
 * @ClassName ResultModel
 * @Description: 返回结果模型
 * @Author: panchao
 * @Date: Created in 19-1-12 下午6:08
 * @Version: 1.0
 */
public class ResultModel<T> {
    /**
     * 结果码
     */
    private int code;
    /**
     * 结果信息
     */
    private String message;
    /**
     * 结果数据
     */
    private T data;

    public ResultModel() {
    }

    public ResultModel(int code, String message) {
        this(code, message, null);
    }

    public ResultModel(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
