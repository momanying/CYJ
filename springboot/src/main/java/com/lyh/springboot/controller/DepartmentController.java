package com.lyh.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.lyh.springboot.common.Result;
import com.lyh.springboot.entity.Department;
import com.lyh.springboot.service.DepartmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;

    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        departmentService.add(department);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Department department) {
        departmentService.update(department);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        departmentService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        departmentService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Department department) {
        List<Department> list = departmentService.selectAll(department);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Department department = departmentService.selectById(id);
        return Result.success(department);
    }
    @GetMapping("/selectOne")
    public Result selectOne(@RequestParam Integer id) {
        Department department = departmentService.selectById(id);
        return Result.success(department);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Department department,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Department> pageInfo = departmentService.selectPage(department, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
