package com.drop.leaves.itext;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mobingsen
 */
@Data
public class ExamScore {

    /** 班级名称 */
    private String gradeName;
    /** 班级类型 */
    private String classType;
    /** 班主任 */
    private String teacherName;
    /** 学生名 */
    private String stuName;
    /** 考试评价 */
    private String  eval;
    /** 学科成绩 */
    private Map<String, String> score;

    public ExamScore() {
        this.gradeName = "小学班";
        this.classType = "中等";
        this.teacherName = "小红";
        this.stuName = "王威";
        this.eval = "该生是一个聪明,善于开动脑筋,勇于探索,富有进取心的好学生。以顽强的意志力和拼搏精神感染着每一个同学,不愧是班级同学的榜样。你学习刻苦,成绩优异并且稳定。希望你能戒骄戒躁、积极进取,力争“百尺竿头,更进一步”。";
        Map<String, String> score = new HashMap<>();
        score.put("语文", "90");
        score.put("数学", "92");
        score.put("英语", "75");
        this.score = score;
    }
}
