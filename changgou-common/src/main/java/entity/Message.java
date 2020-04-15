package entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/*****
 * @Description: entity:MQ消息封装
 ****/
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC,staticName = "createMsg")
public class Message implements Serializable{
    /**
     * 执行的操作  1：增加，2：修改,3：删除
     */
    @NonNull
    private int code;
    /**
     * 数据
     */
    @NonNull
    private Object content;
    /**
     * 发送的routkey
     */
    @JSONField(serialize = false)
    private String routekey;
    /**
     * 交换机
     */
    @JSONField(serialize = false)
    private String exechange;

}
