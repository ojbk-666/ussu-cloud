package cc.ussu.modules.dczx.thread;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.model.vo.homework.ExamResultInfo;
import cc.ussu.modules.dczx.model.vo.homework.HomeWork;
import cc.ussu.modules.dczx.model.vo.homework.InitWorkListmcapiResponse;
import cc.ussu.modules.dczx.service.IDcInterfaceLogService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采集单元作业记录列表线程
 */
public class SaveHomeWorkListThread extends Thread {

    private DcInterfaceLog dcInterfaceLog;

    public SaveHomeWorkListThread(DcInterfaceLog dcInterfaceLog) {
        this.dcInterfaceLog = dcInterfaceLog;
    }

    @Override
    public void run() {
        DcInterfaceLog dcInterfaceLog = this.dcInterfaceLog;
        // 缺少参数跳过
        if (!DczxUtil.checkInterfaceLogParam(dcInterfaceLog)) {
            return;
        }
        // 获取接口结果
        // 页面url: https://classroom.edufe.com.cn/HomeWork?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=2&courseValue=B2001A&courseId=B2001A
        // https://classroom.edufe.com.cn/TKGL/initWorkListmcapi.action?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=2&courseValue=B2001A&customerId=1&_=1594274032338
        Map<String, List<String>> decodeParams = HttpUtil.decodeParams(dcInterfaceLog.getUrl(), CharsetUtil.UTF_8);
        String baseUrl = "https://classroom.edufe.com.cn/TKGL/initWorkListmcapi.action?batchValue={}&gradationValue={}&subjectValue={}&exampapertypeValue={}&courseValue={}&customerId={}&_={}";
        baseUrl = StrUtil.format(baseUrl, decodeParams.get("batchValue").get(0),decodeParams.get("gradationValue").get(0),decodeParams.get("subjectValue").get(0),
                decodeParams.get("exampapertypeValue").get(0),decodeParams.get("courseValue").get(0), "1",DczxUtil.timestamp());
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
        MyHttpResponse execute = MyHttpRequest.createGet(baseUrl)
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                // .form(param)
                .connectionKeepAlive()
                .execute();
        String rep = execute.body();
        dcInterfaceLog.setResponseBody(rep);
        dcInterfaceLog.setResult(true);
        JSONObject jsonObject = null;
        InitWorkListmcapiResponse initWorkListmcapiResponse = null;
        try {
            if (execute.isOk()) {
                initWorkListmcapiResponse = JSONUtil.toBean(rep, InitWorkListmcapiResponse.class);
                if (initWorkListmcapiResponse != null) {
                    if (StrUtil.isNotBlank(initWorkListmcapiResponse.getCode())) {
                        dcInterfaceLog.setResult(false);
                        dcInterfaceLog.setReason(jsonObject.getStr(DczxUtil.msg));
                    } else {
                        analyzeResponse(initWorkListmcapiResponse, decodeParams);
                    }
                }
                jsonObject = JSONUtil.parseObj(rep);
                String code = jsonObject.getStr(DczxUtil.code);
                if (StrUtil.isNotBlank(code)) {
                    dcInterfaceLog.setResult(false);
                    dcInterfaceLog.setReason(jsonObject.getStr(DczxUtil.msg));
                }
            } else {
                throw new IllegalArgumentException("网络错误：" + execute.getStatus());
            }
        } catch (JSONException e) {
            // 出错
            dcInterfaceLog.setResult(false);
            dcInterfaceLog.setReason("josn解析失败");
        } finally {
            dcInterfaceLog.setRemarks("采集单元作业记录列表线程");
            DczxUtil.saveInterfaceLog(dcInterfaceLog);
        }
        if (initWorkListmcapiResponse != null && dcInterfaceLog.getResult()) {
            // 解析保存
            // analyzeResponse(jsonObject, decodeParams);
            analyzeResponse(initWorkListmcapiResponse, decodeParams);
        }
    }

    /**
     * 解析单元作业记录列表
     */
    /*@Deprecated
    private void analyzeResponse(JSONObject jsonObject, Map<String, List<String>> decodeParams) {
        DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        LocalDateTime now = LocalDateTime.now();
        String courseName = jsonObject.getStr("courseName"); //经济学 B2001A
        JSONArray homeWorkList = jsonObject.getJSONArray("homeWorkList");

        // 几套试卷
        for (Object o1 : homeWorkList) {
            JSONObject homeWorkItem = (JSONObject) o1;
            String workerId = homeWorkItem.getStr("WORKER_ID");//27189340-c6c0-481d-b864-d0ed5e1b4087
            String workerName = homeWorkItem.getStr("WORKER_NAME");//经济学第三套作业（9~16单元）
            String workerScore = homeWorkItem.getStr("WORKER_SCORE");//95
            JSONArray examResultInfos = homeWorkItem.getJSONArray("examResultInfos");

            // 作业记录
            for (Object o2 : examResultInfos) {
                JSONObject obj = (JSONObject) o2;
                // 获取标识，校验是否已抓取过该记录，抓取
                String exampaperId = obj.getStr("exampaperId");  //b568b785-337a-4564-b4fc-7be7076063af
                String exampaperSeq = obj.getStr("exampaperSeq");    //18287386
                // 构造参数
                String pu_courseValue = decodeParams.get("courseValue").get(0);
                String pu_subjectValue = decodeParams.get("subjectValue").get(0);
                String pu_gradationValue = decodeParams.get("gradationValue").get(0);
                String pu_exampapertypeValue = decodeParams.get("exampapertypeValue").get(0);
                String u = "https://classroom.edufe.com.cn/HomeWorkHistoryPaper?tactic_id={}&courseCd={}&batchValue={}&gradationValue={}&paperName={}&subjectValue={}&exampapertypeValue={}&exampaperId={}&exampaperSeq={}";
                String formatUrl = StrUtil.format(u, workerId, pu_courseValue, pu_subjectValue, pu_gradationValue,
                        URLUtil.encode(URLUtil.encode(workerName)), pu_subjectValue, pu_exampapertypeValue, exampaperId, exampaperSeq);
                if (existsThis(formatUrl)) {
                    continue;
                }
                // 开始采集
                // DcInterfaceLog newDcInterfaceLog = ObjectUtil.clone(dcInterfaceLog);
                DcInterfaceLog newDcInterfaceLog = local.get();
                if (newDcInterfaceLog != null) {
                    newDcInterfaceLog.setUrl(formatUrl);
                    SaveDczxPaperQuestionThread saveDczxPaperQuestionThread = new SaveDczxPaperQuestionThread(newDcInterfaceLog);
                    SpringUtil.getBean(ThreadPoolTaskExecutor.class).submit(saveDczxPaperQuestionThread);
                }
            }
        }
    }*/

    private void analyzeResponse(InitWorkListmcapiResponse initWorkListmcapiResponse, Map<String, List<String>> decodeParams) {
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        LocalDateTime now = LocalDateTime.now();
        String courseName = initWorkListmcapiResponse.getCourseName(); //经济学 B2001A
        List<HomeWork> homeWorkList = initWorkListmcapiResponse.getHomeWorkList();
        // 几套试卷
        for (HomeWork homeWork : homeWorkList) {
            String workerId = homeWork.getWORKER_ID();//27189340-c6c0-481d-b864-d0ed5e1b4087
            String workerName = homeWork.getWORKER_NAME();//经济学第三套作业（9~16单元）
            List<ExamResultInfo> examResultInfos = homeWork.getExamResultInfos();
            // 作业记录
            for (ExamResultInfo examResultInfo : examResultInfos) {
                // 获取标识，校验是否已抓取过该记录，抓取
                String exampaperId = examResultInfo.getExampaperId();  //b568b785-337a-4564-b4fc-7be7076063af
                String exampaperSeq = examResultInfo.getExampaperSeq();    //18287386
                // 构造参数
                String pu_courseValue = decodeParams.get("courseValue").get(0);
                String pu_subjectValue = decodeParams.get("subjectValue").get(0);
                String pu_gradationValue = decodeParams.get("gradationValue").get(0);
                String pu_exampapertypeValue = decodeParams.get("exampapertypeValue").get(0);
                String u = "https://classroom.edufe.com.cn/HomeWorkHistoryPaper?tactic_id={}&courseCd={}&batchValue={}&gradationValue={}&paperName={}&subjectValue={}&exampapertypeValue={}&exampaperId={}&exampaperSeq={}";
                String formatUrl = StrUtil.format(u, workerId, pu_courseValue, pu_subjectValue, pu_gradationValue,
                        URLUtil.encode(URLUtil.encode(workerName)), pu_subjectValue, pu_exampapertypeValue, exampaperId, exampaperSeq);
                if (existsThis(formatUrl)) {
                    continue;
                }
                // 开始采集
                // DcInterfaceLog newDcInterfaceLog = ObjectUtil.clone(dcInterfaceLog);
                // DcInterfaceLog newDcInterfaceLog = threadLocal.get();
                DcInterfaceLog newDcInterfaceLog = JSONUtil.toBean(JSONUtil.toJsonStr(this.dcInterfaceLog), DcInterfaceLog.class);
                if (newDcInterfaceLog != null) {
                    newDcInterfaceLog.setUrl(formatUrl);
                    SaveDczxPaperQuestionThread saveDczxPaperQuestionThread = new SaveDczxPaperQuestionThread(newDcInterfaceLog);
                    SpringUtil.getBean(ThreadPoolTaskExecutor.class).submit(saveDczxPaperQuestionThread);
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
        return SpringUtil.getBean(IDcInterfaceLogService.class)
                .count(Wrappers.lambdaQuery(DcInterfaceLog.class).eq(DcInterfaceLog::getUrl, url)) > 0;
    }

}
