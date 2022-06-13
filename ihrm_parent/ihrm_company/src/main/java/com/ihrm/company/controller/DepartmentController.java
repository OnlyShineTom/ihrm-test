package com.ihrm.company.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.rmi.registry.Registry;
import java.util.List;

/*
*   1.解决跨域
*   2.声明restController
*   3.设置父路径
* */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/company")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;
    /*
    * 保存或者也叫添加部门
    * */
    @RequestMapping(value = "/department",method = RequestMethod.POST)
    public Result save(@RequestBody Department department){
        try {
            //1.设置保存的企业id
            department.setCompanyId(parseCompanyId());
            //2.调用service完成保存企业
            departmentService.save(department);
            //3.构造返回结果
            return Result.SUCCESS();
        }catch (Exception e){
            e.printStackTrace();
            return new Result(ResultCode.FAIL);
        }
    }

    /*
    *   修改部门信息
    *
    * */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(name = "id")String id,@RequestBody Department department){
        try {
            log.info("department模块传入的id是{},传入的部门信息是{}",id,department);
            department.setPid(id);
            departmentService.update(department);
            return Result.SUCCESS();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL();
        }
    }

    /*
    *   删除部门
    * */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(name = "id") String id){
        try {
            departmentService.delete(id);
            return Result.SUCCESS();
        }catch (Exception e){
            e.printStackTrace();
            return Result.ERROR();
        }

    }

    /*
    *   根据id查询
    * */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(name = "id")String id){
        try {
            log.info("查询的企业id是:{}",id);
            Department department = departmentService.findById(id);
            return new Result(ResultCode.SUCCESS,department);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
    }

    /*
    *     查询企业的部门列表
    *       指定企业id
    *   ①:   一个企业是有多个部门的，这里先是提供了根据企业的id来查询指定的企业
    *   ②:   根据企业id，查询该企业的所有部门
    * */
    @RequestMapping(value = "/department",method = RequestMethod.GET)
    public Result findAll(){
        try {
            //指定企业id  ①
            Company companyServiceById = companyService.findById(parseCompanyId());
            //完成查询  ②
            List<Department> list = departmentService.findAll(parseCompanyId());
            //构造返回结果
            return new Result(ResultCode.SUCCESS,new DeptListResult(companyServiceById,list));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
    }
}
