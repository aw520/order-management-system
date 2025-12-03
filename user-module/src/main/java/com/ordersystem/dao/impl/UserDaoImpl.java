package com.ordersystem.dao.impl;

import com.ordersystem.constant.UserSchema;
import com.ordersystem.dao.UserDao;
import com.ordersystem.entity.User;
import com.ordersystem.utils.JDBCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public Optional<User> getUserById(Integer id) {

        JDBCUtils jdbcUtils = new JDBCUtils();
        String sql = "select * from User where user_id = ?";
        try{
            ResultSet rs = jdbcUtils.executeQuery(sql, id);
            if (rs.next()){
                User user = User.builder().userId(rs.getInt(UserSchema.ID))
                        .firstName(rs.getString(UserSchema.FIRST_NAME))
                        .lastName(rs.getString(UserSchema.LAST_NAME))
                        .email(rs.getString(UserSchema.EMAIL))
                        .password(rs.getString(UserSchema.PASSWORD)).build();
                return Optional.of(user);
            }
        }catch (SQLException e){
            log.error("fail to fetch order by id due to {}", e.getMessage());
        }finally {
            jdbcUtils.close();
        }
        return Optional.empty();
    }
}
