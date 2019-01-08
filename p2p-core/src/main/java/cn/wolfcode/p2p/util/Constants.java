package cn.wolfcode.p2p.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Constants {

    /**
     * 显示精度
     */
    public static final int DISPLAY_SCALE = 2;

    /**
     * 存储精度
     */
    public static final int SCALE_STORE = 4;

    /**
     * 计算精度
     */
    public static final int SCALE_CAL = 8;

    /**
     * 昵称长度
     */
    public static final int LENGTH_MIN_NICKNAME = 2;
    public static final int LENGTH_MAX_NICKNAME = 6;
    /**
     * 密码长度
     */
    public static final int LENGTH_MIN_PASSWORD = 4;
    public static final int LENGTH_MAX_PASSWORD = 16;
    /**
     * 验证码长度
     */
    public static final int LENGTH_VERIFYCODE = 4;



    /**
     * 手机号长度
     */
    public static final int LENGTH_PHONE = 11;

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^[1][3,4,5,7,8][0-9]{9}$";

    /**
     * 零
     */
    public static final BigDecimal ZERO = new BigDecimal("0.0000");

    /**
     * 初始信用额度
     */
    public static final BigDecimal BORROW_LIMIT = new BigDecimal("5000.0000");

    // ---------------------借款状态---------------------------
    public final static int BIDREQUEST_STATE_APPLY = 11;  // 发标待审
    public final static int BIDREQUEST_STATE_PUBLISH_PENDING = 0;   // 待发布
    public final static int BIDREQUEST_STATE_BIDDING = 1;           // 招标中
    public final static int BIDREQUEST_STATE_UNDO = 2;              // 已撤销
    public final static int BIDREQUEST_STATE_BIDDING_OVERDUE = 3;   // 流标
    public final static int BIDREQUEST_STATE_APPROVE_PENDING_1 = 4; // 满标1审
    public final static int BIDREQUEST_STATE_APPROVE_PENDING_2 = 5; // 满标2审
    public final static int BIDREQUEST_STATE_REJECTED = 6;          // 满标审核被拒绝
    public final static int BIDREQUEST_STATE_PAYING_BACK = 7;       // 还款中
    public final static int BIDREQUEST_STATE_COMPLETE_PAY_BACK = 8; // 已还清
    public final static int BIDREQUEST_STATE_PAY_BACK_OVERDUE = 9;  // 逾期
    public final static int BIDREQUEST_STATE_PUBLISH_REFUSE = 10;   // 发标审核拒绝状态

    //-----------------------债权转让状态------------------------
    public final static int TRANSFER_STATE_TRANSING = 0;           // 转让中
    public final static int TRANSFER_STATE_UNDO = 1;              // 已撤销
    public final static int TRANSFER_STATE_TRANSFERRED = 2; // 已转让

    public static final BigDecimal SMALLEST_BID_AMOUNT = new BigDecimal("50.0000");// 系统规定的最小投标金额
    public static final BigDecimal SMALLEST_BIDREQUEST_AMOUNT = new BigDecimal("500.0000");// 系统规定的最小借款金额
    public static final BigDecimal SMALLEST_CURRENT_RATE = new BigDecimal("5.0000");// 系统最小借款利息
    public static final BigDecimal MAX_CURRENT_RATE = new BigDecimal("20.0000");// 系统最大借款利息

    /**
     * 借款期限
     */
    public static final List<String> BORROW_RETURN_MONTHS = Arrays.asList("1","3","6","9","12");
    /**
     * 招标天数
     */
    public static final List<String> BID_DISABLE_DAYS = Arrays.asList("1","2","3","4","5");

    // --------------------还款类型---------------------------

    // 按月分期还款(等额本息)
    public final static int RETURN_TYPE_MONTH_INTEREST_PRINCIPAL = 0;

    // 按月到期还款(每月还利息,到期还本息)
    public final static int RETURN_TYPE_MONTH_INTEREST = 1;

    /** =========还款状态=============== */

    // 正常待还
    public final static int PAYMENT_STATE_NORMAL = 0;

    // 已还
    public final static int PAYMENT_STATE_DONE = 1;

    // 逾期
    public final static int PAYMENT_STATE_OVERDUE = 2;

    //--------------------体验金----------------
    public final static BigDecimal EXPGOLD_DEFAULT = new BigDecimal("3000.0000");

    //---------------------体验金流水---------------
    //领取体验金
    public final static int EXPGOLD_FLOW_GETEXPGOLD = 1;
    //投资体验标成功：体验金余额减少
    public final static int EXPGOLD_FLOW_REDUCE_AMOUNT = 2;
    //投资体验标成功：体验金冻结金额增加
    public final static int EXPGOLD_FLOW_ADD_FREE = 3;
    //满标1审拒绝：退款，体验金余额增加
    public final static int EXPGOLD_FLOW_ADD_AMOUNT = 4;
    //满标1审拒绝：退款，冻结金额减少
    public final static int EXPGOLD_FLOW_REDUCE_FREE = 5;
    //满标2审拒绝：冻结金额减少
    public final static int EXPGOLD_FLOW_BIDSUCCESS_AMOUNT = 6;
    //体验金还款：可用金额减少
    public final static int EXPGOLD_FLOW_RETURN_PRINCIPAL = 7;
    //归还系统体验金：可用金额减少
    public final static int EXPGOLD_FLOW_RETURN_SYSTEM = 8;


}
