package com.asiainfo.springboot.jdbc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.springboot.jdbc.model.User;
import com.asiainfo.springboot.jdbc.route.Master;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午1:11:43
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate template;

    @Override
    public List<User> findAll() {
        return this.template.query("select * from users", new BeanPropertyRowMapper<User>(User.class));
    }

    @Master
    @Override
    public User getById(Long id) {
        return DataAccessUtils.singleResult(
                this.template.query("select * from users where id=?", new Object[] { id }, 
                        new BeanPropertyRowMapper<User>(User.class)));
    }

    @Override
    public User save(User user) {
        
        final String sql = "insert into users(username, password) values(?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        this.template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                return ps;
            }}, keyHolder);
        
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public void delete(Long id) {
        this.template.update("delete from users where id=?", new Object[] { id });
    }
}
