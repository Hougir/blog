package com.yellowhao.testService;

import org.springframework.stereotype.Service;

/**
 * @author huang hao
 * @version 1.0
 * @date 2021/3/17 10:43
 */
@Service
public class KsAdaptee {
    public void getData() {
        System.out.println("快手被适配者的方法");
    }
}
