package com.lyh.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.lyh.springboot.common.Result;
import com.lyh.springboot.entity.Article;
import com.lyh.springboot.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @PostMapping("/add")
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Article article) {
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        articleService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        articleService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Article article) {
        List<Article> list = articleService.selectAll(article);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Article article = articleService.selectById(id);
        return Result.success(article);
    }
    @GetMapping("/selectOne")
    public Result selectOne(@RequestParam Integer id) {
        Article article = articleService.selectById(id);
        return Result.success(article);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Article article,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Article> pageInfo = articleService.selectPage(article, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
