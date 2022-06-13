package com.ihrm.common.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//通过继承的关系，来执行进入控制器之前执行的方法
public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;

    //在进入控制器之前执行的方法
    @ModelAttribute
    public void setReqAndResp(HttpServletResponse response,HttpServletRequest request){
        this.request=request;
        this.response=response;
        //企业id暂时使用1，以后会解决
        this.companyId="1";
        this.companyName="中教控股集团人力资源管理部门";
    }

    //企业id (暂时使用1，以后会动态获取)
    public String parseCompanyId(){
        return "1";
    }

    public String parseCompanyName(){
        return "中教控股集团人力资源管理部门";
    }
}
