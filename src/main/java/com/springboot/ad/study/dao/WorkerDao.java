package com.springboot.ad.study.dao;

import com.springboot.ad.study.entity.Worker;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <h1>Worker Dao 接口定义</h1>
 * Created by Qinyi.
 */
@SuppressWarnings("all")
public interface WorkerDao extends JpaRepository<Worker, Long> {

    /**
     * <h2>只要方法名称符合 JPA 的规范, 就不需要写 SQL 语句</h2>
     * 简单条件查询: 查询方法以 find | read | get | query | stream 开头, 它们都是同义词
     * delete | remove
     * */
    Worker findByName(String name);
    Worker getByName(String name);
    Worker readByName(String name);
    Worker queryByName(String name);
    Worker streamByName(String name);

    /**
     * <h2>多条件查询使用 And 进行连接</h2>
     * 条件的属性名称与个数要与参数的位置和个数一一对应
     * */
    Worker findByNameAndCity(String name, String city);

    /**
     * <h2>JPA 规范中定义了 MySQL 几乎所有的查询关键字</h2>
     * */
    Worker findByNameAndSalaryGreaterThanEqual(String name, Long salary);

    // 如果 JPA 规范定义的查询关键字不能满足我们的需求, 可以使用 @Query 自定义查询 SQL

    // 查找最大 id 的 Worker 对象
    @Query("SELECT w FROM Worker w WHERE id = (SELECT MAX(id) FROM Worker)")
    Worker getMaxIdWorker();

    // 如果查询有参数, 参数有两种不同的传递方式

    /**
     * <h2>第一种查询参数的传递方式</h2>
     * */
    @Query("SELECT w FROM Worker w WHERE name = ?1 AND salary >= ?2")
    List<Worker> findWorkerByFirstParam(String name, Long salary);

    /**
     * <h2>第二种查询参数的传递方式</h2>
     * */
    @Query("SELECT w FROM Worker w WHERE name = :name AND salary >= :salary")
    List<Worker> findWorkerBySecondParam(@Param("name") String name,
                                         @Param("salary") Long salary);

    /**
     * <h2>只查询实体表的部分字段</h2>
     * */
    @Query("SELECT new Worker (w.name, w.salary) FROM Worker w")
    List<Worker> getWorkerNameAndSalaryInfo();

    // 原生查询

    /**
     * <h2>通过原生查询获取 worker 表中的记录</h2>
     * */
    @Query(value = "SELECT * FROM worker", nativeQuery = true)
    List<Worker> findAllNativeQuery();

    /**
     * <h2>通过原生查询拿到部分字段结果</h2>
     * */
    @Query(value = "SELECT w.name, w.salary FROM worker w", nativeQuery = true)
    List<Map<String, Object>> getWorkerNameAndSalaryInfoByNativeQuery();

    @Modifying
    @Transactional(readOnly = false)
    @Query("UPDATE Worker SET salary = :salary WHERE name = :name")
    int updateSalaryByName(@Param("salary") Long salary,
                           @Param("name") String name);

    // 排序
    List<Worker> findAllBySalaryGreaterThanEqual(
            Long salary, Sort sort
    );
}
