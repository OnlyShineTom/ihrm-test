package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.RoleAndUserRelations;
import com.ihrm.system.service.RoleService;
import com.ihrm.system.service.UserAndRoleRelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserAndRoleRelationsService userAndRoleRelationsService;

    //分配权限
    @RequestMapping(value = "/role/assignPrem/",method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String,Object> map){
        //1.获取被分配的角色id
        String roleId = (String) map.get("id");
        //2.获取到权限的id列表
        List<String> permIds = (List<String>) map.get("permIds");
        //3.调用service完成权限的分配
        roleService.assignPerms(roleId,permIds);
        return new Result(ResultCode.SUCCESS);
    }

    //列表显示
    @RequestMapping(value="/role/list" ,method=RequestMethod.GET)
    public Result findAll() throws Exception {
        List<Role> roleList = roleService.findAll(companyId);
        return new Result(ResultCode.SUCCESS,roleList);
    }

    /**
     * 根据用户id获取全部权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/role/userId/{id}", method = RequestMethod.GET)
    public Result findRolesByUserId(@PathVariable(name = "id") String id) {
        List<RoleAndUserRelations> roleByUserId = userAndRoleRelationsService.findRoleByUserId(id);
        if (!ObjectUtils.isEmpty(roleByUserId)){
            List<Role> roles = userAndRoleRelationsService.getRoleDetailByRoleId(roleByUserId);
            if (!ObjectUtils.isEmpty(roles)){
                return new Result(ResultCode.SUCCESS , roles);
            }
        }
        return new Result(ResultCode.SUCCESS);
    }


    /*
     * 添加角色  校验1
     * */
    @RequestMapping(value = "/role",method = RequestMethod.POST)
    public Result add(@RequestBody Role role){
        try {
            role.setCompanyId(parseCompanyId());
            roleService.save(role);
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
    @RequestMapping(value = "/role/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id, @RequestBody Role role){
        try {
            role.setCompanyId(parseCompanyId());
            role.setId(id);
            roleService.update(role);
            return Result.SUCCESS();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL();
        }
    }

    /*
     *   删除部门
     * */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(name = "id") String id){
        try {
            roleService.deleteById(id);
            return Result.SUCCESS();
        }catch (Exception e){
            e.printStackTrace();
            return Result.ERROR();
        }

    }

    /*
     *   根据id查询
     * */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(name = "id")String id){
        try {
            System.out.println("根据id查询"+id);
            Role role = roleService.findById(id);
            return new Result(ResultCode.SUCCESS,role);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/role",method = RequestMethod.GET)
    public Result findByPage(int page,int pagesize, Role role){
        try {
            //获取当前企业的id
            Page<Role> pageRole = roleService.findByPage(companyId,page,pagesize);
            //构造返回结果
            PageResult<Role> pageResult = new PageResult<>(pageRole.getTotalElements(), pageRole.getContent());
            return new Result(ResultCode.SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
    }
}
