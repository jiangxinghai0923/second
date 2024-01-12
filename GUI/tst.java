package GUI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class tst {
    public static void main(String[] args) throws Exception {
              try(Connection conn=DruidUtils.getConnection()){
                  techDao<Stu,Integer> techDao=new techDapImpl(conn);
                  RandomStuGenerator random=new RandomStuGenerator();
                  List<Stu> list=new ArrayList<>();
                  for (int i = 0; i <5 ; i++) {
                      Stu stu=random.generateRandomStu();
                      list.add(stu);
                  }
                  System.out.println(techDao.insertBatch(list));
              }
    }
}
