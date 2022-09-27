package com.izhaong.mall.service;

import com.github.pagehelper.PageInfo;
import com.izhaong.mall.model.pojo.Product;
import com.izhaong.mall.model.request.AddProductReq;
import com.izhaong.mall.model.request.ListProductReq;
import com.izhaong.mall.model.vo.CategoryVO;

import java.util.List;

public interface ProductService {
    void add(AddProductReq addProductReq);

    void update(Product product);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    PageInfo listForCustomer(ListProductReq listProductReq);

    void batchUpdateSellStatus(Integer[] ids, Integer status);

    Product detail(Integer id);
}
