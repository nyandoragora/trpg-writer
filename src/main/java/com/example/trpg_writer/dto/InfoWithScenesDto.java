package com.example.trpg_writer.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoWithScenesDto {

    // 情報のID (infoId)
    private Integer id;

    // 情報のタイトル
    private String title;

    // 情報の本文の要約
    private String summary;

    // この情報が所属している全てのシーン名のリスト
    private List<String> sceneNames;

}
