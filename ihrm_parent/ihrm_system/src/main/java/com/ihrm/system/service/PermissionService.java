package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionMenuDao permissionMenuDao;
    @Autowired
    private PermissionPointDao permissionPointDao;
    @Autowired
    private PermissionApiDao permissionApiDao;


    /**
     *保存用户per
     *
     */
    public void save(Map<String,Object> map) throws Exception {
        //设置主键
        String id = idWorker.nextId() + "";
        //通过map构造权限对象
        Permission perm = BeanMapUtils.mapToBean(map , Permission.class);
        perm.setId(id);
        //根据类型构造不同的资源对象(菜单,按钮,api)
        int type = perm.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(id);
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(id);
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(id);
                permissionApiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }

        //保存权限
        permissionDao.save(perm);
    }

    /**
     * 更新用户
     */
    public void update(Map<String,Object> map) throws Exception {
        //1.通过传递的权限id查询权限
        Permission permission = BeanMapUtils.mapToBean(map,Permission.class);
        Permission permission1 = permissionDao.findById(permission.getId()).get();
        permission1.setName(permission.getName());
        permission1.setCode(permission.getCode());
        permission1.setDescription(permission.getDescription());
        permission1.setEnVisible(permission.getEnVisible());
        //2.根据类型构造不同的资源
        int type = permission.getType();
        switch (type){
            case PermissionConstants .PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(permission.getId());
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(permission.getId());
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(permission.getId());
                permissionApiDao.save(permissionApi);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        //3.保存
        permissionDao.save(permission1);
    }

    /**
     *根据id查询用户
        //1.查询权限
        //2.根据权限的类型查询资源
        //3.构造map集合

     */
    public Map<String,Object> findById(String id) throws CommonException {
        //查询权限
        Permission perm = permissionDao.findById(id).get();
        //根据权限的类型查询资源
        int type = perm.getType();
        //构造map集合
        Object object = null;
        if (type == PermissionConstants.PY_MENU){
            object = permissionMenuDao.findById(id).get();
        }else if (type == PermissionConstants.PY_POINT){
            object = permissionPointDao.findById(id).get();
        }else if (type == PermissionConstants.PY_API){
            object = permissionApiDao.findById(id).get();
        }else{
            throw new CommonException(ResultCode.FAIL);
        }

        Map<String, Object> map = BeanMapUtils.beanToMap(object);
        map.put("name" , perm.getName());
        map.put("type" , perm.getType());
        map.put("code" , perm.getCode());
        map.put("description" , perm.getDescription());
        map.put("pid" , perm.getPid());
        map.put("enVisible" , perm.getEnVisible());

        return map;
    }

    /**
     * 查询全部用户列表
     * 查询全部权限列表type：0：菜单 + 按钮（权限点） 1：菜单2：按钮（权限点）3：API接口
     * enVisible:   是否查询全部权限 0：查询全部(所有Saas平台的权限)，1：只查询企业所属权限
     */
    public List<Permission> findAll(Map<String,Object> map){
        //1.需要查询的条件
        Specification<Permission> specification=new Specification<Permission>() {
            /*
            *   动态拼接查询条件
            * */
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                //1.根据父id查询
                if(!StringUtils.isEmpty((String) map.get("pid"))){
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //2.根据enVisible查询
                if(!StringUtils.isEmpty((String)map.get("enVisible"))){
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                //3.根据type查询
                if(!StringUtils.isEmpty((String) map.get("type"))){
                    String type = (String)map.get("type");
                    //in里面可以传递多个值进行比较
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if("0".equals(type)){
                        in.value(1).value(2);
                    }else{
                        in.value(Integer.parseInt(type));
                    }
                list.add(in);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return permissionDao.findAll(specification);
    }

    //删除用户
    public void deleteById(String id)throws Exception{
        //1.通过传递的id查询权限
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        //2.根据类型删除不同的资源
        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }
}
