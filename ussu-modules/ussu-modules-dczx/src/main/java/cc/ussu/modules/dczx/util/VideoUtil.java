package cc.ussu.modules.dczx.util;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.dczx.entity.DcTaskVideo;
import cc.ussu.modules.dczx.model.vo.videos.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public final class VideoUtil {

    public static final String COURSE_STUDY = "courseStudy";
    public static final String CHAPTER = "chapter";
    public static final String H3 = "h3";
    public static final String DIVISIONS = "divisions";
    public static final String SPAN = "span";
    public static final String UL = "ul";
    public static final String LI = "li";
    public static final String DONE = "done";
    public static final String HALF = "half";
    public static final String TODO = "todo";
    public static final String A = "a";
    public static final String TITLE = "title";
    public static final String TEXT = "text";
    public static final String VIDEO = "video";
    public static final String HOMEWORK = "homework";
    public static final String VERSION_CODE = "versionCode";
    public static final String HOMEWORK_FLAG = "homeworkFlag";

    public static final int TIMEOUT = 3000;
    public static final int VIDEO_FINISH_PROGRESS = -999;

    /**
     * 获取视频cookie
     */
    public static VideoCookieDTO getVideoCookie(String jsessionid,String userid, String serviceCourseVersId) {
        // 入口跳转页
        String url = StrUtil.format("https://s1.edufe.com.cn/v1/myclassroom/redirectKczy?userId={}&courseId={}", userid, serviceCourseVersId);
        // HttpResponse execute = HttpUtil.createGet(url).cookie(jsessionid).setFollowRedirects(false).timeout(TIMEOUT).execute();
        MyHttpResponse execute = MyHttpRequest.createGet(url).disableCookie().cookie(jsessionid).disableRedirect().execute();
        // 第一次跳转的url
        String header = execute.header(Header.LOCATION);
        // HttpResponse execute1 = HttpUtil.createGet(header).timeout(TIMEOUT).execute();
        MyHttpResponse execute1 = MyHttpRequest.createGet(header).disableRedirect().execute();
        // 第二次重定向的url
        String location = execute1.header(Header.LOCATION);
        // 视频任务的cookie
        String cookieStr = execute1.getCookieStr();
        VideoCookieDTO cookieDTO = new VideoCookieDTO();
        cookieDTO.setLocation(location);
        cookieDTO.setCookieStr(cookieStr);
        return cookieDTO;
    }

    @Data
    public static class VideoCookieDTO {
        private String location;
        private String cookieStr;
    }

    /**
     * 获取开始学习点击后的页面 即 视频任务列表页面
     */
    public static String getVideoListPage(VideoCookieDTO param) {
        MyHttpResponse execute = MyHttpRequest.createGet(param.getLocation())
                .disableCookie().cookie(param.getCookieStr())
                .connectionKeepAlive().execute();
        if (execute.isOk()) {
            return execute.body();
        } else {
            throw new IllegalArgumentException("获取视频任务页面失败");
        }
    }

    /**
     * 解析表单
     *
     * @param html
     * @return
     */
    public static LmsStudyVO parse(String html) {
        Document document = Jsoup.parse(html);
        Element body = document.body();
        String versionCode = body.getElementById(VERSION_CODE).val();
        String homeworkFlag = body.getElementById(HOMEWORK_FLAG).val();
        Element courseStudy = body.getElementById(COURSE_STUDY);
        Elements chapters = courseStudy.getElementsByClass(CHAPTER);
        List<Chapter> chapterList = new ArrayList<>();
        for (Element chapterElement : chapters) {
            Chapter chapter = new Chapter();
            Elements title = chapterElement.getElementsByTag(H3);
            if (!title.isEmpty()) {
                String titleValue = title.get(0).text();
                chapter.setTitle(titleValue);
            }
            // divisions 只有一个
            Elements divisionsElements = chapterElement.getElementsByClass(DIVISIONS);
            List<Division> divisions = new ArrayList<>();
            Element firstDivisionsElement = divisionsElements.get(0);
            Elements children = firstDivisionsElement.children();
            for (Element li : children) {
                Division division = new Division();
                Elements spans = li.getElementsByTag(SPAN);
                if (!spans.isEmpty()) {
                    String text = spans.get(0).text();
                    division.setTitle(text);
                }
                division.setDone(li.hasClass(DONE));
                division.setHalf(li.hasClass(HALF));
                division.setTodo(li.hasClass(TODO));
                Elements courseListElements = li.getElementsByClass("course-list");
                if (!courseListElements.isEmpty()) {
                    Element ele = courseListElements.get(0);
                    Elements li1s = ele.getElementsByTag(LI);
                    List<DivisionCourse> divisionCourseList = new ArrayList<>();
                    for (Element li1 : li1s) {
                        Elements as = li1.getElementsByTag(A);
                        Element a = as.get(0);
                        String onclick = a.attr("onclick");
                        String aTitle = a.attr(TITLE);
                        boolean isText = a.hasClass(TEXT);
                        boolean isVideo = a.hasClass(VIDEO);
                        boolean isHomework = a.hasClass(HOMEWORK);
                        boolean textDone = a.hasClass("text-done");
                        boolean videoDone = a.hasClass("video-done");
                        boolean homeworkDone = a.hasClass("homework-done");
                        boolean videoCurrent = a.hasClass("video-current");
                        String replace = onclick.replace("goStudy(", StrUtil.EMPTY).replace(")", StrUtil.EMPTY);
                        String[] split = replace.split(StrUtil.COMMA);
                        GoStudyParam param = GoStudyParam.builder()
                                .chapterId(Integer.parseInt(split[0]))
                                .subChapterId(Integer.parseInt(split[1]))
                                .serviceId(Integer.parseInt(split[2]))
                                .serviceType(Integer.parseInt(split[3]))
                                .studyProgress(split[4].replaceAll("'", StrUtil.EMPTY))
                                .versionCode(versionCode)
                                .build();
                        DivisionCourse divisionCourse = DivisionCourse.builder()
                                .title(aTitle)
                                .goStudyParam(param)
                                .isText(isText)
                                .isVideo(isVideo)
                                .isHomework(isHomework)
                                .textDone(textDone)
                                .videoDone(videoDone)
                                .homeworkDone(homeworkDone)
                                .build();
                        divisionCourseList.add(divisionCourse);
                    }
                    division.setCourseList(divisionCourseList);
                }
                divisions.add(division);
            }
            chapter.setDivisions(divisions);
            chapterList.add(chapter);
        }
        return LmsStudyVO.builder().versionCode(versionCode).homeworkFlag(homeworkFlag).chapterList(chapterList).build();
    }

    /**
     * 获取视频地址
     *
     * @param param
     * @param cookieStr
     * @return
     */
    public static GetVideoJsonVO getVideoJson(GoStudyParam param, String cookieStr) {
        Map<String, Object> form = BeanUtil.beanToMap(param);
        form.put("t", new Date().getTime());
        MyHttpResponse execute = MyHttpRequest.createGet(StrUtil.format("http://kczy.study.edufe.com.cn/lms-study/getVideoJson?versionCode={versionCode}&chapterId={chapterId}&subChapterId={subChapterId}&serviceId={serviceId}&serviceType={serviceType}&_={t}", form))
                .accept(ContentType.JSON.getValue())
                .disableCookie().cookie(cookieStr)
                .connectionKeepAlive().execute();
        if (execute.isOk()) {
            GetVideoJsonVO getVideoJsonVO = JSONUtil.toBean(execute.body(), GetVideoJsonVO.class);
            return getVideoJsonVO;
        }
        return null;
    }

    /**
     * 更新视频进度
     *
     * @param dcTaskVideo
     * @param progress
     * @param cookieStr
     * @return
     */
    public static boolean updateVideoProgress(DcTaskVideo dcTaskVideo, Integer progress, String cookieStr) {
        StringBuffer sb = new StringBuffer();
        sb.append("http://kczy.study.edufe.com.cn/lms-study/updateState?versionCode=").append(dcTaskVideo.getVersionCode())
                .append("&chapterId=").append(dcTaskVideo.getChapterId())
                .append("&subChapterId=").append(dcTaskVideo.getSubChapterId())
                .append("&serviceId=").append(dcTaskVideo.getServiceId())
                .append("&serviceType=").append(dcTaskVideo.getServiceType())
                .append("&studyProgress=").append(progress)
                .append("&_=").append(new Date().getTime());
        try {
            MyHttpResponse execute = MyHttpRequest.createGet(sb.toString())
                    .disableCookie().cookie(cookieStr)
                    .connectionKeepAlive().execute();
            return execute.isOk();
        } catch (Exception e) {
            log.info("更新视频进度接口调用失败：" + e.getMessage());
        }
        return false;
    }

    /**
     * 完成阅读课件任务
     *
     * @param param
     * @param cookieStr
     * @return
     */
    public static boolean showViewPage(GoStudyParam param, String cookieStr) {
        StringBuffer sb = new StringBuffer();
        sb.append("http://kczy.study.edufe.com.cn/lms-study/emptyCss/showViewPage?versionCode=").append(param.getVersionCode())
                .append("&chapterId=").append(param.getChapterId())
                .append("&subChapterId=").append(param.getSubChapterId())
                .append("&serviceId=").append(param.getServiceId())
                .append("&serviceType=").append(param.getServiceType())
                .append("&_=").append(new Date().getTime());
        try {
            MyHttpResponse execute = MyHttpRequest.createGet(sb.toString())
                    .disableCookie().cookie(cookieStr)
                    .connectionKeepAlive().execute();
            return execute.isOk();
        } catch (Exception e) {
            log.info("完成阅读课件任务接口调用失败：" + e.getMessage());
        }
        return false;
    }

}
