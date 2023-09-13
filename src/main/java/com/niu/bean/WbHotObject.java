package com.niu.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WbHotObject {

    private Integer index;
    private String title;
    private String hot;
    private String url;

}
