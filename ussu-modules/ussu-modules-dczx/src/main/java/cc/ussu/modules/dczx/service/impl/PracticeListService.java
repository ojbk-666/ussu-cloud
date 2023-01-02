package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.model.vo.practice.InitTreeListmcapiResponse;
import cc.ussu.modules.dczx.model.vo.practice.Treek;
import cc.ussu.modules.dczx.properties.DczxProperties;
import cc.ussu.modules.dczx.service.IDcInterfaceLogService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@EnableAsync
@Service
public class PracticeListService {

    @Autowired
    private DczxProperties dczxProperties;
    @Autowired
    private PracticePaperQuestionService practicePaperQuestionService;
    @Autowired
    private IDcInterfaceLogService dcInterfaceLogService;

    /**
     * 获取随堂随练列表
     */
    @Async
    public InitTreeListmcapiResponse getPracticeList(DcInterfaceLog dcInterfaceLog) {
        // https://classroom.edufe.com.cn/Practice?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=1&courseValue=B0227A_1&courseId=B0227A
        String url = dcInterfaceLog.getUrl();
        if (DczxUtil.existsInterfaceLog(url)) {
            return null;
        }
        Map<String, String> param = HttpUtil.decodeParamMap(url, CharsetUtil.CHARSET_UTF_8);
        String batchValue = MapUtil.getStr(param, "batchValue");
        String gradationValue = MapUtil.getStr(param, "gradationValue");
        String subjectValue = MapUtil.getStr(param, "subjectValue");
        String exampapertypeValue = MapUtil.getStr(param, "exampapertypeValue");
        String courseValue = MapUtil.getStr(param, "courseValue");
        String courseId = MapUtil.getStr(param, "courseId");
        // https://classroom.edufe.com.cn/TKGL/initTreeListmcapi.action?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=1&courseValue=B0227A_1&customerId=1&_=1631859075203
        String reqUrl = StrUtil.format("https://classroom.edufe.com.cn/TKGL/initTreeListmcapi.action?batchValue={batchValue}&gradationValue={gradationValue}&subjectValue={subjectValue}&exampapertypeValue={exampapertypeValue}&courseValue={courseValue}&customerId=1", param);
        dcInterfaceLog.setReqUrl(reqUrl).setRemarks("采集随堂随练列表");
        String rep = HttpRequest.get(dcInterfaceLog.getReqUrl())
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                .execute().body();
        dcInterfaceLog.setResponseBody(rep);
        try {
            InitTreeListmcapiResponse initTreeListmcapiResponse = JSONUtil.toBean(rep, InitTreeListmcapiResponse.class);
            dcInterfaceLog.setResult(true);
            this.analyzePracticeList(initTreeListmcapiResponse, dcInterfaceLog, param);
            return initTreeListmcapiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            dcInterfaceLog.setResult(false).setReason(e.getMessage());
            return null;
        } finally {
            dcInterfaceLog.setId(null);
            // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
            dcInterfaceLogService.save(dcInterfaceLog);
        }
    }

    /**
     * 解析随堂随练
     *
     * @param initTreeListmcapiResponse
     */
    public void analyzePracticeList(InitTreeListmcapiResponse initTreeListmcapiResponse,DcInterfaceLog dcInterfaceLog, Map<String, String> param) throws InterruptedException {
        String courseName = initTreeListmcapiResponse.getCourseName();
        List<Treek> treekList = initTreeListmcapiResponse.getTreekList();
        for (Treek treek : treekList) {
            DcInterfaceLog dil = ObjectUtil.clone(dcInterfaceLog);
            initDcInterfaceLog(dil);
            dil.setUrl(getUrlFromTreek(treek, param));
            // 开始解析
            practicePaperQuestionService.collect(dil, courseName);
            Thread.sleep(100);
        }
    }

    /**
     * 获取baseurl
     *
     * @param treek
     * @return
     */
    private String getUrlFromTreek(Treek treek, Map<String, String> param) {
        // https://classroom.edufe.com.cn/PracticePaper?tactic_id=0af284eb-2a22-4cc2-8ba8-18e85b17a532&courseCd=B0227A_1&batchValue=202003&gradationValue=a23&paperName=%25E6%2594%25BE%25E4%25BB%25BB%25E7%25AE%25A1%25E7%2590%2586%25E9%2598%25B6%25E6%25AE%25B5%25E4%25B8%258E%25E5%25AE%259A%25E9%2587%258F%25E4%25BD%259C%25E4%25B8%259A%25E7%25AE%25A1%25E7%2590%2586%25E9%2598%25B6%25E6%25AE%25B5%25E2%2580%2594%25E2%2580%2594%25E6%25B3%25B0%25E5%258B%2592%25E5%2588%25B6&subjectValue=37&chapterId=042500010001&exampapertypeValue=1&courseId=B0227A
        String url = "https://classroom.edufe.com.cn/PracticePaper?tactic_id="+treek.getWORKER_ID()+"&courseCd={courseValue}&batchValue={batchValue}&gradationValue={gradationValue}&paperName="+ URLUtil.encode(URLUtil.encode(treek.getWORKER_NAME())) +"&subjectValue={subjectValue}&chapterId="+treek.getCHAPTER_ID()+"&exampapertypeValue={exampapertypeValue}&courseId={courseId}";
        return StrUtil.format(url, param);
    }

    /**
     * 初始化基础接口值
     *
     * @param dcInterfaceLog
     * @return
     */
    private DcInterfaceLog initDcInterfaceLog(DcInterfaceLog dcInterfaceLog) {
        dcInterfaceLog.setId(null).setResult(null).setReason(null).setRemarks(null).setResponseBody(null).setReqUrl(null).setUrl(null);
        return dcInterfaceLog;
    }

}
