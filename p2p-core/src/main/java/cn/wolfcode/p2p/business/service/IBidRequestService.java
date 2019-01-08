package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.util.PageResult;

import java.util.Date;
import java.util.List;

public interface IBidRequestService {
    /**
     * 借款申请
     */
    void apply(BidRequest bidRequest);

    /**
     * 后台发标审核列表
     * @param qo
     * @return
     */
    PageResult queryForPage(BidRequestQueryObject qo);

    /**
     * 发标前审核
     * @param id
     * @param state
     * @param publishTime
     * @param remark
     */
    void bidrequestAuth(Long id, Integer state, Date publishTime, String remark);

    /**
     * 首页待发布借款
     * @return
     */
    List publishPendngBidRequests();

    /**
     * 更新
     * @param bidRequest
     */
    void update(BidRequest bidRequest);

    /**
     * 查询进行中的借款
     * @return
     */
    List bidRequests();

    /**
     * 获取借款对象
     * @param id
     * @return
     */
    BidRequest getById(Long id);

    /**
     * 满标一审审核
     */
    void audit1(Long id, Integer state, String remark);

    /**
     * 满标2审审核
     */
    void audit2(Long id, Integer state, String remark);

    /**
     * 体验标发布
     */
    void publishExpBidRequest(BidRequest bidRequest);

    /**
     * 查询招标中的体验标
     */
    List expBidRequests();

    /**
     * 满标2审体验标
     */
    void audit2Exp(Long id, Integer state, String remark);
}
