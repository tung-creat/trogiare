package com.trogiare.payload;

import lombok.Data;

import java.util.List;

@Data
public class PostPayloadDelete {
    List<String> postIds;
}
