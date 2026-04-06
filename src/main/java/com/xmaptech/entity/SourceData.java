package com.xmaptech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "SourceData")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 主键 自增
    private String content; // 单词 短语 或者句子原文
    private String translation; // 中文含义
    private int type; // 类型 VOCABULARY(词汇1) PHRASE(短语2) SENTENCE(句子3)
    private Integer weight; // 权重(初始为 10 点击一次 +2)

    private LocalDateTime lastViewed;
    private LocalDateTime createTime;

    private String tags; // 标签 (如 考研 经济学等)

}
