package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.VideoAuth;
import cn.wolfcode.p2p.base.query.VideoAuthQueryObject;
import java.util.List;

public interface VideoAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VideoAuth record);

    VideoAuth selectByPrimaryKey(Long id);

    List<VideoAuth> selectAll();

    int updateByPrimaryKey(VideoAuth record);

    int selectForCount(VideoAuthQueryObject qo);

    List selectForList(VideoAuthQueryObject qo);
}