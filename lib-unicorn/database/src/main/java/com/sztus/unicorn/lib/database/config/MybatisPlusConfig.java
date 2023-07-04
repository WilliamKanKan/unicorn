package com.sztus.unicorn.lib.database.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.sztus.unicorn.lib.database.component.injector.CustomizedSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LuffyWang
 */
@Configuration
public class MybatisPlusConfig {

  /**
   * @return mybatis plus 拦截器
   */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {

    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

    /*
     * 防全表更新与删除拦截器，针对 update 和 delete 语句 作用: 阻止恶意的全表更新删除
     */
    interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

    /*
     * 当要更新一条记录的时候，希望这条记录没有被别人更新
     * 乐观锁实现方式：
     *
     * 取出记录时，获取当前 version
     * 更新时，带上这个 version
     * 执行更新时， set version = newVersion where version = oldVersion
     * 如果 version 不对，就更新失败
     */
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

    /*
     * 分页插件, 如果不配置，分页插件将不生效
     * 指定数据库类型为mysql
     */
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }

  /**
   * 自定义sql注入器
   * @return sql注入器
   */
  @Bean
  public ISqlInjector iSqlInjector() {
    return new CustomizedSqlInjector();
  }


}