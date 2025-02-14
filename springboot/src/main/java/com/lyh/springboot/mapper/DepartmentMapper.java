package com.lyh.springboot.mapper;

import com.lyh.springboot.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DepartmentMapper {
    List<Department> selectAll(Department department);

    @Select("select * from `department` where id = #{id}")
    Department selectById(Integer id);

    void insert(Department department);

    void updateById(Department department);

    @Delete("delete from `department` where id = #{id}")
    void deleteById(Integer id);
}
