package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据字典分类
 */
@Setter
@Getter
public class SystemDictionary extends BaseDomain {
    //数据字典分类编码
    private String sn;
    //数据字典分类名称
    private String title;
}
