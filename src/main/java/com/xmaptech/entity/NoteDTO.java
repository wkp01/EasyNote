package com.xmaptech.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {

    private String content; // 原文
    private String translation; // 翻译
    private int type; // 类型
    private String tags; // 标签

}
