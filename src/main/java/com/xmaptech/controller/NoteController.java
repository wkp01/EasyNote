package com.xmaptech.controller;

import com.xmaptech.constants.Result;
import com.xmaptech.entity.NoteDTO;
import com.xmaptech.entity.Response;
import com.xmaptech.entity.SourceData;
import com.xmaptech.service.DataSourceServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Resource
    private DataSourceServiceImpl dataSourceServiceImpl;

    // 存数据
    @PostMapping("/save")
    public Response<String> save(@RequestBody NoteDTO note) {
        try {
            return dataSourceServiceImpl.addNote(note);
        } catch (Exception exception) {
            System.err.println("系统异常:" + exception.getMessage());
            return Response.error(Result.error);
        }
    }

    // 关键词 检索 (英文 中文 tags)
    @GetMapping("search")
    public Response<List<SourceData>> search(@RequestParam("keyword") String keyword) {
        try {
            return dataSourceServiceImpl.searchSourceData(keyword);
        } catch (Exception exception) {
            return Response.error(Result.search_data_error);
        }
    }

    // 权重 并且更新时间
    @PutMapping("/update")
    public void update(@RequestParam("id") Integer id,@RequestParam("increment") int increment) {
        try {
            dataSourceServiceImpl.updateNote(id,increment);
        } catch (Exception exception) {
            System.err.println("系统异常:" + exception.getMessage());
        }
    }

    @GetMapping("recommend")
    public Response<List<SourceData>> recommend() {
       try {
           return dataSourceServiceImpl.getRecommended();
       } catch (Exception exception) {
           System.err.println("系统异常:" + exception.getMessage());
           return Response.error(Result.error);
       }
    }

    @GetMapping("/all")
    public Response<Page<SourceData>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return dataSourceServiceImpl.getAllNotes(page, size);
    }
}
