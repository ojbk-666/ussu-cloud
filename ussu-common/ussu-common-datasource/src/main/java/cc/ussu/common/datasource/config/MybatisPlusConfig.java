package cc.ussu.common.datasource.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.MySqlDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    /**
     * 插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 分页
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(1000L);
        paginationInnerInterceptor.setDialect(new MySqlDialect());
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 攻击 SQL 阻断解析器、加入解析链
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        // 乐观锁
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    /**
     * <a href="https://mybatis.plus/guide/optimistic-locker-plugin.html#_1-%E6%8F%92%E4%BB%B6%E9%85%8D%E7%BD%AE">乐观锁插件</a>
     * 特别说明:
     * 支持的数据类型只有:int,Integer,long,Long,Date,Timestamp,LocalDateTime
     * 整数类型下 newVersion = oldVersion + 1
     * newVersion 会回写到 vo 中
     * 仅支持 updateById(id) 与 update(vo, wrapper) 方法
     * 在 update(vo, wrapper) 方法下, wrapper 不能复用!!!
     */

}
