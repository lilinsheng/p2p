package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.util.PageResult;

import java.util.List;

public interface IPlatformBankInfoService {
    /**
     * 分页查询平台银行信息
     */
    PageResult queryForPage(QueryObject qo);

    /**
     * 保存或更新平台银行信息
     */
    void saveOrUpdate(PlatformBankInfo platformBankInfo);

    /**
     * 查询平台所有银行账户
     * @return
     */
    List<PlatformBankInfo> listAll();

}
