package com.h3c.bigdata.zhgx.function.report.model;

import com.h3c.bigdata.zhgx.function.system.entity.AuthDepartmentInfoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TemplateBean
 * @Description TODO
 * @Author zzzzitai
 * @Date 2018/11/16 10:17
 * @Version 1.0
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "模板添加")
public class TemplateAddBean {

    @ApiModelProperty(value = "模板id")
    private Integer id;

    @ApiModelProperty(value = "模板编号")
    private String templateNum;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "模板填写说明")
    private String description;

    @ApiModelProperty(value = "数据项列表")
    List<TemplateItemBean> itemBeans;

    @ApiModelProperty(value = "部门列表")
    List<AuthDepartmentInfoEntity> departmentId;

    @ApiModelProperty(value = "模板标签")
    private String tempTag;
    @ApiModelProperty(value = "模板标签id")
    private String tempTagId;
    @ApiModelProperty(value = "填报周期")
    private String fillInPeriod;
    @ApiModelProperty(value = "填报周期key")
    private String fillInPeriodKey;
    @ApiModelProperty("模板源名称")
    private String templateSourceName;
    @ApiModelProperty("模板源英文名称")
    private String templateSourceNameEn;
    /**
     * 0：正在使用，1代表未使用
     */
    @ApiModelProperty("模板是否使用")
    private String isUsed;

    List<TemplateAddBean> tmpAddBeans;
    /**
     *更新人
     */
    private String updateUser;
    /**
     *更新时间
     */
    private Date updateTime;

    /**
     * 模板id列表
     */
    private List<Integer> templateIds;

    /**
     * 模板是否及时更新 : 0：未及时更新，1已更新
     */
    private Integer isCompleted;
}
