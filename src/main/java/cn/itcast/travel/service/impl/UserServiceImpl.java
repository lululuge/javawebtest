package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;

public class UserServiceImpl implements UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */

    // 创建dao对象
    UserDao dao = new UserDaoImpl();
    @Override
    public boolean regist(User user) {
        User u = dao.findUserByUsername(user.getUsername());
        if (u == null) {
            // 该用户名可用
            // 存储该用户到数据库中
            dao.save(user);
            return true;
        }
        else {
            // 该用户名不可用
            return false;
        }

    }
}
