package com.qf.ithub.mapper;

import com.qf.ithub.common.dto.ChartAllDTO;
import com.qf.ithub.entity.Share;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ShareMapper extends Mapper<Share> {

    /**
     * 获得用户购买过的资源
     */
    public  List<Share> getExchangeShares(@Param("userid") Integer userid) ;

    /**
     *  获得首页的全局报表的数据
     */
    public List<ChartAllDTO> getChartAllDTO() ;

}