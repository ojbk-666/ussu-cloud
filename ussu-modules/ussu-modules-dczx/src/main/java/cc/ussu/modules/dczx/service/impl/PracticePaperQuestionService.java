package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.entity.DcPaper;
import cc.ussu.modules.dczx.model.vo.SimulatedQuestionResponse;
import cc.ussu.modules.dczx.service.IDcInterfaceLogService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * 随堂随练收集题目
 */
@Service
public class PracticePaperQuestionService {

    @Autowired
    private SimulatedQuestionService simulatedQuestionService;
    @Autowired
    private IDcInterfaceLogService dcInterfaceLogService;

    @Async
    @Transactional
    public void collect(DcInterfaceLog dcInterfaceLog, String courseName) {
        String url = dcInterfaceLog.getUrl();
        if (DczxUtil.existsInterfaceLog(url)) {
            return;
        }
        url = URLUtil.decode(URLUtil.decode(url));
        Map<String, String> param = HttpUtil.decodeParamMap(url, CharsetUtil.CHARSET_UTF_8);
        String tactic_id = MapUtil.getStr(param, "tactic_id");
        String courseCd = MapUtil.getStr(param, "courseCd");
        String batchValue = MapUtil.getStr(param, "batchValue");
        String gradationValue = MapUtil.getStr(param, "gradationValue");
        String paperName = MapUtil.getStr(param, "paperName");
        String subjectValue = MapUtil.getStr(param, "subjectValue");
        String chapterId = MapUtil.getStr(param, "chapterId");
        String exampapertypeValue = MapUtil.getStr(param, "exampapertypeValue");
        String courseId = MapUtil.getStr(param, "courseId");
        String reqUrl = "https://classroom.edufe.com.cn/TKGL/showExamPapermcapi.action?tactic_id={tactic_id}&batchValue={batchValue}&gradationValue={gradationValue}&subjectValue={subjectValue}&courseValue={courseCd}&chapterssections_id={chapterId}&exampapertypeValue={exampapertypeValue}&customerId=1";
        dcInterfaceLog.setReqUrl(StrUtil.format(reqUrl, param)).setResult(true).setRemarks("采集随堂随练试题");
        String rep = HttpRequest.get(dcInterfaceLog.getReqUrl())
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                .execute().body();
        dcInterfaceLog.setResponseBody(rep);
        try {
            SimulatedQuestionResponse simulatedQuestionResponse = JSONUtil.toBean(rep, SimulatedQuestionResponse.class);
            if (simulatedQuestionResponse == null || StrUtil.isBlank(simulatedQuestionResponse.getPAPERID())) {
                // 失败
                dcInterfaceLog.setResult(false).setReason(rep);
            } else {
                dcInterfaceLogService.save(dcInterfaceLog);
                DcPaper dcPaper = new DcPaper().setPaperId(simulatedQuestionResponse.getPAPERID())
                        .setPaperName(paperName).setInterfaceLogId(dcInterfaceLog.getId()).setCreateTime(new Date());
                dcPaper = DczxUtil.savePaper(dcPaper);
                simulatedQuestionService.add(simulatedQuestionResponse, dcPaper, dcInterfaceLog, courseId);
            }
        } catch (Exception e) {
            dcInterfaceLog.setResult(false).setReason(e.getMessage());
        } finally {
            // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
            dcInterfaceLog.setId(null);
            dcInterfaceLogService.saveOrUpdate(dcInterfaceLog);
        }

    }

}
