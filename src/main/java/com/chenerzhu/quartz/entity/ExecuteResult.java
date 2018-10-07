package com.chenerzhu.quartz.entity;

/**
 * @author chenerzhu
 * @create 2018-10-02 1:12
 **/
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExecuteResult {
    private int code;
    private String msg;

    public ExecuteResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}