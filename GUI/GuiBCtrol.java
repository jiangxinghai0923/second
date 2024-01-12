package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class GuiBCtrol implements Initializable {
    private techDao<Stu,Integer> techDao;
    @FXML
    private ToggleGroup sexToggleGroup;

    @FXML
    private Button select;

    @FXML
    private TableColumn<Stu, String> btID;

    @FXML
    private TextField bName;

    @FXML
    private DatePicker bDate2;

    @FXML
    private DatePicker bDate1;

    @FXML
    private TableView<Stu> tableView;

    @FXML
    private TableColumn<Stu, String> btSex;

    @FXML
    private TableColumn<Stu, String> btPhone;

    @FXML
    private MenuButton bmenu;

    @FXML
    private RadioMenuItem sex1;

    @FXML
    private RadioMenuItem sex2;

    @FXML
    private TextField Bphone;

    @FXML
    private TextField bID;

    @FXML
    private TableColumn<Stu, String> btDate;

    @FXML
    private TableColumn<Stu, String> btName;

    @FXML
    private Label result;

    @FXML
    void sLike(ActionEvent event) throws Exception {
        try (Connection conn = DruidUtils.getConnection()) {
            techDao = new techDapImpl(conn);
            StringBuilder condition = new StringBuilder();
            String id = bID.getText();
            String name = bName.getText();
            String phone = Bphone.getText();
            String sex = getSelectedSex(); // 获取菜单按钮被选中的文本内容
            String date1 = bDate1.getValue() != null ? bDate1.getValue().toString() : null;
            String date2 = bDate2.getValue() != null ? bDate2.getValue().toString() : null;

            // 构建动态条件
            if (id != null && !id.isEmpty()) {
                condition.append("id like '%" + id + "%' AND ");
            }
            if (name != null && !name.isEmpty()) {
                condition.append("name like '%" + name + "%' and ");
            }
            if (phone != null && !phone.isEmpty()) {
                condition.append("phone like '%" + phone + "%' AND ");
            }
            if(date1!=null && date2==null){
                condition.append("date > '"+date1+"' and'");
            }
            if(date1==null&& date2!=null){
                condition.append("date < '"+date2+"' and'");
            }
            if (date1 != null && date2 != null) {
                condition.append("date BETWEEN '" + date1 + "' AND '" + date2 + "' AND ");
            }
            if (sex != null && !sex.isEmpty()) {
                condition.append("sex = '" + sex + "' AND ");
            }

            // 去除末尾的 "AND"
            if (condition.length() > 0) {
                condition.setLength(condition.length() - 4);
            }

             List<Stu> stus = techDao.selectByCondition(condition.toString());
             int count=0;
            for (int i = 0; i < stus.size(); i++) {
                count++;
            }
            String s="符合要求的共有[";
            result.setText(s+count+"]条");
             updateTableView(stus);
        }
    }

    // 辅助方法，获取选择的性别文本
    private String getSelectedSex() {
        RadioMenuItem selectedSex = (RadioMenuItem) sexToggleGroup.getSelectedToggle();
        if (selectedSex != null) {
            bmenu.setText(selectedSex.getText());
            return selectedSex.getText();
        }
        return null; // 没有选中时返回null，你可以根据实际情况修改
    }



    //更新TableView中的数据
    private void updateTableView(List<Stu> students) {
        // 传入List集合，更新
        tableView.getItems().setAll(students);
        // 为每列设置单元格值工厂，以从学生对象中获取属性值显示。
        btID.setCellValueFactory(new PropertyValueFactory<>("id"));
        btName.setCellValueFactory(new PropertyValueFactory<>("name"));
        btDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        btSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        btPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
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

    private String handleSexSelection(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String selectedSex = selectedItem.getText();
        return selectedSex;
    }

    private void updateMenuButtonText(MenuButton menuButton, String newText) {
        menuButton.setText(newText);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image icon = new Image(getClass().getResourceAsStream("/Image/查询(1).png"));
        select.setGraphic(new ImageView(icon));
        select.setStyle("-fx-background-color: green");


    }
}
