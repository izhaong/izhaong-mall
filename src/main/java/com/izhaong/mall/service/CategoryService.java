package com.izhaong.mall.service;

import com.github.pagehelper.PageInfo;
import com.izhaong.mall.model.pojo.Category;
import com.izhaong.mall.model.request.AddCategoryReq;
import com.izhaong.mall.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category category);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listForCustomer(Integer parentId);
}
