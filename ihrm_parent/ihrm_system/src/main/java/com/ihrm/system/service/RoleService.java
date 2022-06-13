package com.ihrm.system.service;


import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService extends BaseService<Role> {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;


    //分配权限
    public void assignPerms(String roleId, List<String> permIds){
        //1.获取分配的角色对象
        Role role = roleDao.findById(roleId).get();
        //2.构造角色的权限集合
        Set<Permission> permissionSet = new HashSet<>();
        for (String permId : permIds) {
            Permission permission = permissionDao.findById(permId).get();
            //需要根据父id和类型查询api权限列表
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PY_API, permission.getPid());
            permissionSet.addAll(apiList);//赋予API权限
            permissionSet.add(permission);//当前菜单获按钮的权限
        }
        //3.设置角色和权限的关系
        role.setPermissions(permissionSet);
        //4.更新角色
        roleDao.save(role);

    }


    /*
    * 添加角色
    * */
    public void save(Role role){
        role.setId(idWorker.nextId()+"");
        roleDao.save(role);
    }

    /*
    * 更新角色
    * */
    public void update(Role role){
        Role target = roleDao.getOne(role.getId());
        target.setDescription(role.getDescription());
        target.setName(role.getName());
        roleDao.save(target);
    }

    /*
    *   根据id查询角色
    * */
    public Role findById(String id){
        return roleDao.findById(id).get();
    }
    /**
     * 查询所有角色:
     *      根据内部维护的公司id进行查询该公司的所有角色
     */
    public List<Role> findAll(String companyId){
        return roleDao.findAll(getSpecification(companyId));
    }

    /*
    * 根据id删除角色
    * */
    public void deleteById(String id){
        roleDao.deleteById(id);
    }

    /*
    * 分页查询角色列表
    * */
    public Page<Role> findByPage(String companyId,int page,int size){
        return roleDao.findAll(getSpecification(companyId), PageRequest.of(page-1,size));
    }


}
