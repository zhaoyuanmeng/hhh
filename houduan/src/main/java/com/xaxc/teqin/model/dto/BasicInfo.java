package com.xaxc.teqin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicInfo implements Serializable {
    /**
     * 表头
     */
//    private String title;

    /**
     * 名称
     */
    private String mingcheng;

    /**
     * 管辖派出所
     */
    private String guanxiapaichusuo;

    /**
     * 单位性质
     */
    private String danweixingzhi;

    /**
     * 隶属关系
     */
    private String lishuguanxi;

    /**
     * 法人
     */
    private String faren;

    /**
     * 联系电话
     */
    private String lianxidianhua;

    /**
     * 总人数
     */
    private String zongrenshu;

    /**
     * 地址
     */
    private String dizhi;

    /**
     * 占地面积
     */
    private String zhandimianji;

    /**
     * 经度
     */
    private String jingdu;

    /**
     * 纬度
     */
    private String weidu;

    /**
     * 内设机构
     */
    private List<InternalMechanism> neishejigou;

    /**
     * 主要建筑
     */
    private List<MajorArchitecture> zhuyaojianzhu;

    /**
     * 重要部位
     */
    private List<ImportantPart> zhongyaobuwei;

    /**
     * 四邻情况
     */
    private NeighborhoodInfo silinqingkuang;

    /**
     * 停车场
     */
    private List<CarPark> tingchechang;
}
