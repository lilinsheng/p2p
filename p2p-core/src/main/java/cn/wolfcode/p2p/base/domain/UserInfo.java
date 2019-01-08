package cn.wolfcode.p2p.base.domain;

import cn.wolfcode.p2p.util.BitStatesUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户基本信息
 */
@Setter
@Getter
public class UserInfo extends BaseDomain {
    //版本号，用作乐观锁
    private Integer version = 0;
    //用户位状态值
    private Long bitState = 0L;
    //用户实名值
    private String realName;
    //用户身份证号
    private String idNumber;
    //实名对象id
    private Long realAuthId;
    //视频认证对象id
    private Long videoAuthId;
    //用户电话
    private String phoneNumber;
    //邮箱
    private String email;
    //收入
    private SystemDictionaryItem incomeGrade;
    //婚姻情况
    private SystemDictionaryItem marriage;
    //子女情况
    private SystemDictionaryItem kidCount;
    //学历
    private SystemDictionaryItem educationBackground;
    //住房条件
    private SystemDictionaryItem houseCondition;

    //用户是否有一个借款在申请流程当中
    public boolean hasBidrequestInProcess(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
    }

    //是否完成基本信息认证
    public boolean hasBasicInfo(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.OP_BASIC_INFO);
    }

    //是否认证身份证
    public boolean hasRealAuth(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.OP_REAL_AUTH);
    }

    //视频认证
    public boolean hasVedioAuth(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.OP_VEDIO_AUTH);
    }

    //完成基本信息，身份，视频认证
    public boolean hasAllAuth(){
        return hasBasicInfo()&&hasRealAuth()&&hasVedioAuth();
    }

    //是否领取体验金
    public boolean hasGetExpGold(){
        return BitStatesUtils.hasState(bitState,BitStatesUtils.HAS_GET_EXPGOLD);
    }

    //添加位状态
    public void addBitState(long value){
        bitState = BitStatesUtils.addState(bitState,value);
    }

    //移除位状态
    public void removeBitState(long value){
        bitState = BitStatesUtils.removeState(bitState,value);
    }
}
