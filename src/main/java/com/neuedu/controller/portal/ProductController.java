package com.neuedu.controller.portal;

import com.alipay.api.domain.FaceAbilityExtInfo;
import com.alipay.api.domain.Keyword;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.management.modelmbean.RequiredModelMBean;
import java.io.Serializable;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    IProductService productService;
    /*
    *前台
    * 商品详情
    * */
    @RequestMapping(value = "detail.do")
    public ServerResponse detail(Integer productId){
        return productService.detial_portal(productId);
    }
    /**
     * 前台-搜索商品并排序
     *
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(@RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false)     String keyword,
                               @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                               @RequestParam(required = false,defaultValue = "")String orderBy){


        return  productService.list_portal(categoryId,keyword,pageNum,pageSize,orderBy);
    }

}
