package com.lyh.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyh.springboot.entity.Account;
import com.lyh.springboot.entity.Department;
import com.lyh.springboot.exception.CustomException;
import com.lyh.springboot.mapper.DepartmentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;

    public List<Department> selectAll(Department department) {
        return departmentMapper.selectAll(department);
    }
    public Department selectById(Integer id) {
        return departmentMapper.selectById(id);
    }
    public PageInfo<Department> selectPage(Department department, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Department> list = departmentMapper.selectAll(department);
        return new PageInfo<>(list);
    }

    public void add(Department department) {
        departmentMapper.insert(department);
    }

    public void update(Department department) {
        departmentMapper.updateById(department);
    }

    public void delete(Integer id) {
        departmentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            departmentMapper.deleteById(id);
        }
    }
}
