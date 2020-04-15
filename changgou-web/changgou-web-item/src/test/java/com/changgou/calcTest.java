package com.changgou;

import java.util.Date;
import java.util.Random;
import org.assertj.core.util.DateUtil;
import org.junit.Test;

/**
 * @Description
 * @Author tangKai
 * @E-mail tangkai@qingzu.com.cn
 * @Date 16:00 2020/4/11
 **/
public class calcTest {

    @Test
    public void t1() {
        int random = new Random().nextInt(4);
        System.out.println(random);
    }


    @Test
    public void big() {
        String s1 = "2020-01-12 12:00:00";
        String s2 = "2020-01-13 12:00:00";

        Date d1 = DateUtil.parse(s1);
        Date d2 = DateUtil.parse(s2);
        int bound = 3;
        if (d1.before(d2)) {
            bound = 2;
        }

        int sum = 1 + bound;
        System.out.println(sum + bound);
    }

}
