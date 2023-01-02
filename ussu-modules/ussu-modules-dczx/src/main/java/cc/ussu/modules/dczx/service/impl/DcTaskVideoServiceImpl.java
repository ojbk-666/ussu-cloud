package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcCourse;
import cc.ussu.modules.dczx.entity.DcTaskVideo;
import cc.ussu.modules.dczx.mapper.DcTaskVideoMapper;
import cc.ussu.modules.dczx.model.vo.videos.*;
import cc.ussu.modules.dczx.service.IDcCourseService;
import cc.ussu.modules.dczx.service.IDcTaskVideoService;
import cc.ussu.modules.dczx.util.VideoUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频信息 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-02 14:01:34
 */
@Slave
@Service
public class DcTaskVideoServiceImpl extends ServiceImpl<DcTaskVideoMapper, DcTaskVideo> implements IDcTaskVideoService {

    @Autowired
    private IDcCourseService courseService;

    @Override
    public List<DcTaskVideo> listByCourseVersion(String versionCode) {
        if (StrUtil.isBlank(versionCode)) {
            return new ArrayList<>();
        }
        return list(Wrappers.lambdaQuery(DcTaskVideo.class).eq(DcTaskVideo::getVersionCode, versionCode));
    }

    @Override
    public void saveVideoByStudyPlan(String ck, String userid, String courseId) {
        // 判断是否已有
        DcCourse dcCourse = courseService.getById(courseId);
        final int TIMEOUT = 3000;
        // 入口跳转页
        HttpResponse execute = HttpUtil.createGet(StrUtil.format("https://s1.edufe.com.cn/v1/myclassroom/redirectKczy?userId={}&courseId={}"
                        , userid, dcCourse.getServiceCourseVersId()))
                .cookie(ck).setFollowRedirects(false).timeout(TIMEOUT).execute();
        String header = execute.header(Header.LOCATION);
        HttpResponse execute1 = HttpUtil.createGet(header).timeout(TIMEOUT).execute();
        String header1 = execute1.header(Header.LOCATION);
        // 视频任务的cookie
        String cookieStr = execute1.getCookieStr();
        HttpResponse execute2 = HttpUtil.createGet(header1).cookie(cookieStr).execute();
        if (execute2.isOk()) {
            LmsStudyVO lmsStudyVO = VideoUtil.parse(execute2.body());
            // 解析数据
            List<DcTaskVideo> taskVideoList = new ArrayList<>();
            List<Chapter> chapterList = lmsStudyVO.getChapterList();
            chapterList.forEach(item -> {
                String chapterTitle = item.getTitle();
                List<Division> divisions = item.getDivisions();
                divisions.forEach(item2 -> {
                    String divisionCourseTitle = item2.getTitle();
                    List<DivisionCourse> divisionCourseList = item2.getCourseList();
                    divisionCourseList.forEach(item3 -> {
                        GoStudyParam goStudyParam = item3.getGoStudyParam();
                        if (item3.isVideo()) {
                            DcTaskVideo taskVideo = BeanUtil.toBean(goStudyParam, DcTaskVideo.class);
                            taskVideo.setChapterTitle(chapterTitle).setDivisionCourseTitle(divisionCourseTitle);
                            GetVideoJsonVO videoJson = VideoUtil.getVideoJson(goStudyParam, cookieStr);
                            if (videoJson != null) {
                                List<GetVideoJsonVO.Source> sources = videoJson.getSources();
                                GetVideoJsonVO.Source source = sources.get(0);
                                taskVideo.setVid(source.getVid()).setVideoUrl(source.getFile()).setImage(videoJson.getImage());
                                taskVideoList.add(taskVideo);
                            }
                        } else if (item3.isText()) {
                            // todo 浏览课件任务
                        } else if (item3.isHomework()) {
                        }
                    });
                });
            });
            List<DcTaskVideo> collect = taskVideoList.stream().filter(r -> StrUtil.isNotBlank(r.getVid())).collect(Collectors.toList());
            super.saveOrUpdateBatch(collect);
        }
    }
}
