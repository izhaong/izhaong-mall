package com.izhaong.mall.model.dao;

import com.izhaong.mall.model.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    Product selectByName(String name);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);


}