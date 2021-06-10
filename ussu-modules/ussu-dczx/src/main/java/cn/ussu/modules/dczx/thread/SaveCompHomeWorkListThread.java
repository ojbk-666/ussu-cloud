package cn.ussu.modules.dczx.thread;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.ussu.common.core.util.SpringContextHolder;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.model.vo.composite.ExamResultInfo;
import cn.ussu.modules.dczx.model.vo.composite.ExamVo;
import cn.ussu.modules.dczx.model.vo.composite.InitExamListmcapiVo;
import cn.ussu.modules.dczx.service.IDcInterfaceLogService;
import cn.ussu.modules.dczx.util.DczxUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采集综合作业记录列表线程
 */
public class SaveCompHomeWorkListThread extends Thread {

    private DcInterfaceLog dcInterfaceLog;

    public SaveCompHomeWorkListThread(DcInterfaceLog dcInterfaceLog) {
        this.dcInterfaceLog = dcInterfaceLog;
    }

    @Override
    public void run() {
        // 缺少参数跳过
        if (!DczxUtil.checkInterfaceLogParam(dcInterfaceLog)) return;
        // 获取接口结果
        // 页面url: https://classroom.edufe.com.cn/CompHomework?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=7&courseValue=B0226A&courseId=B0226A
        // https://classroom.edufe.com.cn/TKGL/initExamListmcapi.action?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=7&courseValue=B0226A&customerId=1&_=1623232427808
        Map<String, List<String>> decodeParams = HttpUtil.decodeParams(dcInterfaceLog.getUrl(), CharsetUtil.UTF_8);
        // String baseUrl = "https://classroom.edufe.com.cn/TKGL/initWorkListmcapi.action?batchValue={}&gradationValue={}&subjectValue={}&exampapertypeValue={}&courseValue={}&customerId={}&_={}";
        String baseUrl = "https://classroom.edufe.com.cn/TKGL/initExamListmcapi.action?batchValue={}&gradationValue={}&subjectValue={}&exampapertypeValue={}&courseValue={}&customerId={}&_={}";
        baseUrl = StrUtil.format(baseUrl, decodeParams.get("batchValue").get(0), decodeParams.get("gradationValue").get(0), decodeParams.get("subjectValue").get(0),
                decodeParams.get("exampapertypeValue").get(0), decodeParams.get("courseValue").get(0), "1", DczxUtil.timestamp());
        dcInterfaceLog.setReqUrl(baseUrl);
        Map<String, Object> param = new HashMap<>();
        param.put("batchValue", decodeParams.get("batchValue").get(0));
        param.put("gradationValue", decodeParams.get("gradationValue").get(0));
        param.put("subjectValue", decodeParams.get("subjectValue").get(0));
        param.put("exampapertypeValue", decodeParams.get("exampapertypeValue").get(0));
        param.put("courseValue", decodeParams.get("courseValue").get(0));
        param.put("customerId", "1");
        param.put("_", DczxUtil.timestamp());
        // baseUrl = StrUtil.format(baseUrl, dcInterfaceLog.getUserid(), decodeParams.get("courseId").get(0));
        String rep = HttpRequest.get(baseUrl)
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                // .form(param)
                .execute().body();
        dcInterfaceLog.setResponseBody(rep);
        dcInterfaceLog.setResult(true);
        JSONObject jsonObject = null;
        InitExamListmcapiVo initExamListmcapiVo = null;
        try {
            jsonObject = JSON.parseObject(rep);
            initExamListmcapiVo = JSON.parseObject(rep, InitExamListmcapiVo.class);
            String code = jsonObject.getString(DczxUtil.code);
            if (StrUtil.isNotBlank(code)) {
                dcInterfaceLog.setResult(false);
                dcInterfaceLog.setReason(jsonObject.getString(DczxUtil.msg));
            }
        } catch (JSONException e) {
            // 出错
            dcInterfaceLog.setResult(false);
            dcInterfaceLog.setReason("josn解析失败");
        } finally {
            dcInterfaceLog.setRemarks("采集综合作业记录列表线程");
            saveInterfaceLog();
        }
        if (jsonObject != null && dcInterfaceLog.getResult()) {
            // 解析保存
            analyzeResponse(initExamListmcapiVo, decodeParams);
        }
    }

    // 保存接口日志
    private void saveInterfaceLog() {
        dcInterfaceLog.insert();
    }

    /**
     * 解析结果保存结果
     */
    private void analyzeResponse(InitExamListmcapiVo jsonObject, Map<String, List<String>> decodeParams) {
        LocalDateTime now = LocalDateTime.now();
        List<ExamVo> examList = jsonObject.getExamList();
        // 几套试卷
        for (ExamVo examVo : examList) {
            // 开始采集试题
            String workerId = examVo.getWORKER_ID();//19384a85-64f2-48f1-aa8b-f3a43e68f793
            String workerName = examVo.getWORKER_NAME();//移动电子商务综合作业
            Integer worker_score = examVo.getWORKER_SCORE();//68
            List<ExamResultInfo> examResultInfos = examVo.getExamResultInfos();

            // 作业记录
            for (ExamResultInfo examResultInfo : examResultInfos) {
                // 获取标识，校验是否已抓取过该记录，抓取
                String exampaperId = examResultInfo.getExampaperId();
                String exampaperSeq = examResultInfo.getExampaperSeq();
                examResultInfo.getExampaperId();
                // 构造参数
                String pu_courseValue = decodeParams.get("courseValue").get(0);
                String pu_subjectValue = decodeParams.get("subjectValue").get(0);
                String pu_gradationValue = decodeParams.get("gradationValue").get(0);
                String pu_exampapertypeValue = decodeParams.get("exampapertypeValue").get(0);
                String customerId = examResultInfo.getCustomerId();
                // 页面 https://classroom.edufe.com.cn/CompHomework?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=7&courseValue=B0226A&courseId=B0226A
                // String u = "https://classroom.edufe.com.cn/HomeWorkHistoryPaper?tactic_id={}&courseCd={}&batchValue={}&gradationValue={}&paperName={}&subjectValue={}&exampapertypeValue={}&exampaperId={}&exampaperSeq={}";
                // String u = "https://classroom.edufe.com.cn/TKGL/showHistoryPapermcapi.action?tactic_id=19384a85-64f2-48f1-aa8b-f3a43e68f793&courseValue=B0226A&exampapertypeValue=7&customerId=1&examPaper_Id=be6a4141-209c-4fb7-b8c6-a69ddde39cfc&exampaperSeq=6f164433-ea43-40d0-a197-71d6cb97735e&_=1623235881675";
                String u = "https://classroom.edufe.com.cn/TKGL/showHistoryPapermcapi.action?tactic_id={}&courseValue={}&exampapertypeValue={}&customerId={}&examPaper_Id={}&exampaperSeq={}&_={}&paperName={}";
                String formatUrl = StrUtil.format(u, workerId, pu_courseValue,pu_exampapertypeValue,customerId, exampaperId, exampaperSeq, dcInterfaceLog.getTime(), URLUtil.encode(workerName));
                if (existsThis(formatUrl)) continue;
                // 开始采集
                // DcInterfaceLog newDcInterfaceLog = ObjectUtil.clone(dcInterfaceLog);
                DcInterfaceLog newDcInterfaceLog = dcInterfaceLog;
                if (newDcInterfaceLog != null) {
                    newDcInterfaceLog.setUrl(formatUrl);
                    new SaveDczxCompPaperQuestionThread(newDcInterfaceLog).start();
                }
            }
        }
    }

    /**
     * 判断是否已存在记录
     *
     * @param url
     * @return
     */
    public boolean existsThis(String url) {
        IDcInterfaceLogService service = SpringContextHolder.getBean(IDcInterfaceLogService.class);
        QueryWrapper<DcInterfaceLog> qw = new QueryWrapper<>();
        qw.eq("url", url);
        int count = service.count(qw);
        return count > 0;
    }

}
