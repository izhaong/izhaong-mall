package com.izhaong.mall.contraoller;


import com.github.pagehelper.PageInfo;
import com.izhaong.mall.common.ApiRestResponse;
import com.izhaong.mall.common.Constant;
import com.izhaong.mall.exception.MallException;
import com.izhaong.mall.exception.MallExceptionEnum;
import com.izhaong.mall.model.pojo.Product;
import com.izhaong.mall.model.request.AddProductReq;
import com.izhaong.mall.model.request.ListProductReq;
import com.izhaong.mall.model.request.UpdateProductReq;
import com.izhaong.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class ProductAdminController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid + suffixName;
        //创建文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        //如果文件夹不存在
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new MallException(MallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return ApiRestResponse
                    .success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/"
                            + newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(MallExceptionEnum.UPLOAD_FAILED);
        }
    }

    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                    null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    @ApiOperation("后台更新商品")
    @PutMapping("/admin/product/update")
    public ApiRestResponse update(@Valid @RequestBody UpdateProductReq upProduct) {
        System.out.println(upProduct.toString());
        Product product = new Product();
        BeanUtils.copyProperties(upProduct, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台删除商品")
    @DeleteMapping("/admin/product/delete")
    public ApiRestResponse delete(@RequestParam Integer id) {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量上下架商品")
    @PutMapping("/admin/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer status) {
        productService.batchUpdateSellStatus(ids, status);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品列表")
    @GetMapping("/admin/product/list")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("商品详情")
    @GetMapping("/admin/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("前台商品列表")
    @GetMapping("/product/list")
    public ApiRestResponse listForCustomer(ListProductReq listProductReq) {
        PageInfo pageInfo = productService.listForCustomer(listProductReq);
        return ApiRestResponse.success(pageInfo);
    }
}
