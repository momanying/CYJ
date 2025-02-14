package com.lyh.springboot.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyh.springboot.entity.Account;
import com.lyh.springboot.entity.Article;
import com.lyh.springboot.exception.CustomException;
import com.lyh.springboot.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }
    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }
    public PageInfo<Article> selectPage(Article article, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return new PageInfo<>(list);
    }

    public void add(Article article) {
        article.setTime(DateUtil.now());
        articleMapper.insert(article);
    }

    public void update(Article article) {
        articleMapper.updateById(article);
    }

    public void delete(Integer id) {
        articleMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            articleMapper.deleteById(id);
        }
    }

    public Integer selectCountByDate(String date) {
        return articleMapper.selectCountByDate(date);
    }
}
