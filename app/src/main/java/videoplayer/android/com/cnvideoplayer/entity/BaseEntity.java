package videoplayer.android.com.cnvideoplayer.entity;

import java.io.Serializable;

/**
 * Date: 2018/8/15
 * Author:
 * Des：
 */

public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -8697809972652592165L;
    private int status;
    private String error;

    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return error;
    }

    public void setMsg(String msg) {
        this.error = msg;
    }
}
