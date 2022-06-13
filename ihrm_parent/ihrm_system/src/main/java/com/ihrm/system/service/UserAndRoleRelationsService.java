package com.ihrm.system.service;

import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.RoleAndUserRelations;
import com.ihrm.system.dao.UserAndRoleRelationsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAndRoleRelationsService {
    @Autowired
    private UserAndRoleRelationsDao userAndRoleRelationsDao;

    @Autowired
    private RoleService roleService;

    public List<RoleAndUserRelations> findRoleByUserId(String userId){
        return userAndRoleRelationsDao.findByUserId(userId);
    }


    public List<Role> getRoleDetailByRoleId(List<RoleAndUserRelations> roleByUserId) {
        List<Role> res = new ArrayList<>();
        for (RoleAndUserRelations userAndRoleRea : roleByUserId) {
            Role role = roleService.findById(userAndRoleRea.getRoleId());
            if (!ObjectUtils.isEmpty(role)){
                res.add(role);
            }
        }
        return res;
    }

}
