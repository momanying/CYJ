package com.lyh.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyh.springboot.entity.Account;
import com.lyh.springboot.entity.Employee;
import com.lyh.springboot.exception.CustomException;
import com.lyh.springboot.mapper.EmployeeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;

    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }
    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }
    public PageInfo<Employee> selectPage(Employee employee, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return new PageInfo<>(list);
    }

    public void add(Employee employee) {
        String username = employee.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee != null){
            throw new CustomException("500","账号已存在，请更换别的账号");
        }
        if(StrUtil.isBlank(employee.getPassword())){
            employee.setPassword("123");
        }
        if(StrUtil.isBlank(employee.getName())){
            employee.setName(employee.getUsername());
        }
        employee.setRole("EMP");
        employeeMapper.insert(employee);
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void delete(Integer id) {
        employeeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            employeeMapper.deleteById(id);
        }
    }

    public Employee login(Account account) {
        String username = account.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee == null){
            throw new CustomException("500","账号不存在");
        }
        String password = account.getPassword();
        if(!dbEmployee.getPassword().equals(password)){
            throw new CustomException("500","账号或密码错误");
        }
        return dbEmployee;
    }

    public void register(Employee employee) {
        String username = employee.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee != null){
            throw new CustomException("500","账号已存在，请更换别的账号");
        }
        employeeMapper.insert(employee);
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Employee employee = this.selectById(id);
        if(!employee.getPassword().equals(account.getPassword())){
            throw new CustomException("500","对不起，原密码错误");
        }
        employee.setPassword(account.getNewPassword());
        this.update(employee);
    }
}
