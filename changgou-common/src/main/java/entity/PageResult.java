package entity;

import java.util.List;
import lombok.Data;

/**
 * 分页结果类
 */
@Data
public class PageResult<T> {
    private Long total;//总记录数
    private List<T> rows;//记录

}
