package com.xaxc.teqin.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 容东社区数据
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Data
@TableName("rd_community_data")
public class RdCommunityData implements Serializable {

    private static final long serialVersionUID = 1L;

//    @TableField("整体/分步拆迁")
//    private String 整体/分步拆迁;

//    @TableField("是否存在住改商")
//    private String 是否存在住改商;
//
//    @TableField("是否为群租房")
//    private String 是否为群租房;
//
//    @TableField("是否为民宿")
//    private String 是否为民宿;
//
//    @TableField("是否为零就业家庭")
//    private String 是否为零就业家庭;
//
//    @TableField("家庭主要收入来源")
//    private String 家庭主要收入来源;

    @TableField("乡镇街道名称")
    @ExcelProperty(index = 6)
    private String 乡镇街道名称;


    @TableField("村社区名称")
    @ExcelProperty(index = 7)
    private String 村社区名称;

    @ExcelProperty(index = 8)
    @TableField("网格名称")
    private String 网格名称;

    @ExcelProperty(index = 9)
    @TableField("楼宇名称")
    private String 楼宇名称;

    @ExcelProperty(index = 10)
    @TableField("单元名称")
    private String 单元名称;

    @ExcelProperty(index = 11)
    @TableField("房屋名称")
    private String 房屋名称;

    @ExcelProperty(index = 12)
    @TableField("房屋用途")
    private String 房屋用途;

    @ExcelProperty(index = 13)
    @TableField("房屋地址")
    private String 房屋地址;

    @ExcelProperty(index = 14)
    @TableField("房主姓名")
    private String 房主姓名;

    @ExcelProperty(index = 15)
    @TableField("房主身份证")
    private String 房主身份证;

    @ExcelProperty(index = 16)
    @TableField("联系方式")
    private String 联系方式;

    @ExcelProperty(index = 17)
    @TableField("房主性别")
    private String 房主性别;

    @ExcelProperty(index = 18)
    @TableField("姓名")
    private String 姓名;

    @ExcelProperty(index = 19)
    @TableField("身份证")
    private String 身份证;

    @ExcelProperty(index = 20)
    @TableField("性别")
    private String 性别;

    @ExcelProperty(index = 21)
    @TableField("年龄")
    private String 年龄;

    @ExcelProperty(index = 22)
    @TableField("民族")
    private String 民族;

    @ExcelProperty(index = 23)
    @TableField("户籍详址")
    private String 户籍详址;

    @ExcelProperty(index = 24)
    @TableField("联系电话")
    private String 联系电话;

    @ExcelProperty(index = 25)
    @TableField("曾用名")
    private String 曾用名;

    @ExcelProperty(index = 26)
    @TableField("籍贯")
    private String 籍贯;

    @ExcelProperty(index = 27)
    @TableField("学历")
    private String 学历;

    @ExcelProperty(index = 28)
    @TableField("宗教信仰")
    private String 宗教信仰;

    @ExcelProperty(index = 29)
    @TableField("政治面貌")
    private String 政治面貌;

    @ExcelProperty(index = 30)
    @TableField("婚姻状况")
    private String 婚姻状况;

    @ExcelProperty(index = 31)
    @TableField("就业状态")
    private String 就业状态;

    @ExcelProperty(index = 32)
    @TableField("就业类型")
    private String 就业类型;

    @ExcelProperty(index = 33)
    @TableField("行业类别")
    private String 行业类别;

    @ExcelProperty(index = 34)
    @TableField("职业")
    private String 职业;

    @ExcelProperty(index = 35)
    @TableField("服务处所")
    private String 服务处所;

    @ExcelProperty(index = 36)
    @TableField("未就业原因")
    private String 未就业原因;

    @ExcelProperty(index = 37)
    @TableField("就业地点")
    private String 就业地点;

    @ExcelProperty(index = 38)
    @TableField("就业意愿")
    private String 就业意愿;

    @ExcelProperty(index = 39)
    @TableField("省外就业")
    private String 省外就业;

    @ExcelProperty(index = 40)
    @TableField("专业特长")
    private String 专业特长;

    @ExcelProperty(index = 41)
    @TableField("职业技能证书")
    private String 职业技能证书;

    @ExcelProperty(index = 42)
    @TableField("特征描述")
    private String 特征描述;

    @ExcelProperty(index = 43)
    @TableField("备注")
    private String 备注;

    @ExcelProperty(index = 44)
    @TableField("现居地")
    private String 现居地;

    @ExcelProperty(index = 45)
    @TableField("居住开始时间")
    private String 居住开始时间;

    @TableField("与房主关系")
    private String 与房主关系;

    @TableField("户内人员关系")
    private String 户内人员关系;

    @TableField("是否有营业执照")
    private String 是否有营业执照;

    @TableField("是否担任股东董事监事")
    private String 是否担任股东董事监事;

    @TableField("是否在容东上学")
    private String 是否在容东上学;

    @TableField("就读学校")
    private String 就读学校;

    @TableField("上学交通方式")
    private String 上学交通方式;

    @TableField("是否是家长接送")
    private String 是否是家长接送;

    @TableField("是否有预约公交需求")
    private String 是否有预约公交需求;

//    @TableField("是否有特殊技能/才艺/传统手艺/文体特长")
//    private String 是否有特殊技能/才艺/传统手艺/文体特长;
//
//    @TableField("获得过先进或荣誉称号（级别）")
//    private String 获得过先进或荣誉称号（级别）;

    @TableField("需求诉求")
    private String 需求诉求;

    @ExcelProperty(index = 58)
    @TableField("人员标签")
    private String 人员标签;


}
