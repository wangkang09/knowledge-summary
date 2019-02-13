package enumTest;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 10:04 2019/1/16
 * @Modified By:
 */
public enum TT {
    UPDATE("0","直接修改"), ACTIVITY_START("1","活动开始"), ACTIVITY_END("2", "活动结束"), ACTIVITY_CLOSE("3", "活动关闭");

    private String code;

    private String msg;

    private TT(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TT{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

class TTE {
    TT tt;
    String a;
}