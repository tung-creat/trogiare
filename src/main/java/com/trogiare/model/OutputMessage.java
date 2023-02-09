package com.trogiare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutputMessage {
    private String from;
    private String text;
    private Date time;

    // constructor and getters ...
}