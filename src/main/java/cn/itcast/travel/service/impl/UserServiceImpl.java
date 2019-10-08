package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

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
            // 设置该用户的激活状态和激活码（唯一）
            user.setStatus("N");
            user.setCode(UuidUtil.getUuid());
            dao.save(user);
            // 发送邮件
            MailUtils.sendMail("18736791805@163.com",
                    "<a href='http://localhost:8080/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>",
                    "激活邮件");
            return true;
        }
        else {
            // 该用户名不可用
            return false;
        }

    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        // 根据激活码查找对应的用户
        User user = dao.findUserByCode(code);
        if (user != null) {
            // 修改此用户的status为Y
            dao.updateStatus(user);
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * 用户登录
     * @param loginUser
     * @return
     */
    @Override
    public User login(User loginUser) {
        User user = dao.findUserByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());
        return user;
    }
}
