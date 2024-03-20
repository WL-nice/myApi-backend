package com.wanglei.MyApi.commmon;

import com.wanglei.MyApi.constant.CommonConstant;
import lombok.Data;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    protected int pageSize = 10;


    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;

    /**
     * 排序字段
     */
    private String sortField;


}
