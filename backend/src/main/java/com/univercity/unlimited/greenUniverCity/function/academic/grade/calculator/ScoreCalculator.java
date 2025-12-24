package com.univercity.unlimited.greenUniverCity.function.academic.grade.calculator;

import com.univercity.unlimited.greenUniverCity.function.academic.assignment.entity.Submission;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.GradeItem;
import com.univercity.unlimited.greenUniverCity.function.academic.grade.entity.StudentScore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *  순수 성적 계산기 (Pure Score Calculator)
 * * 역할:
 * - Service에서 조회한 데이터를 넘겨받아 '계산'만 전담
 * - DB 의존성이 없어 단위 테스트가 매우 용이함
 */
@Component
@Slf4j
public class ScoreCalculator {

    /**
     * 🚀 [메인] 최종 성적 통합 계산 메서드
     * - 역할: Service가 던져준 데이터(List)를 가지고 반복문을 돌며 최종 점수를 산출
     * - 이 메서드 하나로 계산 로직을 끝냅니다.
     */
    public float calculateFinalGrade(List<GradeItem> gradeItems,
                                     List<Submission> submissions,
                                     List<StudentScore> studentScores) {

        List<Float> weightedScores = new ArrayList<>();
        float totalWeight = 0.0f;

        for (GradeItem item : gradeItems) {
            float obtainedScore = 0.0f;

            // Enum 타입에 따라 계산 방식 분기 처리
            switch (item.getItemType()) {
                case ASSIGNMENT:
                    // 과제 점수 계산 (Submission 리스트 전체 전달하여 평균 산출)
                    obtainedScore = calculateAssignmentScore(submissions, item);
                    break;

                case MIDTERM:    // 중간고사
                case FINAL:      // 기말고사
                case ATTENDANCE: // 출석
                case ETC:        // 기타
                default:
                    // 시험 및 기타 점수는 StudentScore 테이블에서 해당 항목(ItemId)의 점수를 찾아옴
                    StudentScore matchScore = studentScores.stream()
                            .filter(s -> s.getGradeItem().getItemId().equals(item.getItemId()))
                            .findFirst()
                            .orElse(null);

                    obtainedScore = calculateExamScore(matchScore, item);
                    break;
            }

            // 가중치 적용 후 리스트에 추가
            weightedScores.add(applyWeight(obtainedScore, item));
            totalWeight += item.getWeightPercent();
        }

        // 최종 합산 반환 (비율 보정 포함)
        return calculateFinalTotal(weightedScores, totalWeight);
    }

    /**
     *  과제 점수 계산 (평균 점수 환산)
     * 로직: (학생이 획득한 총점 / 과제들의 총 배점) * 평가항목 배점
     * * @param submissions 학생의 모든 과제 제출 내역 (Service에서 미리 조회해서 넘겨줌)
     * @param gradeItem '과제' 타입의 평가 항목 정보 (예: 반영비율 30%)
     * @return 환산된 점수
     */
    public float calculateAssignmentScore(List<Submission> submissions, GradeItem gradeItem) {
        if (submissions == null || submissions.isEmpty()) {
            log.debug("계산 대상 과제 제출물이 없습니다.");
            return 0.0f;
        }

        float totalObtained = 0.0f; // 학생 획득 점수 합계
        float totalMax = 0.0f;      // 과제 만점 합계

        for (Submission sub : submissions) {
            // 채점된 점수가 있을 때만 합산 (미채점은 0점 처리)
            if (sub.getScore() != null) {
                totalObtained += sub.getScore();
                totalMax += sub.getAssignment().getMaxScore();
            }
        }

        if (totalMax == 0.0f) {
            return 0.0f; // 분모가 0인 경우 방지
        }

        // 1. 획득률 계산 (예: 200점 만점에 180점 -> 0.9)
        float rate = totalObtained / totalMax;

        // 2. 평가항목 만점 기준 환산 (예: 평가항목이 100점 만점이면 -> 90점)
        return rate * gradeItem.getMaxScore();
    }

    /**
     * 📄 시험/기타 점수 계산
     * 로직: StudentScore 테이블의 점수를 그대로 사용
     */
    public float calculateExamScore(StudentScore studentScore, GradeItem gradeItem) {
        if (studentScore == null) {
            log.debug("점수 데이터 미입력 - 항목: {}", gradeItem.getItemName());
            return 0.0f;
        }
        return studentScore.getScoreObtained();
    }

    /**
     * 📊 가중치 적용 (Weighted Score)
     * 로직: (획득점수 / 만점 * 100) * (반영비율 / 100)
     */
    public float applyWeight(float obtainedScore, GradeItem gradeItem) {
        if (gradeItem.getMaxScore() == 0) return 0.0f;

        // 1. 100점 만점으로 정규화
        float normalizedScore = (obtainedScore / gradeItem.getMaxScore()) * 100;

        // 2. 가중치(반영비율) 적용
        return normalizedScore * (gradeItem.getWeightPercent() / 100.0f);
    }

    /**
     * 📈 최종 합산 (비율 보정 포함)
     * 로직: 가중 점수 합계 / 가중치 총합 * 100
     */
    public float calculateFinalTotal(List<Float> weightedScores, float totalWeight) {
        float sum = weightedScores.stream()
                .reduce(0f, Float::sum);

        // 총 가중치가 100%가 아닐 경우 (예: 80%만 설정된 상태) 100% 기준으로 환산
        if (totalWeight > 0 && totalWeight != 100.0f) {
            return (sum / totalWeight) * 100.0f;
        }

        return sum;
    }


}