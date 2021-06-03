package cn.ussu.modules.dczx.core.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 搜索基类
 *
 * @author liming
 * @date 2020-06-14 15:22
 */
public class BaseSearch<T> {

    @Autowired(required = false)
    protected ElasticsearchTemplate elasticsearchTemplate;

    private String getTId(T t, String idFieldName) {
        String fieldGetMethodName = "get" + StrUtil.upperFirst(StrUtil.toCamelCase(idFieldName));
        Object invoke = ReflectUtil.invoke(t, fieldGetMethodName);
        if (invoke instanceof Integer || invoke instanceof Float || invoke instanceof Double || invoke instanceof Boolean) {
            return String.valueOf(invoke);
        } else {
            return StrUtil.toString(invoke);
        }
    }

    /**
     * 创建索引
     *
     * @param indexName 索引名称
     */
    protected void createIndex(String indexName) {
        if (!elasticsearchTemplate.indexExists(indexName)) {
            elasticsearchTemplate.createIndex(indexName);
        }
    }

    /**
     * 保存单个对象到es
     */
    public String insert(T t, String idFieldName) {
        IndexQuery indexQuery = new IndexQueryBuilder().withId(getTId(t, idFieldName)).withObject(t).build();
        String index = elasticsearchTemplate.index(indexQuery);
        return index;
    }

    /**
     * 批量写入
     *
     * @param list 集合
     * @param idFieldName es中的id字段名,将会使用此此段执行get的反射方法获取值,字段名不应以is开头
     * @param indexName 索引名称
     * @param typeName 保存到哪个文档
     */
    public void insertBatch(Collection<T> list, String idFieldName, String indexName, String typeName) {
        int counter = 0;
        try {
            if (CollectionUtil.isEmpty(list)) return;
            String fieldGetMethodName = "get" + StrUtil.upperFirst(StrUtil.toCamelCase(idFieldName));
            // Method methodByName = ReflectUtil.getMethodByName(listClass, fieldGetMethodName);
            List<IndexQuery> queries = new ArrayList<>();
            for (T t : list) {
                //上面的那几步也可以使用IndexQueryBuilder来构建
                IndexQuery indexQuery = new IndexQueryBuilder()
                        .withId(ReflectUtil.invoke(t, fieldGetMethodName) + "")
                        .withObject(t)
                        .withIndexName(indexName)
                        .withType(typeName)
                        .build();
                queries.add(indexQuery);
                if (counter % 500 == 0) {
                    elasticsearchTemplate.bulkIndex(queries);
                    queries.clear();
                    System.out.println("bulkIndex counter : " + counter);
                }
                counter++;
            }
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries);
            }
            System.out.println("bulkIndex completed.");
        } catch (Exception e) {
            System.out.println("IndexerService.bulkIndex e;" + e.getMessage());
            throw e;
        }
    }

}
