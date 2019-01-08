package cn.wolfcode.p2p.base.domain;

import cn.wolfcode.p2p.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class Account extends BaseDomain {
    //版本号
    private Integer version = 0;
    //交易密码
    private String tradePassword;
    //可用金额
    private BigDecimal usableAmount = Constants.ZERO;
    //冻结金额
    private BigDecimal freezedAmount = Constants.ZERO;
    //待收利息
    private BigDecimal unReceiveInterest = Constants.ZERO;
    //待收本金
    private BigDecimal unReceivePrincipal = Constants.ZERO;
    //待还金额
    private BigDecimal unReturnAmount = Constants.ZERO;
    //剩余授信额度
    private BigDecimal remainBorrowLimit = Constants.BORROW_LIMIT;
    //总信用额度
    private BigDecimal borrowLimit = Constants.BORROW_LIMIT;

    //待收本息
    public BigDecimal getUnReturnAmount(){
        return unReceivePrincipal.add(unReceiveInterest);
    }

    public BigDecimal getAllAmount(){
        return usableAmount.add(freezedAmount).add(unReceivePrincipal);
    }
}
