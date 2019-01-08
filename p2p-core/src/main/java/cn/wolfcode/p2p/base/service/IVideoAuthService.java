package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import cn.wolfcode.p2p.util.PageResult;

public interface IVideoAuthService {
    /**
     * 预约客服
     * @param videoAuth
     */
    void apply(VideoAuth videoAuth);

    /**
     * 获取视频认证对象
     */
    VideoAuth getById(Long id);

    /**
     * 分页查询视频预约客户
     */
    PageResult queryForPage(VideoAuthQueryObject qo);

    /**
     * 视频审核结果
     * @param id 视频认证对象id
     * @param state 认证结果
     * @param remark 备注
     */
    void videoAuth(Long id, Integer state, String remark);
}
