package com.ihrm.system.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.domain.system.Permission;
import com.ihrm.system.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping( value = "/sys")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /*
     * 保存部门
     * */
    @RequestMapping(value = "/permission",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        permissionService.save(map);

        return Result.SUCCESS();
        
    }

    /*
     *   修改部门信息
     *
     * */
    @RequestMapping(value = "/permission/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(name = "id")String id, @RequestBody Map<String,Object> map) throws Exception{
        //构造id
        map.put("id",id);
        permissionService.update(map);
        return Result.SUCCESS();
    }

    /*
     *   根据id删除权限
     *      删除权限
     *      删除权限对应的资源
     * */
    @RequestMapping(value = "/permission/{id}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable(value = "id") String id) throws Exception{
        permissionService.deleteById(id);
        return Result.SUCCESS();
    }

    /*
     *   根据id查询
     * */

    @RequestMapping(value = "/permission/{id}" , method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) throws CommonException {
        Map map = permissionService.findById(id);
        return new Result(ResultCode.SUCCESS , map);
    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/permission",method = RequestMethod.GET)
    public Result findAll(int page,int size,@RequestParam Map<String,Object> map){
        List<Permission> list = permissionService.findAll(map);
        return new Result(ResultCode.SUCCESS,list);
    }

}
