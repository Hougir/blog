package com.yellowhao.testService;

/**
 *
 * 适配器模式
 * @author huang hao
 * @version 1.0
 * @date 2021/3/17 10:47
 */
public class OrderService {
    public static void main(String[] args) {
        Target dyAdapter = new DyAdapter();
        dyAdapter.handel();
        Target ksAdapter = new KsAdapter();
        ksAdapter.handel();


    }
}
