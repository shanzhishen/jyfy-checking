package cn.com.jyfy.checking.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class JsonObject<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int state;
    private String msg;
    private T data;

    public static final int SUCCESS = 1;
    public static final int FAILED = 0;
    public static final int UNLOGIN = -1;

    public JsonObject(int state, String msg, T data) {
        this.state = state;
        this.msg = msg;
        this.data = data;
    }

    public JsonObject() {
        this.state = SUCCESS;
    }

    public JsonObject(int state) {
        this.state = state;
    }

    public JsonObject(String msg) {
        this.state = FAILED;
        this.msg = msg;
    }

    public JsonObject(T data) {
        this.state = SUCCESS;
        this.data = data;
    }

    public JsonObject(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public JsonObject(int state, T data) {
        this.state = state;
        this.data = data;
    }

    public JsonObject(Exception e) {
        this.state = JsonObject.FAILED;
        this.msg = e.getMessage();
    }

}
