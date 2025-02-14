package com.lyh.springboot.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.lyh.springboot.common.Result;
import com.lyh.springboot.entity.Account;
import com.lyh.springboot.entity.Article;
import com.lyh.springboot.entity.Employee;
import com.lyh.springboot.exception.CustomException;
import com.lyh.springboot.service.AdminService;
import com.lyh.springboot.service.ArticleService;
import com.lyh.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class WebController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdminService adminService;
    @Resource
    private ArticleService articleService;

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account result = null;
        if("ADMIN".equals(account.getRole())) {
            result = adminService.login(account);
        }else if ("EMP".equals(account.getRole())) {
            result = employeeService.login(account);
        }
        else {
            throw new CustomException("500","非法输入");
        }
        return Result.success(result);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Employee employee) {
        employeeService.register(employee);
        return Result.success();
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        }else if ("EMP".equals(account.getRole())) {
            employeeService.updatePassword(account);
        }else {
            throw new CustomException("500","非法输入");
        }
        return Result.success();
    }

    @GetMapping("/barData")
    public Result barData() {
        Map<String, Object> map = new HashMap<>();
        List<Employee> employeeSet = employeeService.selectAll(null);
        Set<String> departmentNameList = employeeSet.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
        map.put("department",departmentNameList);
        List<Long> countList = new ArrayList<>();
        for (String departmentName : departmentNameList) {
            long count = employeeSet.stream().filter(employee -> employee.getDepartmentName().equals(departmentName)).count();
            countList.add(count);
        }
        map.put("count",countList);
        return Result.success(map);
    }

    @GetMapping("/lineData")
    public Result lineData() {
        Map<String, Object> map = new HashMap<>();

        // 设置起始日期和结束日期
        DateTime start = DateUtil.parse("2025-02-08");  // 起始日期：2025年2月8日
        DateTime end = DateUtil.parse("2025-02-15");    // 结束日期：2025年2月15日

        // 获取从 start 到 end 的日期范围
        List<DateTime> dateTimeList = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);

        // 格式化日期为 "MM月dd日"
        List<String> dateStrList = dateTimeList.stream()
                .map(dateTime -> DateUtil.format(dateTime, "MM月dd日"))
                .sorted(Comparator.naturalOrder())  // 日期排序
                .collect(Collectors.toList());

        map.put("date", dateStrList);  // x轴数据

        // 获取每一天发布文章的数量
        List<Integer> countList = new ArrayList<>();
        for (DateTime day : dateTimeList) {
            String dateStr = DateUtil.format(day, "yyyy-MM-dd");
            Integer count = articleService.selectCountByDate(dateStr);  // 获取指定日期的文章发布数量
            countList.add(count);
        }

        map.put("count", countList);  // y轴发布文章的数量

        return Result.success(map);
    }

    @GetMapping("/pieData")
    public Result pieData() {
        List<Map<String,Object>> list = new ArrayList<>();
        List<Employee> employeeSet = employeeService.selectAll(null);
        Set<String> departmentNameList = employeeSet.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
        for (String departmentName : departmentNameList) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("name",departmentName);
            long count = employeeSet.stream().filter(employee -> employee.getDepartmentName().equals(departmentName)).count();
            map.put("value",count);
            list.add(map);
        }
        return Result.success(list);
    }

}
