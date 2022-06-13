package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/*
*   部门操作业务逻辑层
* */

@Service
public class DepartmentService extends BaseService {
     @Autowired
     private IdWorker idWorker;
     @Autowired
     private DepartmentDao departmentDao;

    /*
    * 保存部门
    * */
    public void save(Department department){
        //填充其他参数
        department.setId(idWorker.nextId()+"");
        department.setCreateTime(new Date());
        departmentDao.save(department);
    }

    /*
    *   更新部门信息
    * */
    public void update(Department department){
        //1.根据id查询部门
        Department newDepartment = departmentDao.findById(department.getId()).get();
        //设置部门属性
        newDepartment.setCode(department.getCode());
        newDepartment.setName(department.getName());
        newDepartment.setIntroduce(department.getIntroduce());
        newDepartment.setManager(department.getManager());
        //更新部门
        departmentDao.save(newDepartment);
    }
    /*
    *
    * 根据id获取部门信息
    * */
    public Department findById(String id){
        return departmentDao.findById(id).get();
    }

    /*
    *  根据id删除部门
    *
    * */
    public void delete(String id){
        departmentDao.deleteById(id);
    }

    /*
    * 获取部门列表
    * */
    public List<Department> findAll(String companyId){
//        Specification<Department> specification = new Specification<Department>() {
//            /*
//             *   用户用于构造查询条件
//             *       root            :包含了所有的对象数据属性
//             *       criteriaQuery   :包含了一些更高级的查询，我们一般也不使用
//             *       criteriaBuilder :构造查询条件
//             *
//             * */
//            @Override
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                //1.根据企业id查询
//                return criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
//            }
//        };

        return departmentDao.findAll(getSpecification(companyId));
    }




}
