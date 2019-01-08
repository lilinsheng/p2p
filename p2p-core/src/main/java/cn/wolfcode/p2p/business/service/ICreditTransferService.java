package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.CreditTransfer;
import cn.wolfcode.p2p.business.query.TransferQueryObject;
import cn.wolfcode.p2p.util.PageResult;

public interface ICreditTransferService {
    /**
     * 查询可以转让的赵权
     */
    PageResult queryForCanTransPage(TransferQueryObject qo);

    /**
     * 债权转让
     * @param bidIds ：转让的bid的id
     */
    void creditTransfer(Long[] bidIds);

    /**
     * 查询招标中的债权标
     */
    PageResult queryForPage(TransferQueryObject qo);

    /**
     * 购买债权标
     * @param id ：债权标id
     */
    void subscribe(Long id);

    /**
     * 更新
     */
    void update(CreditTransfer creditTransfer);

    /**
     * 撤消转让中的债券标
     * @param bidRequestId
     */
    void batchUpdateTransStateInTransingByBidRequestId(Long bidRequestId);

    /**
     * 查询某人转让中的债权
     */
    PageResult queryForMyTransPage(TransferQueryObject qo);

    /**
     * 撤消债券标
     */
    void cancelTrans(Long bidId);
}
