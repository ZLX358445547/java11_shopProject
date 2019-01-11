package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/manage/product")
public class UploadController {

    @Autowired
    IProductService productService;


    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String uploadHtml(){
        return "upload";//逻辑视图  前缀加逻辑视图加后缀 --》
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse uploadHtml2(@RequestParam(value = "upload_file",required = false)MultipartFile file){
        String path ="C:\\ftpfile";

        return productService.upload(file,path);
    }
}