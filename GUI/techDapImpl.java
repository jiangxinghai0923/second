package GUI;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class techDapImpl implements techDao<Stu,Integer> {
    private final Connection conn;
    private PreparedStatement pstmt;
    public techDapImpl(Connection conn){
        this.conn=conn;
    }
    @Override
    public int insert(Stu Stu) throws Exception {
           String sql="insert ignore into student (id,name,date,sex,phone) " +
                   "values(?,?,?,?,?)";
           pstmt=conn.prepareStatement(sql);
           int index=1;
           pstmt.setString(index++, String.valueOf(Stu.getId()));
           pstmt.setString(index++, Stu.getName());
           pstmt.setString(index++, Stu.getDate());
           pstmt.setString(index++, Stu.getSex());
           pstmt.setString(index++, Stu.getPhone());


           return pstmt.executeUpdate();

    }

    @Override
    public int insertBatch(List<Stu> list) throws SQLException {
        String sql = "insert ignore into student (id,name,date,sex,phone)  " +
                "values(?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 设置批处理模式
            conn.setAutoCommit(false);
            for (Stu t : list) {
                pstmt.setString(1, String.valueOf(t.getId()));
                pstmt.setString(2, t.getName());
                pstmt.setString(3, t.getDate());
                pstmt.setString(4, t.getSex());
                pstmt.setString(5, t.getPhone());

                pstmt.addBatch();
            }
            int[] rows = pstmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            int totalRow = 0;
            for (int i : rows) {
                totalRow += i;
            }
            return totalRow;
        } catch (SQLException e) {
            throw new SQLException("Error executing batch insert", e);
        }
    }

//根据主键更新数据
    @Override
    public int updateByPrimaryKey(Stu t) throws Exception {
        String sql = "update  Student set name=?, date=?,sex=?, phone=? where id=?";
        int updateCount = 0;
        pstmt = conn.prepareStatement(sql); // 设置参数
        pstmt.setString(1, t.getName());
        pstmt.setString(2, t.getDate());
        pstmt.setString(3, t.getSex());
        pstmt.setString(4, t.getPhone());
        pstmt.setString(5, String.valueOf(t.getId()));
         // 执行更新操作
         updateCount = pstmt.executeUpdate();
         return updateCount;
    }

    @Override
    public int updateByCondition(String condition, Map<String, Object> updateParams) throws Exception {
        // 创建一个字符串构建器，用于动态构建SQL语句
        StringBuilder sql = new StringBuilder("update Stu set ");
// 参数索引，用于设置PreparedStatement的参数
        int paramIndex = 1;
// 遍历更新参数映射，构建UPDATE语句的SET部分
        for (Map.Entry<String, Object> entry : updateParams.entrySet())
        { sql.append(entry.getKey()).append("=?, ");
        }
        // 去掉SET部分最后多余的逗号
        sql.delete(sql.length() - 2, sql.length());
// 在SQL语句末尾添加WHERE条件
        sql.append(" WHERE ").append(condition);
// 初始化更新记录数
        int updateCount = 0;
        pstmt = conn.prepareStatement(sql.toString());
// 遍历更新参数映射，为PreparedStatement设置参数
        for (Map.Entry<String, Object> entry : updateParams.entrySet())
        {
            pstmt.setObject(paramIndex++, entry.getValue()); }
// / 执行更新操作，并获取受影响的记录数
        updateCount = pstmt.executeUpdate();

        return  updateCount;
    }
//根据主键删除数据
    @Override
    public int deleteByPrimaryKey(Integer sno) throws Exception {
        String sql = "DELETE FROM student WHERE id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, String.valueOf(sno));
        return pstmt.executeUpdate();
    }
//根据主键删除多条数据
    @Override
    public int deleteByPrimaryKey(List<Stu> snoList) throws Exception {
        if (snoList.isEmpty()){
            return 0;
        }
        String sql = "DELETE FROM Student WHERE id IN ("
                + String.join(",", Collections.nCopies(snoList.size(), "?")) +")";
        pstmt=conn.prepareStatement(sql);
       int i=1;
        for (Stu t:snoList
             ) {
            pstmt.setString(i++, String.valueOf(t.getId()));
        }
        return pstmt.executeUpdate();
    }
//根据条件批量删除
    @Override
    public int deleteByCondition(String condition) throws Exception {
        String sql = "DELETE FROM Student WHERE " + condition;
        pstmt = conn.prepareStatement(sql);
        return pstmt.executeUpdate();
    }
//根据主键查询数据
    @Override
    public Stu selectByPrimaryKey(Integer id) throws Exception {
        String sql = "SELECT * FROM Student WHERE id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, String.valueOf(id));
//         设置参数，即学号
         ResultSet rs = pstmt.executeQuery();
//         执行查询，返回结果集
        if (rs.next())
         { // 解析结果集，构建学生对象并返回
         return new Stu(Integer.parseInt(rs.getString(1)), rs.getString(2),
                 rs.getString(3), rs.getString(4), rs.getString(5));
         }
        //未查询到 返回空
         return null;
    }
//查询返回所有数据
    @Override
    public List<Stu> selectAll() throws Exception {
        String sql = "SELECT * FROM Student";
        List<Stu> StuList = new ArrayList<>();
        pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
//         执行查询，返回结 果集
         while (rs.next()) {
//         解析结果集，构建学生对象并添加到列表中
         StuList.add(new Stu(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
         }
         return StuList;
    }
//根据条件查询数据
    @Override
    public List<Stu> selectByCondition(String condition) throws Exception {
        List<Stu> StuList = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE " + condition;
        pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            StuList.add(new Stu(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        return StuList;
    }
 //分页查询
    @Override
    public List<Stu> selectBypage(int pageIndex, int pageSize) throws Exception {
        // 计算偏移量
         int offset = (pageIndex - 1) * pageSize;
        // SQL 查询语句，使用 LIMIT 和 OFFSET 进行分页
         String sql = "SELECT * FROM student LIMIT ? OFFSET ?";
         pstmt = conn.prepareStatement(sql);
//         设置参数
          pstmt.setInt(1, pageSize);
         pstmt.setInt(2, offset);
        // 执行查询，返回结果集
         ResultSet rs = pstmt.executeQuery();
        // 解析结果集，构建学生对象列表
         List<Stu> StuList = new ArrayList<>();
         while (rs.next()) {
         StuList.add(new Stu(Integer.parseInt( rs.getString("id")), rs.getString("name"), rs.getString("date"),
                 rs.getString("sex"), rs.getString("phone")
                ));
         }
         return StuList;
}

    @Override
    public int getTotalCount() throws Exception {
        String sql="select * from student";
        pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        int count=0;
        while(resultSet.next()){
            count++;
        }
        return count;
    }

}

