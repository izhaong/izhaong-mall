package com.izhaong.mall.contraoller;


import com.github.pagehelper.PageInfo;
import com.izhaong.mall.common.ApiRestResponse;
import com.izhaong.mall.common.Constant;
import com.izhaong.mall.exception.MallExceptionEnum;
import com.izhaong.mall.model.pojo.Category;
import com.izhaong.mall.model.pojo.User;
import com.izhaong.mall.model.request.AddCategoryReq;
import com.izhaong.mall.model.request.UpdateCategoryReq;
import com.izhaong.mall.model.vo.CategoryVO;
import com.izhaong.mall.service.CategoryService;
import com.izhaong.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    // 后台添加目录
    @ApiOperation("后台添加目录")
    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq) {
        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        //校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，执行操作
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("后台更新目录")
    @PutMapping("/admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(HttpSession session, @Valid @RequestBody UpdateCategoryReq updateCategoryReq) {
        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        //校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，执行操作
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq, category);
            categoryService.update(category);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }


    @ApiOperation("后台删除目录")
    @DeleteMapping("/admin/category/delete")
    @ResponseBody
    public ApiRestResponse updateCategory(@RequestParam Integer id) {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台目录列表")
    @GetMapping("/admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategory(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageinfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageinfo);
    }


    @ApiOperation("前台目录列表")
    @GetMapping("/category/list")
    @ResponseBody
    public ApiRestResponse listCategory() {
        List<CategoryVO> list = categoryService.listForCustomer(0);
        return ApiRestResponse.success(list);
    }
}
