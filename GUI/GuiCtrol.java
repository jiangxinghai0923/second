package GUI;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuiCtrol implements Initializable {

   private techDao<Stu, Integer> techDao;

    @FXML
    private TextField StutID;

    @FXML
    private RadioButton Sman;

    @FXML
    private RadioButton Swoman;

    @FXML
    private Button Querry;

    @FXML
    private Button add;

    @FXML
    private TableColumn<Stu, String> tDate;

    @FXML
    private TableColumn<Stu, String> tSex;

    @FXML
    private ToggleGroup sex;

    @FXML
    private DatePicker StuDate;

    @FXML
    private TextField StuPhone;

    @FXML
    private TableColumn<Stu, String> tID;

    @FXML
    private TableColumn<Stu, String> tPhone;

    @FXML
    private TextField StuName;

    @FXML
    private TableColumn<Stu, String> tName;

    @FXML
    private TableView<Stu> TableView;

    @FXML
    private Pagination pageTable;
    private int itemsPerPage = 11;//每页显示的数据条数

    @FXML
    private Button edit;


    @FXML
    void querry(ActionEvent event) throws IOException {
      newGui();
    }

    @FXML
    void addNew(ActionEvent event) throws Exception {
//        LocalDate value = StuDate.getValue();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try(Connection conn=DruidUtils.getConnection()){
            techDao=new techDapImpl(conn);
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            if(add.getText().equals("新增数据")){
                Stu stu=new Stu(Integer.parseInt(StutID.getText()),StuName.getText(),
                        StuDate.getValue().toString(),getSelectedGender(),StuPhone.getText());
            techDao.insert(stu);
            alert.setHeaderText("新增成功");
            alert.show();
            refreshTableView();
            }
            if(add.getText().equals("保存编辑")){
               Stu stu=new Stu(Integer.parseInt(StutID.getText()),StuName.getText(),StuDate.getValue().toString(),
                       getSelectedGender(),StuPhone.getText());
                   techDao.updateByPrimaryKey(stu);
                    alert.setHeaderText("编辑成功");
                    alert.show();
                    StutID.clear();
                    StuName.clear();
                    StuDate.setValue(null);
                    sex.selectToggle(null);
                    StuPhone.clear();
                    add.setText("新增数据");
                    refreshTableView();

            }
        }
    }

    //更新TableView中的数据
    private void updateTableView(List<Stu> students) {
        // 传入List集合，更新
        TableView.getItems().setAll(students);
        // 为每列设置单元格值工厂，以从学生对象中获取属性值显示。
        tID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        tPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    //从数据库中返回所有的学生信息
    private List<Stu> loadData() throws Exception {
        try (Connection conn = DruidUtils.getConnection()) {
            // 获取 数据库连接// 创建学生数据访问对象
            techDao = new techDapImpl(conn); //
            // 从数据库中查询所有学生数据
            return techDao.selectAll();
        } catch (Exception e) {
            // 异常处理，打印异常信息
            e.printStackTrace();
            // 返回空列表表示加载数据失败
            return null;
        }
    }


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePagination();
        loadPageData(0);
        // 设置TableView的多选模式
        TableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableView.setOnMouseClicked(event->{
            if(event.getClickCount()==2){
                editt(event);
            }
        });
    }
     @FXML
    public void btnDel(ActionEvent event) throws Exception {
         // 获取选定的多个行数据
         List<Stu> selectedStudents = new ArrayList<>(TableView.getSelectionModel().getSelectedItems());
         // 使用系统确认框询问
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setHeaderText("您确定要删除选定的学生信息吗？");
         // 显示对话框，并等待按钮返回
         Optional<ButtonType> result = alert.showAndWait();
         // 判断返回的按钮类型是确定还是取消，再据此分别进一步处理
         if (result.get() == ButtonType.OK) {
             try {
                 // 删除选定的学生信息
                 deleteSelectedStudents(selectedStudents);
                 //刷新数据表格
                 refreshTableView();
             } catch (Exception e) {
                 throw new RuntimeException(e);}
         }
             }
     @FXML
    public void editt(Event event) {
           add.setText("保存编辑");
          Stu stu1=TableView.getSelectionModel().getSelectedItem();
          StutID.setText(String.valueOf(stu1.getId()));
          StuName.setText(stu1.getName());
          StuDate.setValue(LocalDate.parse(stu1.getDate()));
          if(stu1.getSex().equals("男")){
              Sman.setSelected(true);
          }else{
              Swoman.setSelected(true);
          }
          StuPhone.setText(stu1.getPhone());
//          Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
//          alert.setHeaderText("您确定要编辑该学生的信息吗? ");
//          Optional<ButtonType> result=alert.showAndWait();
//          if(result.get()==ButtonType.OK){
//              try {
//                  updateSelectStudents(stu1);
//                  refreshTableView();
//              } catch (Exception e) {
//                  throw new RuntimeException(e);
//              }
//          }




     }

    //弹出新窗口
    private void newGui() throws IOException {
        Stage stage = new Stage();
        Parent load = FXMLLoader.load(getClass().getResource("/FXML/Btable.fxml"));
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Image/查询(1).png"))));
        stage.setScene(new Scene(load));
        stage.setTitle("查询数据");
        //设置窗口没关闭不能使用其他窗口
        stage.initModality(Modality.APPLICATION_MODAL);
        //设置窗口大小不能改变
        stage.setResizable(false);
        stage.show();

    }

    // 获取选中的单选按钮的值
    public String getSelectedGender() {
        RadioButton selectedRadioButton = (RadioButton) sex.getSelectedToggle();
        // 检查是否有选中的单选按钮
        if (selectedRadioButton != null) {
            // 返回选中的单选按钮的值
            return selectedRadioButton.getText();
        } else {
            // 如果没有选中的单选按钮，可以返回默认值或者空字符串，取决于你的需求
            return "未选择性别";
        }
    }

    private void deleteSelectedStudents(List<Stu> selectedStudents) throws Exception {
        try (Connection conn = DruidUtils.getConnection()) {
            // 创建学生数据访问对象
           techDao = new techDapImpl(conn);
            // 遍历选定的学生，逐个删除
            for (Stu student : selectedStudents) {
                techDao.deleteByPrimaryKey(student.getId());
                // } }
            }
        }
    }

    private void updateSelectStudents(Stu students) throws Exception{
        try (Connection conn = DruidUtils.getConnection()) {
            // 创建学生数据访问对象
            techDao = new techDapImpl(conn);
            techDao.updateByPrimaryKey(students);

        }
    }
    //初始化分页控件，设置总页数，并添加页码变化监听器。
     private void initializePagination() {
//     设置分页控件的页数
     pageTable.setPageCount(calculatePageCount());
    // 监听页码变化事件，当页码变化时刷新表格数据
     pageTable.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
    // 根据新的页码加载数据
     loadPageData(newValue.intValue()); });
    }

    //计算总页数，基于总记录数和每页显示的条数。
     private int calculatePageCount() {
    // 获取总记录数
     try (Connection connection = DruidUtils.getConnection()) {
         techDao= new techDapImpl(connection);
    // 计算总页数，并向上取整
     return (int) Math.ceil((double) techDao.getTotalCount() / itemsPerPage);
     } catch (Exception e) {
     e.printStackTrace();
     }
     return 0;
    }

    //加载指定页码的数据到TableView 中
     private void loadPageData(int pageIndex) {
     try (Connection connection = DruidUtils.getConnection()) {
      techDao = new techDapImpl(connection);
    // 获取每页数据
     List<Stu> pageData = techDao.selectBypage(pageIndex+1, itemsPerPage);
    // 将数据加载到 TableView 中
     updateTableView(pageData);
     } catch (Exception e) {
     e.printStackTrace();
     }
     }
    private void refreshTableView() throws Exception {
        updateTableView(loadData());
    }


}
