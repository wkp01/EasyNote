package com.xmaptech.mapper;

import com.xmaptech.entity.NoteDTO;
import com.xmaptech.entity.Response;
import com.xmaptech.entity.SourceData;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DataSourceService {
    // 新增数据
    Response<String> addNote(NoteDTO note);
    // 更新 数据 权重 和 时间
    void updateNote(Integer id,Integer increment);
    Response<List<SourceData>> searchSourceData(String keyword);
    Response<List<SourceData>> getRecommended();
    Response<Page<SourceData>> getAllNotes(int page, int size);

}
