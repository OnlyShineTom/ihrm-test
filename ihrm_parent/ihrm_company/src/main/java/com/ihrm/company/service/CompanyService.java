package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService  {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private IdWorker idWorker;

    //1.保存企业
    //  首先需要配置idwork到工程中
    //  在service中注入idwork
    //  通过idwork生成id
    //  保存企业
    public Company add(Company company){
        //1.基本属性的设置
        String id = idWorker.nextId()+"";
        company.setId(id);
        company.setCreateTime(new Date());
        //默认状态
        company.setAuditState("1"); //0 为审核   1 已审核
        company.setState(1);  //0 为激活， 1已激活
        company.setBalance(0d);
        return companyDao.save(company);
    }

    /*
        2.更新企业
            1.参数:company
            2.根据id查询企业对象
            3.设置修改的属性
            4.调用dao完成更新操作
    */
    public Company update(Company company){
        return companyDao.save(company);
    }

    //3.删除企业
    public void deleteById(String id){
        companyDao.deleteById(id);
    }

    //4.根据id查询企业
    public Company findById(String id){
      return companyDao.findById(id).get();
    }

    //5.查询企业列表
    public List<Company> findAll(){
        return companyDao.findAll();
    }


}
