package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询所有分类条目
     * @return
     */
    @Override
    public List<Category> findAll() {
        try {
            String sql = "select * from tab_category";
            return template.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
        } catch (Exception e) {
            // 不打印错误信息
        }
        // 查询不到，返回null
        return null;
    }
}
