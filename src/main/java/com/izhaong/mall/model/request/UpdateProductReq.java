package com.izhaong.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class UpdateProductReq {
    @NotNull(message = "id不能为空")
    private Integer id;

    private String name;

    private String image;

    private String detail;

    private Integer categoryId;

    @Min(value = 1, message = "价格不能小于1")
    private Integer price;

    @Max(value = 10000, message = "库存不能大于10000")
    private Integer stock;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
