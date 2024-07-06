package com.example.bookwise.domain.book.Enum;

import lombok.Getter;

@Getter
public enum Category {
    HEALTH_HOBBY("건강/취미"),
    ECONOMY_MANAGEMENT("경제경영"),
    HIGH_SCHOOL_TEXTBOOK("고등학교참고서"),
    PUBLIC_OFFICER_EXAM("공무원 수험서"),
    SCIENCE("과학"),
    CALENDAR_MISC("달력/기타"),
    UNIVERSITY_TEXTBOOK("대학교재"),
    COMIC("만화"),
    SOCIAL_SCIENCE("사회과학"),
    NOVEL_POETRY_PLAY("소설/시/희곡"),
    CERTIFICATION("수험서/자격증"),
    CHILDREN("어린이"),
    ESSAY("에세이"),
    TRAVEL("여행"),
    HISTORY("역사"),
    ART_POPULAR_CULTURE("예술/대중문화"),
    FOREIGN_LANGUAGE("외국어"),
    COOKING_LIVING("요리/살림"),
    INFANT("유아"),
    HUMANITIES("인문학"),
    SELF_IMPROVEMENT("자기계발"),
    MAGAZINE("잡지"),
    GENRE_NOVEL("장르소설"),
    COLLECTION_USED_COLLECTION("전집/중고전집"),
    RELIGION_DIVINATION("종교/역학"),
    GOOD_PARENTING("좋은부모"),
    TEENAGER("청소년"),
    COMPUTER_MOBILE("컴퓨터/모바일"),
    ELEMENTARY_TEXTBOOK("초등학교참고서"),
    MIDDLE_SCHOOL_TEXTBOOK("중학교참고서");

    private final String koreanValue;

    Category(String koreanValue) {
        this.koreanValue = koreanValue;
    }
}
