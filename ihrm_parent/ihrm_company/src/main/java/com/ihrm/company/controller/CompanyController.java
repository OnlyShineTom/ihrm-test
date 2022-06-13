package com.ihrm.company.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//解决跨域问题
@CrossOrigin
@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    /*
    * 添加企业
    * */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Company company) throws Exception{
        try {
            companyService.add(company);
            return Result.SUCCESS();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
    }

    /*
    *   根据id更新企业信息
    * */

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(name = "id") String id, @RequestBody Company company)throws Exception{
        try {
            Company one = companyService.findById(id);
            one.setName(company.getName());
            one.setId(company.getId());
            one.setState(company.getState());
            companyService.update(one);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
        return Result.SUCCESS();
    }

    /*
    *   根据id删除企业信息
    * */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(name = "id") String id) throws Exception{
        try {
            companyService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
        return Result.SUCCESS();
    }

    /*
        根据id获取公司的信息
    */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(name="id")String id) throws Exception{
        try {
            Company company = companyService.findById(id);
            return new Result(ResultCode.SUCCESS,company);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
    }

    /*
    *   获取企业列表
    * */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Result findAll()throws Exception{
        try {
            List<Company> companyList = companyService.findAll();
            return new Result(ResultCode.SUCCESS,companyList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }


    }




}
