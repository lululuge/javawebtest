package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    // 创建CategoryDao对象
    CategoryDao dao = new CategoryDaoImpl();

    /**
     * 查询所有分类条目
     * @return
     */
    @Override
    public List<Category> findAll() {
        // 从redis中查询
        // 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        List<Category> list = null;
        // 使用sortedSet排序查询
        Set<String> set = jedis.zrange("category", 0, -1);
        // 判断是否为空
        if (set == null || set.size() == 0) {
            System.out.println("从数据库查询");
            // redis中不存在，则从数据库中查询，并存入redis
            list = dao.findAll();
            // 遍历list
            for (int i = 0; i < list.size(); i++) {
                // 存入redis,数据格式为sortedSet
                jedis.zadd("category", list.get(i).getCid(), list.get(i).getCname());
            }
         }
        else {
            System.out.println("从redis中查询");
            // redis中存在，直接从redis中查询
            // 需要将set转为list
            list = new ArrayList<Category>();
            for (String name : set) {
                Category category = new Category();
                category.setCname(name);
                list.add(category);
            }
        }
        return list;
    }
}
