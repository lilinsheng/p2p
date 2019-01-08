package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据字典明细
 */
@Setter
@Getter
public class SystemDictionaryItem extends BaseDomain {
    //数据字典明细对应的分类id
    private Long parentId;
    //数据字典明细显示名称
    private String title;
    //数据字典明细在该分类中的排序
    private Integer sequence = 0;
}
