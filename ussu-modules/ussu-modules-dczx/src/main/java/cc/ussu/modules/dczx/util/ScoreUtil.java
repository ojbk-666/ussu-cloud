package cc.ussu.modules.dczx.util;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.model.vo.score.ExamMethodEnum;
import cc.ussu.modules.dczx.model.vo.score.MethodVO;
import cc.ussu.modules.dczx.model.vo.score.ToComposeResponseVO;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;

import java.util.List;

public final class ScoreUtil {

    /**
     * 获取分数
     *
     * @param loginResultVo
     * @param courseId
     * @return
     */
    public static ToComposeResponseVO getScore(DczxLoginResultVo loginResultVo, String courseId) {
        MyHttpResponse execute = MyHttpRequest.createPost("https://classroom.edufe.com.cn/api/v1/myclassroom/toCompose?courseId=" + courseId)
                .headerMap(loginResultVo.getHeaderMap(), true)
                .contentType(ContentType.FORM_URLENCODED.getValue())
                .disableCookie()
                .cookie(loginResultVo.getRequestCookie())
                // .form("courseId", courseId)
                .execute();
        if (execute.isOk()) {
            return JSONUtil.toBean(execute.body(), ToComposeResponseVO.class);
        }
        return null;
    }

    /**
     * 获取单项成绩，按比例折算之前的
     *
     * @param loginResultVo
     * @param courseId
     * @param examMethodEnum
     * @return
     */
    public static Float getScore(DczxLoginResultVo loginResultVo, String courseId, ExamMethodEnum examMethodEnum) {
        ToComposeResponseVO score = getScore(loginResultVo, courseId);
        if (score != null) {
            List<MethodVO> methodList = score.getMethodList();
            for (MethodVO methodVO : methodList) {
                if (methodVO != null && examMethodEnum.getExamMethodId().equals(methodVO.getExamMethodId())) {
                    if (StrUtil.isBlank(methodVO.getScore())) {
                        return 0F;
                    }
                    return Float.parseFloat(methodVO.getScore());
                }
            }
        }
        return null;
    }

}
