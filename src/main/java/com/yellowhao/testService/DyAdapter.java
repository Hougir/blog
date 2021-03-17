package com.yellowhao.testService;


/**
 * Dy适配器
 * @author huang hao
 * @version 1.0
 * @date 2021/3/17 10:45
 */
public class DyAdapter implements Target {
    private DyAdaptee dyAdaptee = new DyAdaptee();
    @Override
    public void handel() {
        dyAdaptee.getData();
    }
}
