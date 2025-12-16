package com.univercity.unlimited.greenUniverCity.function.academic.section.entity;

/**
 * 분반 수업 유형
 * - ONLINE: 비대면 온라인 수업
 * - OFFLINE: 대면 오프라인 수업
 */
public enum SectionType {
    ONLINE("온라인", "비대면 온라인 수업"),
    OFFLINE("오프라인", "대면 오프라인 수업");

    private final String displayName;
    private final String description;

    SectionType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 문자열로부터 SectionType 조회 (대소문자 무시)
     */
    public static SectionType fromString(String type) {
        if (type == null) {
            return null;
        }

        try {
            return SectionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "유효하지 않은 수업 유형입니다: " + type +
                            ". 가능한 값: ONLINE, OFFLINE"
            );
        }
    }

    /**
     * 온라인 수업 여부 확인
     */
    public boolean isOnline() {
        return this == ONLINE;
    }

    /**
     * 오프라인 수업 여부 확인 (강의실 필요 여부)
     */
    public boolean requiresClassroom() {
        return this == OFFLINE;
    }
}