package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.PageRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.*;


@Service
public class UserService extends BaseService<User> {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    /**
     *保存用户
     */
    public void save(User user){
        String id = idWorker.nextId()+"";
        user.setPassword("123456");
        //0是禁用 1是启用
        user.setEnableState(1);
        user.setId(id);
        userDao.save(user);
    }

    /**
     * 更新用户
     */
    public void update(User user){
        //根据id 查询部门
        User target = userDao.findById(user.getId()).get();
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        userDao.save(target);
    }

    /**
     *根据id查询用户
     */
    public User findById(String id){
        User user = userDao.findById(id).get();
        return user;
    }

    /**
     * 查询全部用户列表
     */
    public Page findAll(Map<String,Object> map, int page, int size){
        //需要拼接查询条件
        Specification<User>  spec = new Specification<User>() {
            // List<Predicate> list = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if(!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if(!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                if(!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据请求的hasDept判断  是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if("0".equals((String) map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        //2.分页
        Page<User> pageUser = userDao.findAll(spec, PageRequest.of(page -1, size));
        return pageUser;
    }

    //删除用户
    public void deleteById(String id){
        userDao.deleteById(id);
    }

    //分配角色
    public void assignRoles(String userId,List<String> roleIds){
        //1.根据id 查询用户
        User user = userDao.findById(userId).get();
        //2.设置用户的角色集合
        Set<Role> roleSet =new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roleSet.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roleSet);
        //3.更新用户集合
        userDao.save(user);
    }

    /*
    *   根据mobile查询用户
    * */
    public User findByMobile(String mobile){
        return userDao.findByMobile(mobile);
    }
}
