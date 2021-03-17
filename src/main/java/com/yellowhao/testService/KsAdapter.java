package com.yellowhao.testService;

/**
 * @author huang hao
 * @version 1.0
 * @date 2021/3/17 10:45
 */
public class KsAdapter implements Target {
    private KsAdaptee ksAdapter = new KsAdaptee();
    @Override
    public void handel() {
        ksAdapter.getData();
    }
}
