package cc.ussu.modules.dczx.exception;

public class GetAllStudyPlanFailedException extends RuntimeException {

    public GetAllStudyPlanFailedException() {
        super("获取全部学习计划失败");
    }

    public GetAllStudyPlanFailedException(String message) {
        super(message);
    }
}
