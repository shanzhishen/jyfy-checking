package cn.com.jyfy.checking.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateInMonthDO {

    private LocalDateTime start;
    private LocalDateTime end;


}
