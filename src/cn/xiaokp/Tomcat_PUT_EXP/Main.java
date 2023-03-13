package cn.xiaokp.Tomcat_PUT_EXP;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //导入fxml
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
        //设置窗口标题
        primaryStage.setTitle("Tomcat_PUT_EXP V1.4  by xiaokp");
        //设置场景
        primaryStage.setScene(new Scene(root));
        //设置窗口不可调节大小
        primaryStage.setResizable(false);
        //显示窗口
        primaryStage.show();
    }
}
