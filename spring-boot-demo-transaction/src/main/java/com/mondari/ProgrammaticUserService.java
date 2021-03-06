package com.mondari;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 编程式事务
 * </p>
 *
 * @author limondar
 * @date 2020/12/5
 */
@Slf4j
@Service("ProgrammaticUserService")
public class ProgrammaticUserService implements IUserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * TransactionTemplate 其实是使用 PlatformTransactionManager 来管理事务
     * <br>
     * PlatformTransactionManager 定义了事务的创建、提交和回滚三个状态
     */
    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    DataSource dataSource;

    @Override
    public List<Map<String, Object>> queryUserList() {
        return jdbcTemplate.queryForList("SELECT * FROM USER");
    }

    @Override
    public void insertUsers(String... usernames) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (String username : usernames) {
                    log.info("新增用户：{}", username);
                    jdbcTemplate.update("INSERT INTO USER(NAME) VALUES(?)", username);
                }
            }
        });
    }

    @Override
    public void truncateUser() {
        try (Connection connection = DataSourceUtils.getConnection(dataSource);
             PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE USER")) {
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            log.error("清空表数据失败：", throwables);
        }
    }
}
