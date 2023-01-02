package cc.ussu.modules.dczx.exception.homework;

public class ShowExamPapermcapiException extends RuntimeException {

    public ShowExamPapermcapiException() {
        super("获取新的单元作业题目异常");
    }

    public ShowExamPapermcapiException(String msg) {
        super(msg);
    }
}
