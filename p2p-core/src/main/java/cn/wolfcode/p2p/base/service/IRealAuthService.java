package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.util.PageResult;

public interface IRealAuthService {
    /**
     * 实名申请
     */
    void apply(RealAuth realAuth);

    /**
     * 后台查询实名审核列表
     */
    PageResult queryForPage(RealAuthQueryObject qo);

    /**
     * 审核身份信息
     * @param realAuthId 身份认证对象
     * @param state 状态：通过1，拒绝2
     * @param remark 备注
     */
    void realAuthAudit(Long realAuthId, Integer state, String remark);

    /**
     * 更新realAuth对象
     * @param realAuth
     */
    void update(RealAuth realAuth);

    /**
     * 获取实名对象
     */
    RealAuth getById(Long realAuthId);
}
