package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.system.User;
import com.ihrm.domain.company.response.DeptListResult;
import com.ihrm.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    /*
    *   分配角色
    * */
    @RequestMapping(value = "/user/assignRoles",method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String,Object> map){
        //1.获取被分配的用户id
        String userId=(String)map.get("id");

        //2.获取到角色的id列表
        List<String> roleIds=(List<String>)map.get("roleIds");

        //3.调用service完成角色分配
        userService.assignRoles(userId,roleIds);
        return new Result(ResultCode.SUCCESS);
    }



    /*
     * 添加部门
     * */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public Result save(@RequestBody User user){
        try {
            user.setCompanyId(parseCompanyId());
            user.setCompanyName(parseCompanyName());
            userService.save(user);
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
    @RequestMapping(value = "/user/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(name = "id")String id, @RequestBody User user){
        try {
            user.setCompanyId(parseCompanyId());
            user.setId(id);
            userService.update(user);
            return Result.SUCCESS();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAIL();
        }
    }

    /*
     *   删除部门
     * */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id){
        try {
            userService.deleteById(id);
            return Result.SUCCESS();
        }catch (Exception e){
            e.printStackTrace();
            return Result.ERROR();
        }

    }

    /*
     *   根据id查询
     * */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id")String id){
        try {
            User user = userService.findById(id);
            return new Result(ResultCode.SUCCESS,user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public Result findAll(int page,int size, @RequestParam Map map){
        try {
            log.info("前端传回的page为{},传回的size为{}",page,size);
            Set set = map.keySet();
            for (Object o : set) {
                System.out.println(o);
                System.out.println(map.get(o));
            }
            //获取当前企业的id
            map.put("companyId",companyId);
            //完成查询
            Page<User> pageUser = userService.findAll(map,page,size);
            //构造返回结果
            PageResult<User> pageResult = new PageResult<>(pageUser.getTotalElements(), pageUser.getContent());
            return new Result(ResultCode.SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR();
        }
    }

    /*
    *   用户登录
    *       1.通过service根据mobile查询用户
    *       2.比较password
    *       3.生成jwt信息
    * */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> loginMap){
        String mobile=loginMap.get("mobile");
        String password=loginMap.get("password");
        User user = userService.findByMobile(mobile);
        if(user==null || !user.getPassword().equals(password)){
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }else {
            //登录成功
            Map<String,Object> map = new HashMap<>();
            map.put("companyId",user.getCompanyId());
            map.put("companyName",user.getCompanyName());
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return new Result(ResultCode.SUCCESS,token);
        }
    }


}
