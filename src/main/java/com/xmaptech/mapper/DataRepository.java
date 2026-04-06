package com.xmaptech.mapper;

import com.xmaptech.entity.SourceData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<SourceData, Long> {

    @Query("SELECT n FROM SourceData n WHERE " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.translation) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.tags) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY n.weight DESC")
    List<SourceData> searchByKeyword(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("UPDATE SourceData n SET n.weight = n.weight + :increment, n.lastViewed = CURRENT_TIMESTAMP WHERE n.id = :id")
    void updateWeightAndTimestamp(@Param("id") Integer id, @Param("increment") Integer increment);

    @Query(value =
            // --- 第一部分：60% 绝对高权重数据 (用户最常看的) ---
            "(SELECT * FROM source_data " +
                    " ORDER BY weight DESC " +
                    " LIMIT :highCount) " +

                    "UNION ALL " +

                    // --- 第二部分：40% 遗忘打捞数据 (针对久未露面的词) ---
                    "(SELECT * FROM source_data " +
                    " WHERE id NOT IN (" +
                    "   SELECT id FROM source_data ORDER BY weight DESC LIMIT :highCount" +
                    " ) " +
                    // 核心逻辑 计算天数。如果 last_viewed 为空，用 0 代替以防报错
                    // 算法：天数差越大（久未看） 且 权重越小（非高频），分值越高
                    " ORDER BY (DATEDIFF('DAY', COALESCE(last_viewed, create_time), CURRENT_DATE) + 1.0) / (weight + 1.0) DESC " +
                    " LIMIT :lowCount)",
            nativeQuery = true)
    List<SourceData> findSmartRecommendedNotes(@Param("highCount") int highCount, @Param("lowCount") int lowCount);

    Page<SourceData> findAllByOrderByCreateTimeDesc(Pageable pageable);
}
