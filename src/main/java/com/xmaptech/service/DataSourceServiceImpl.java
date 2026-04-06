package com.xmaptech.service;

import com.xmaptech.constants.Result;
import com.xmaptech.entity.NoteDTO;
import com.xmaptech.entity.Response;
import com.xmaptech.entity.SourceData;
import com.xmaptech.mapper.DataRepository;
import com.xmaptech.mapper.DataSourceService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Resource
    private DataRepository dataRepository;

    @Override
    public Response<String> addNote(NoteDTO note) {

        String content = note.getContent();
        String translation = note.getTranslation();
        int type = note.getType();
        String tags = note.getTags();
        LocalDateTime now = LocalDateTime.now();

        SourceData sourceData =
                new SourceData(null,content, translation, type, 10, now, now, tags);

        dataRepository.save(sourceData);
        return Response.success(Result.add_data,null);

    }

    @Override
    public void updateNote(Integer id,Integer increment) {
        dataRepository.updateWeightAndTimestamp(id, increment);
    }

    @Override
    public Response<List<SourceData>> searchSourceData(String keyword) {
        List<SourceData> sourceData = dataRepository.searchByKeyword(keyword);
        return Response.success(Result.search_data,sourceData);
    }

    @Override
    public Response<List<SourceData>> getRecommended() {
        List<SourceData> smartRecommendedNotes = dataRepository.findSmartRecommendedNotes(60, 40);
        return Response.success(Result.success,smartRecommendedNotes);
    }

    @Override
    public Response<Page<SourceData>> getAllNotes(int page, int size) {
        // PageRequest.of(页码, 每页数量)
        // 注意：JPA 的页码是从 0 开始的
        Page<SourceData> dataPage = dataRepository.findAllByOrderByCreateTimeDesc(PageRequest.of(page, size));
        return Response.success(Result.success, dataPage);
    }

}
