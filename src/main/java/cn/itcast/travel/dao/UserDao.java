package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 用户保存
     * @param user
     */
    void save(User user);

    /**
     * 根据激活码查询用户
     * @param code
     */
    User findUserByCode(String code);

    /**
     * 修改用户的status
     * @param user
     */
    void updateStatus(User user);
}
