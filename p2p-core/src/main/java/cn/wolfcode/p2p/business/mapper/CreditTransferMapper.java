package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.CreditTransfer;
import cn.wolfcode.p2p.business.query.TransferQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CreditTransferMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CreditTransfer record);

    CreditTransfer selectByPrimaryKey(Long id);

    List<CreditTransfer> selectAll();

    int updateByPrimaryKey(CreditTransfer record);

    int selectForCanTransCount(TransferQueryObject qo);

    List selectForCanTransList(TransferQueryObject qo);

    List<CreditTransfer> selectForTransList(@Param("bidIds") Long[] bidIds, @Param("transFromId") Long transFromId);

    /**
     * 招标中的债权标总数
     */
    int selectForCount(TransferQueryObject qo);
    /**
     * 招标中的债权标
     */
    List selectForList(TransferQueryObject qo);

    void batchUpdateTransStateInTransingByBidRequestId(Long bidRequestId);

    /**
     * 查询某人转让中的债权列表
     */
    int selectForMyTransCount(TransferQueryObject qo);
    /**
     * 查询某人转让中的债权数量
     */
    List selectForMyTransList(TransferQueryObject qo);
}