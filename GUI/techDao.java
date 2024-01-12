package GUI;

import jdbc.Tech1;

import java.util.List;
import java.util.Map;

public interface techDao<T extends Entity,K>{
    //插入一条数据
    int insert(T tech1) throws Exception;
    //插入一批数据
    int insertBatch(List<T> list) throws Exception;
    //根据主键更新数据
    int updateByPrimaryKey(T tech1) throws Exception;
    //根据条件更新数据
    int updateByCondition(String condition, Map<String, Object> updateParams) throws Exception;
    //根据主键删除单条数据
    int deleteByPrimaryKey(K sno) throws Exception;
    //根据主键删除多条数据
    int deleteByPrimaryKey(List<T> snoList) throws Exception;
    //根据条件批量删除数据
    int deleteByCondition(String condition) throws Exception;
    //根据主键查询数据
    Stu selectByPrimaryKey(K id) throws Exception;
    //查询所有数据
    List<T> selectAll() throws Exception;
    //根据条件查询数据
    List<T> selectByCondition(String condition) throws Exception;
    //分页查询数据
    List<T> selectBypage(int pageIndex,int pageSize) throws  Exception;

    int getTotalCount() throws Exception;
}
