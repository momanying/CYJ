package com.lyh.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.lyh.springboot.common.Result;
import com.lyh.springboot.entity.Admin;
import com.lyh.springboot.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService eadminService;

    @PostMapping("/add")
    public Result add(@RequestBody Admin eadmin) {
        eadminService.add(eadmin);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Admin eadmin) {
        eadminService.update(eadmin);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        eadminService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        eadminService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Admin eadmin) {
        List<Admin> list = eadminService.selectAll(eadmin);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin eadmin = eadminService.selectById(id);
        return Result.success(eadmin);
    }
    @GetMapping("/selectOne")
    public Result selectOne(@RequestParam Integer id) {
        Admin eadmin = eadminService.selectById(id);
        return Result.success(eadmin);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Admin eadmin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> pageInfo = eadminService.selectPage(eadmin, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
