package com.lyh.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.pagehelper.PageInfo;
import com.lyh.springboot.common.Result;
import com.lyh.springboot.entity.Employee;
import com.lyh.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    @PostMapping("/add")
    public Result add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        employeeService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        employeeService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Employee employee) {
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }
    @GetMapping("/selectOne")
    public Result selectOne(@RequestParam Integer id) {
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Employee employee,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Employee> pageInfo = employeeService.selectPage(employee, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<Employee> employeeList =  employeeService.selectAll(null);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("sex", "性别");
        writer.addHeaderAlias("no", "工号");
        writer.addHeaderAlias("age", "年龄");
        writer.addHeaderAlias("description", "个人介绍");
        writer.addHeaderAlias("departmentName", "部门");
        writer.setOnlyAlias(true);
        writer.write(employeeList,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工信息","UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out);
        writer.close();
    }
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader =  ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias( "账号","username");
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("性别","sex");
        reader.addHeaderAlias("工号","no");
        reader.addHeaderAlias("年龄","age");
        reader.addHeaderAlias("个人介绍","description");
        reader.addHeaderAlias("部门","departmentName");
        List<Employee> employeeList =  reader.readAll(Employee.class);
        for (Employee employee : employeeList) {
            employeeService.add(employee);
        }
        return Result.success();
    }
}
