package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.business.domain.PlatformBankInfo;
import cn.wolfcode.p2p.business.mapper.PlatformBankInfoMapper;
import cn.wolfcode.p2p.business.service.IPlatformBankInfoService;
import cn.wolfcode.p2p.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformBankInfoServiceImpl implements IPlatformBankInfoService {
    @Autowired
    private PlatformBankInfoMapper platformBankInfoMapper;

    @Override
    public PageResult queryForPage(QueryObject qo) {
        int count = platformBankInfoMapper.selectForCount(qo);
        if (count==0){
            return PageResult.empty(qo.getPageSize());
        }

        List listData = platformBankInfoMapper.selectForList(qo);

        return new PageResult(listData,count,qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void saveOrUpdate(PlatformBankInfo platformBankInfo) {
        //更新
        if (platformBankInfo.getId()!=null){
            platformBankInfoMapper.updateByPrimaryKey(platformBankInfo);
        }else {
            //新增
            platformBankInfoMapper.insert(platformBankInfo);
        }
    }

    @Override
    public List<PlatformBankInfo> listAll() {
        return platformBankInfoMapper.selectAll();
    }
}
