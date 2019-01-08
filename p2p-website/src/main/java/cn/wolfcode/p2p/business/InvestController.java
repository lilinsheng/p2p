package cn.wolfcode.p2p.business;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.query.TransferQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import cn.wolfcode.p2p.business.service.ICreditTransferService;
import cn.wolfcode.p2p.util.Constants;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import static cn.wolfcode.p2p.util.Constants.*;

/**
 * 前台投资
 */
@Controller
public class InvestController {

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private ICreditTransferService creditTransferService;

    /**
     * 进入投资主列表
     */
    @RequestMapping("/invest")
    public String invest(){
        return "invest";
    }

    /**
     * 查询投资列表
     */
    @RequestMapping("/invest_list")
    public String investList(BidRequestQueryObject qo, Model model){
        if (qo.getBidRequestType()== BidRequest.BID_TYPE_CREDIT){
            //信用标
            if (qo.getBidRequestState()==-1){
                qo.setBidRequestStates(new int[]{BIDREQUEST_STATE_BIDDING,BIDREQUEST_STATE_COMPLETE_PAY_BACK});
            }
            PageResult pageResult = bidRequestService.queryForPage(qo);
            model.addAttribute("pageResult",pageResult);
            return "invest_list";
        }else {
            //债权标
            TransferQueryObject tqo = new TransferQueryObject();
            tqo.setState(Constants.TRANSFER_STATE_TRANSING);
            tqo.setPageSize(qo.getPageSize());
            tqo.setCurrentPage(qo.getCurrentPage());
            PageResult pageResult = creditTransferService.queryForPage(tqo);
            model.addAttribute("pageResult",pageResult);
            return "credittransfer_list";
        }
    }
}
