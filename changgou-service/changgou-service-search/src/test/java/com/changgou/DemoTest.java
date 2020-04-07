package com.changgou;

import java.util.Calendar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description
 * @Author tangKai
 * @Date 14:21 2020/1/13
 **/
@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class DemoTest {

    @Test
    public void test1() {
        Calendar calendar = Calendar.getInstance();

//        calendar.set(Calendar.YEAR, 2000);
//        calendar.add(Calendar.YEAR,-20);
//        System.out.println("年: " + calendar.get(Calendar.YEAR));
//        System.out.println("月: " + (calendar.get(Calendar.MONTH) + 1));
//        System.out.println("日: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.DATE));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
    }


}
