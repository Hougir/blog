package com.yellowhao.testService;

import org.springframework.stereotype.Service;

/**
 * 被适配的类
 * @author huang hao
 * @version 1.0
 * @date 2021/3/17 10:43
 */
@Service
public class DyAdaptee {
    public void getData() {
        System.out.println("抖音被适配者的方法");
    }
}
