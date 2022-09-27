package com.izhaong.mall.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.izhaong.mall.common.Constant;
import com.izhaong.mall.exception.MallException;
import com.izhaong.mall.exception.MallExceptionEnum;
import com.izhaong.mall.model.dao.ProductMapper;
import com.izhaong.mall.model.pojo.Product;
import com.izhaong.mall.model.query.ListProductQuery;
import com.izhaong.mall.model.request.AddProductReq;
import com.izhaong.mall.model.request.ListProductReq;
import com.izhaong.mall.model.vo.CategoryVO;
import com.izhaong.mall.service.CategoryService;
import com.izhaong.mall.service.ProductService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    @Override
    public void add(AddProductReq addProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        // 重名情况处理
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void update(Product updateProduct) {
        Product productOld = productMapper.selectByName(updateProduct.getName());
        if (productOld != null && !productOld.getId().equals(updateProduct.getId())) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILD);
        }
    }

    @Override
    public void delete(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILD);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectListForAdmin();
        return new PageInfo(list);
    }

    @Override
    public PageInfo listForCustomer(ListProductReq listProductReq) {
        ListProductQuery newListReq = new ListProductQuery();

        // handle keyword
        if (!StringUtils.isNullOrEmpty(listProductReq.getKeyword())) {
            String keyword = new StringBuilder().append("%").append(listProductReq.getKeyword()).append("%").toString();
            newListReq.setKeyword(keyword);
        }


        // handle category
        if (listProductReq.getCategoryId() != null) {
            List<CategoryVO> categoryVOList = categoryService.listForCustomer(listProductReq.getCategoryId());

            ArrayList<Integer> cateIds = new ArrayList<>();
            cateIds.add(listProductReq.getCategoryId());

            getCateIdsByParent(categoryVOList, cateIds);
            newListReq.setCategoryIds(cateIds);
        }
        // handle order
        String orderBy = listProductReq.getOrderBy();
        // set up field enumeration to prevent the font end from spreading parameters
        if (!StringUtils.isNullOrEmpty(orderBy) && Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper.startPage(listProductReq.getPageNum(), listProductReq.getPageSize(), orderBy);
        } else {
            PageHelper.startPage(listProductReq.getPageNum(), listProductReq.getPageSize(), orderBy);
        }

        List<Product> list = productMapper.selectListForCustomer(newListReq);
        return new PageInfo(list);
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer status) {
        int count = productMapper.batchUpdateSellStatus(ids, status);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILD);
        }
    }

    @Override
    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new MallException(MallExceptionEnum.GET_FILE);
        } else {
            return product;
        }

    }


    public void getCateIdsByParent(List<CategoryVO> list, ArrayList cateIds) {
        for (CategoryVO categoryVO : list) {
            if (categoryVO != null) {
                cateIds.add(categoryVO.getId());
                if (!categoryVO.getChildCategory().isEmpty()) {
                    getCateIdsByParent(categoryVO.getChildCategory(), cateIds);
                }
            }
        }
    }

}
