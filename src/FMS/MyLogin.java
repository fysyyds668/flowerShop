package FMS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MyLogin extends JFrame{
    private static MyLogin mylogin;
    public static MyLogin getMylogin(){
        if(mylogin==null){
            mylogin=new MyLogin();
        }
        return mylogin;
    }
    private static int select=0;
    private MyLogin() {
        super("花店管理系统登录界面");
        this.setBounds(700,300,350,200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        Font font = new Font("SansSerif", Font.PLAIN, 15);
        setLayout(null);
        JLabel userLable=new JLabel("账号:");
        userLable.setBounds(40,20,80,25);
        userLable.setFont(font);
        add(userLable);

        JTextField userText=new JTextField(20);
        userText.setBounds(100,20,165,25);
        userText.setFont(new Font("SansSerif",Font.PLAIN,15));
        add(userText);

        JLabel passwordLabel=new JLabel("密码:");
        passwordLabel.setBounds(40,50,80,25);
        passwordLabel.setFont(font);
        add(passwordLabel);

        JPasswordField passwordtext=new JPasswordField(20);
        passwordtext.setBounds(100,50,165,25);
        passwordtext.setFont(new Font("SansSerif",Font.PLAIN,30));
        add(passwordtext);

        JRadioButton jRadioButton1 = new JRadioButton("店长");
        jRadioButton1.setSelected(true);
        jRadioButton1.setBounds(100,85,80,25);
        jRadioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select=0;//选择了店长
            }
        });
        //System.out.println(select);
        add(jRadioButton1);

        JRadioButton jRadioButton2 = new JRadioButton("店员");
        jRadioButton2.setBounds(215,85,80,25);
        jRadioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select=1;//选择了店员
            }
        });
        add(jRadioButton2);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);

        JButton loginbutton =new JButton("登录");
        loginbutton.setBounds(70,120,90,30);
        loginbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user=userText.getText();
                char[] password=passwordtext.getPassword();
                String password1=new String(password);
                boolean flag;

                if(select==0){
                    try {
                         flag = new FlowerDatabase().IsLogin_boss(user, password1);
                    } catch (SQLException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(flag) {
                        JOptionPane.showMessageDialog(null,"登录成功！","成功",JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("login success!");
                        try {
                            new StoreManagerGUI();
                        } catch (SQLException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(null,"账号或密码错误！","错误",JOptionPane.WARNING_MESSAGE);
                        System.out.println("login error!");
                    }

                }else if(select==1){
                    try {
                        flag = new FlowerDatabase().IsLogin_employ(user, password1);
                    } catch (SQLException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(flag) {
                        JOptionPane.showMessageDialog(null,"登录成功！","成功",JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("login success!");
                        try {
                            new ShopAssistantGUI();
                        } catch (SQLException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(null,"账号或密码错误！","错误",JOptionPane.WARNING_MESSAGE);
                        System.out.println("login error!");
                    }
                }
            }
        });
        add(loginbutton);

        JButton signbutton = new JButton("店员注册");
        signbutton.setBounds(180,120,90,30);
        signbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(select==1){
                    new Sign_up();
                }
            }
        });
        add(signbutton);
        repaint();

    }
}
class Sign_up extends JFrame{
    public Sign_up() {
        super("注册");
        setLayout(null);
        setBounds(700,300,400,300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        String[] columnNames ={"账号：","密码：","姓名：","性别：","年龄："};
        for (int i = 0; i < columnNames.length; i++) {
            JLabel label = new JLabel(columnNames[i]);
            label.setBounds(60,i*31,80,50);
            label.setFont(new Font("SansSerif", Font.PLAIN, 15));
            add(label);
        }

        JTextField textField1 = new JTextField();
        textField1.setBounds(120,20,200,20);
        add(textField1);
        JTextField textField2 = new JTextField();
        textField2.setBounds(120,50,200,20);
        add(textField2);
        JTextField textField3 = new JTextField();
        textField3.setBounds(120,80,200,20);
        add(textField3);
        JTextField textField4 = new JTextField();
        textField4.setBounds(120,110,200,20);
        add(textField4);
        JTextField textField5 = new JTextField();
        textField5.setBounds(120,140,200,20);
        add(textField5);

        JButton button1 = new JButton("完成");
        button1.setBounds(120,200,80,30);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user=textField1.getText();
                String password=textField2.getText();
                String name=textField3.getText();
                String sax=textField4.getText();
                int age= Integer.parseInt(textField4.getText());

                try {
                    boolean b = new FlowerDatabase().insert_sale_table(user, password, name, sax, age);
                    if(b){
                        JOptionPane.showMessageDialog(null,"注册成功！","成功",JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }else {
                        JOptionPane.showMessageDialog(null,"该账号已存在！","警告",JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        add(button1);

        JButton button2 = new JButton("取消");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        button2.setBounds(240,200,80,30);
        add(button2);

    }
}