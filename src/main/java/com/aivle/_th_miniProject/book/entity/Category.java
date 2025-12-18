package com.aivle._th_miniProject.book.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Category {
    NOVEL("소설"),
    SCIENCE_FICTION("SF"),
    FANTASY("판타지"),
    MYSTERY("미스터리"),
    ROMANCE("로맨스"),
    SELF_HELP("자기계발"),
    ESSAY("에세이"),
    HISTORY("역사"),
    SCIENCE("과학"),
    OTHER("기타");

    private final String koreanName;

    Category(String koreanName) {
        this.koreanName = koreanName;
    }

    //JSON 입력 → Enum 변환
    @JsonCreator
    public static Category from(String input) {
        //영어
        for (Category c : values()) {
            if (c.name().equalsIgnoreCase(input)) {
                return c;
            }
        }
        //한글
        for (Category c : values()) {
            if (c.koreanName.equalsIgnoreCase(input)) {
                return c;
            }
        }
        throw new IllegalArgumentException("잘못된 카테고리입니다: " + input);
    }

    //JSON 출력 시 한글로 나가게
    @JsonValue
    public String toJson() {
        return this.koreanName;
    }
}
