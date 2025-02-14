package com.lyh.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyh.springboot.entity.Account;
import com.lyh.springboot.entity.Admin;
import com.lyh.springboot.exception.CustomException;
import com.lyh.springboot.mapper.AdminMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return new PageInfo<>(list);
    }

    public void add(Admin admin) {
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin != null){
            throw new CustomException("500","账号已存在，请更换别的账号");
        }
        if(StrUtil.isBlank(admin.getPassword())){
            admin.setPassword("admin");
        }
        if(StrUtil.isBlank(admin.getName())){
            admin.setName(admin.getUsername());
        }
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void delete(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            adminMapper.deleteById(id);
        }
    }

    public Admin login(Account account) {
        String username = account.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin == null){
            throw new CustomException("500","账号不存在");
        }
        String password = account.getPassword();
        if(!dbAdmin.getPassword().equals(password)){
            throw new CustomException("500","账号或密码错误");
        }
        return dbAdmin;
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Admin admin = this.selectById(id);
        if(!admin.getPassword().equals(account.getPassword())){
            throw new CustomException("500","对不起，原密码错误");
        }
        admin.setPassword(account.getNewPassword());
        this.update(admin);
    }
}
