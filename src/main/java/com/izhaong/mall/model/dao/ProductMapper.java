package com.izhaong.mall.model.dao;

import com.izhaong.mall.model.pojo.Product;
import com.izhaong.mall.model.query.ListProductQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    Product selectByName(String name);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    int batchUpdateSellStatus(@Param("ids") Integer[] ids, @Param("status") Integer status);

    List<Product> selectListForAdmin();

    List<Product> selectListForCustomer(@Param("query") ListProductQuery query);
}