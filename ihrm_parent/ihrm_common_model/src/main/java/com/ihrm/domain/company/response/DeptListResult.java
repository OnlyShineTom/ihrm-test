package com.ihrm.domain.company.response;

import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeptListResult {
    private String companyId;
    private String companyName;
    private String companyMessage;//公司联系人
    private List<Department> depts;

    public DeptListResult(Company company,List depts){
        this.companyId=company.getId();
        this.companyName=company.getName();
        this.companyMessage=company.getLegalRepresentative();//公司联系人
        this.depts=depts;
    }
}
