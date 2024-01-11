package FMS;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.synth.SynthToolTipUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class StoreManagerGUI extends JFrame {
    private Object[][] data;
    private JTable table;
    private  String[] columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
    private static final String[] flower_columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
    private static final String[] supplies_columnNames = {"SupplyID", "Name", "Category", "Price", "Stock"};
    private static final String[] fertilizers_columnNames = {"FertilizerID", "Name", "Type", "Price", "Stock"};
    private static DefaultTableModel tableModel;
    private static String select_table="flower1";

    public static String getSelect_table() {
        return select_table;
    }

    private boolean selected1=false;
    private final TableModelListener tableModelListener;
    /*public static String[] getColumnNames() {
        return columnNames;
    }*/
    public static DefaultTableModel getTableModel() {
        return tableModel;
    }

    public StoreManagerGUI() throws SQLException, ClassNotFoundException {
        super("店长管理系统");
        setBounds(350,170,1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setBackground(Color.white);
        setVisible(true);
        setLayout(null);

        //输出默认
        try {
            data=new FlowerDatabase().FindAll(select_table);
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
        refreshTable();

        TextField textField = new TextField();
        textField.setFont(new Font("SansSerif",Font.PLAIN,20));
        textField.setBounds(140,28,170,30);
        add(textField);

        JLabel table_label = new JLabel("表:");
        table_label.setBounds(780,27,30,30);
        table_label.setFont(new Font("SansSerif",Font.PLAIN,20));
        add(table_label);

        String[] items={"花卉","花卉工具","肥料"};
        JComboBox<String> stringJComboBox = new JComboBox<>(items);
        stringJComboBox.setFont(new Font("SansSerif",Font.PLAIN,15));
        stringJComboBox.setSelectedItem(("花卉"));
        stringJComboBox.setBounds(820,28,150,30);
        stringJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> source= (JComboBox<String>) e.getSource();
                String selectedItem = (String) source.getSelectedItem();
                if (selectedItem.equals(items[0])){     //选择了花
                    columnNames=flower_columnNames;
                    select_table="flower1";
                    try {
                        data=new FlowerDatabase().FindAll(select_table);
                    } catch (SQLException | ClassNotFoundException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //System.out.println(select_table);

                    refreshTable();
                }else if(selectedItem.equals(items[1])){//选择了花卉工具
                    columnNames=supplies_columnNames;
                    select_table="supplies";
                    try {
                        data=new FlowerDatabase().FindAll(select_table);
                    } catch (SQLException | ClassNotFoundException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //System.out.println(select_table);
                    refreshTable();
                }else if(selectedItem.equals(items[2])){//选择了肥料
                    columnNames=fertilizers_columnNames;
                    select_table="fertilizers";
                    try {
                        data=new FlowerDatabase().FindAll(select_table);
                    } catch (SQLException | ClassNotFoundException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //System.out.println(select_table);
                    refreshTable();
                }
                textField.setText("");
            }
        });
        add(stringJComboBox);

        JLabel chooselabel = new JLabel("商品名：");
        chooselabel.setBounds(20,10,120,60);
        chooselabel.setFont(new Font("SansSerif",Font.PLAIN,25));
        add(chooselabel);

        //System.out.println(select_table);
        JButton choose = new JButton("查询");
        choose.setBounds(320,28,80,30);
        choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    data=new FlowerDatabase().Find(select_table,textField.getText());
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                refreshTable();
            }
        });
        add(choose);


        JButton addButton = new JButton("添加商品");
        addButton.setBounds(20,110,100,30);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Myaddflower();
                //refreshTable();
            }

        });
        add(addButton);

        JButton allButton = new JButton("查看全部");
        allButton.setBounds(140,110,100,30);
        allButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    data = new FlowerDatabase().FindAll(select_table);
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                refreshTable();
            }
        });
        add(allButton);

        JButton del_button = new JButton("删除商品");
        del_button.setBounds(260,110,100,30);
        del_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JPanel jPanel = new JPanel();
                int result = JOptionPane.showConfirmDialog(jPanel, "确定删除选中的商品吗？", "删除商品", JOptionPane.YES_NO_CANCEL_OPTION);

                if(result==JOptionPane.YES_OPTION){
                    int[] selectedRows = table.getSelectedRows();
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        int ID = (int) tableModel.getValueAt(selectedRows[i], 0);
                        String fieldID=null;
                        if (select_table.equals("flower1")){          //选择了花
                            fieldID="FlowerID";
                        }else if(select_table.equals("supplies")){    //选择了花卉工具
                            fieldID="SupplyID";
                        }else if(select_table.equals("fertilizers")){ //选择了肥料
                            fieldID="FertilizerID";
                        }
                        //System.out.println(flowerID);
                        try {
                            new FlowerDatabase().Delete(select_table,ID,fieldID);
                        } catch (SQLException | ClassNotFoundException | IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        tableModel.removeRow(selectedRows[i]);
                    }
                    tableModel.fireTableDataChanged();
                }else if(result==JOptionPane.NO_OPTION){

                }else if(result==JOptionPane.CANCEL_OPTION){

                }
                add(jPanel);

            }
        });
        add(del_button);


        // refreshTable();
        JCheckBox jCheckBox = new JCheckBox("修改模式");
        jCheckBox.setBounds(546,107,100,20);
        tableModelListener=new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //System.out.println(TableModelEvent.HEADER_ROW);
                if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                    // 表头改变事件，不处理
                    return;
                }

                int firstRow = e.getFirstRow();
                int column = e.getColumn();
                System.out.println("row"+firstRow+" "+"col"+column);
                if (firstRow > 0 && column > 0) {
                    Object oldvalueAt = tableModel.getValueAt(firstRow, column);
                    Object newvalueAt = tableModel.getValueAt(firstRow, column);
                    //System.out.println("old"+oldvalueAt+" "+"new"+newvalueAt);
                    try {
                        new FlowerDatabase().update(firstRow, column, oldvalueAt, newvalueAt,select_table);
                    } catch (SQLException | ClassNotFoundException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else if(column==0){

                }
            }

        };
        jCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = jCheckBox.isSelected();
                selected1=selected;
                if (selected){
                    tableModel.addTableModelListener(tableModelListener);
                }else {
                    tableModel.removeTableModelListener(tableModelListener);
                }
            }
        });
        add(jCheckBox);

        JButton countButton = new JButton("销售信息数据统计");
        countButton.setBounds(380,110,150,30);
        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BigDecimal totalPrice = new FlowerDatabase().count_sale_information();
                    JFrame jFrame = new JFrame("总销售额");
                    jFrame.setVisible(true);
                    jFrame.setLayout(null);
                    jFrame.setBounds(700,400,300,200);
                    jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    JLabel label = new JLabel(String.valueOf("总营业额："+totalPrice));
                    label.setBounds(50,30,200,80);
                    label.setFont(new Font("SansSerif",Font.PLAIN,20));
                    jFrame.add(label);
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(countButton);

        JButton excel_button = new JButton("导出excel");
        excel_button.setBounds(880,110,100,30);
        excel_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FlowerDatabase().into_excel(select_table);//导入excel
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(excel_button);

        /*ImageIcon imageIcon = new ImageIcon("src/resource/花店.jpg");
        JLabel label = new JLabel(imageIcon);
        label.setBounds(0,300,1000,700);
        add(label);*/

        revalidate();
        repaint();
    }

    public void fun(){
        System.out.println("asdfasdf");
    }

    public void refreshTable(){

            if (table == null) {
            tableModel = new DefaultTableModel(data, columnNames);
            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBounds(0, 150, 985, 500);
            add(scrollPane);
        } else {
            // 表格存在，更新数据
            tableModel = (DefaultTableModel) table.getModel();
            tableModel.setDataVector(data, columnNames);
            if(!selected1){
                tableModel.fireTableDataChanged();
            }
        }
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.lightGray);
        g.drawLine(0,130,getWidth(),130);
    }
}
class Myaddflower extends JFrame{
    public Myaddflower() {

        super("添加商品");
        setVisible(true);
        setLayout(null);
        setBounds(600,380,400,300);
        String selectTable = StoreManagerGUI.getSelect_table();
        //System.out.println("Myaddflower"+selectTable);
        String[] columnNames =null;
        String[] flower_columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
        String[] supplies_columnNames = {"SupplyID", "Name", "Category", "Price", "Stock"};
        String[] fertilizers_columnNames = {"FertilizerID", "Name", "Type", "Price", "Stock"};

        if (selectTable.equals("flower1")){          //选择了花
            columnNames=flower_columnNames;
        }else if(selectTable.equals("supplies")){    //选择了花卉工具
            columnNames=supplies_columnNames;
        }else if(selectTable.equals("fertilizers")){ //选择了肥料
            columnNames=fertilizers_columnNames;
        }

        for (int i = 0; i < columnNames.length; i++) {
            JLabel label = new JLabel(columnNames[i]);
            label.setBounds(40,i*30,80,50);
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
        JTextField textField6 = new JTextField();
        if(selectTable.equals("flower1")){
            textField6.setBounds(120,170,200,20);
            add(textField6);
        }

        JButton button1 = new JButton("完成");
        button1.setBounds(120,200,80,30);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object field1= Integer.parseInt(textField1.getText());
                Object field2=textField2.getText();
                Object field3=textField3.getText();
                Object field4=textField4.getText();
                Object field5= Integer.parseInt(textField5.getText());
                if(selectTable.equals("flower1")){
                    Object field6= Double.parseDouble(textField6.getText());
                    try {
                        new FlowerDatabase().Insert(selectTable,field1,field2,field3,field4,field5,field6);
                    } catch (ClassNotFoundException | SQLException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else {
                    try {
                        new FlowerDatabase().Insert(selectTable,field1,field2,field3,field4,field5,null);
                    } catch (ClassNotFoundException | SQLException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                dispose();

            }
        });
        add(button1);

        JButton button2 = new JButton("取消");
        button2.setBounds(240,200,80,30);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(button2);

        repaint();
    }
}
