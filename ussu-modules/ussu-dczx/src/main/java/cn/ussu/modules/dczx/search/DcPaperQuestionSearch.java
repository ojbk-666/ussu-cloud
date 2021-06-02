package cn.ussu.modules.dczx.search;

import cn.hutool.core.collection.CollectionUtil;
import cn.ussu.modules.dczx.core.base.BaseSearch;
import cn.ussu.modules.dczx.entity.DcPaperQuestion;
import cn.ussu.modules.dczx.service.IDcPaperQuestionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 题目搜索类
 */
@Service
public class DcPaperQuestionSearch extends BaseSearch<DcPaperQuestion> {

    public static final String INDEX_NAME = "dczx";
    public static final String QUESTION_TYPE_NAME = "dcQuestion";
    @Deprecated
    public static final String QUESTION_OPTION_TYPE_NAME = "dcQuestionOption";

    @Autowired
    private IDcPaperQuestionService paperQuestionService;
    @Autowired
    private DcQuestionHightLightResultMapper dcQuestionHightLightResultMapper;

    /**
     * 写入es
     * @param questionId questionId
     */
    public void insertByQuestionId(String questionId) {
        List<DcPaperQuestion> list = paperQuestionService.findByQuestionIds(questionId);
        for (DcPaperQuestion item : list) {
            super.insert(item, "questionId");
        }
    }

    /**
     * 将数据刷新到es
     */
    public int refresh2ElasticSearch() {
        // 获取现有所有id
        QueryWrapper<DcPaperQuestion> qw = new QueryWrapper<>();
        qw.select("question_id");
        List<DcPaperQuestion> existsList = paperQuestionService.list(qw);
        List<String> existsIds = CollectionUtil.getFieldValues(existsList, "questionId", String.class);
        // 创建索引
        super.createIndex(INDEX_NAME);
        // 获取es中所有已有值
        NativeSearchQuery sq = new NativeSearchQueryBuilder()
                .withIndices(INDEX_NAME)
                .withTypes(QUESTION_TYPE_NAME)
                .withPageable(PageRequest.of(0, existsList.size()))
                .build();
        List<String> existsIdsInEs = elasticsearchTemplate.queryForIds(sq);
        existsIds.removeAll(existsIdsInEs);
        if (CollectionUtil.isEmpty(existsIds)) return 0;
        // 写入不存在的值 分次写入，每次写入1000条数据
        int everyCounts = 500;
        List<List<String>> needCounts = CollectionUtil.split(existsIds, everyCounts);
        System.out.println("es刷新，共 " + existsIds.size() + " 条数据，每次刷新 " + everyCounts + " 条，共 " + needCounts.size() + " 次");
        for (List<String> list : needCounts) {
            QueryWrapper<DcPaperQuestion> tempqw = new QueryWrapper<>();
            tempqw.in("question_id", list);
            // Collection<DcPaperQuestion> needs = paperQuestionService.list(tempqw);
            Collection<DcPaperQuestion> needs = paperQuestionService.findByQuestionIds(CollectionUtil.join(list, ",").split(","));
            super.insertBatch(needs, "questionId", INDEX_NAME, QUESTION_TYPE_NAME);
            System.gc();
        }
        return existsIds.size();
    }

    /**
     * 根据题目搜索
     *
     * @param keyword 关键词
     * @param page    页码，自动-1
     * @param limit   分页大小
     * @return
     * @author liming
     * @date 2020-08-26 12:40
     */
    /*public LayuiPageInfo searchEs(String keyword, Integer page, Integer limit) {
        if (page < 0) page = 1;
        page -= 1;  // 第一页页码为0
        String searchField = "questionTitle";
        QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery(keyword).field(searchField);
        String preTag = "<font color='red'>";
        String postTag = "</font>";
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                //查询条件
                .withQuery(qb)
                //分页
                .withPageable(PageRequest.of(page, limit))
                //排序
                // .withSort(SortBuilders.fieldSort("publishTime").order(SortOrder.DESC))
                //高亮字段显示
                .withHighlightFields(new HighlightBuilder.Field(searchField).preTags(preTag).postTags(postTag))
                .build();
        Page<DcPaperQuestion> p = elasticsearchTemplate.queryForPage(nativeSearchQuery, DcPaperQuestion.class, dcQuestionHightLightResultMapper);
        LayuiPageInfo pageInfo = LayuiPageFactory.createPageInfo(p);
        return pageInfo;
    }*/

}
