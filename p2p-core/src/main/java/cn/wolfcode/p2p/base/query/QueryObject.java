package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

/**
 * 高级查询相关
 */
@Setter
@Getter
public class QueryObject {
    private int currentPage = 1;
    private int pageSize = 10;

    public int getStart(){
        return (currentPage-1)*pageSize;
    }
}
