package FMS;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShopAssistantGUI extends JFrame {
    private Object[][] data;
    private Object[][] sale_data;
    private JTable table;
    private JTable sale_table;
    //private static final String[] columnNames = {"FlowerID", "花名", "花卉用品","肥料","库存","单价"};
    private static final String[] sale_columnNames = {"商品编号", "名称", "数量", "单价", "总价", "日期"};
    private String[] columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
    private static final String[] flower_columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
    private static final String[] supplies_columnNames = {"SupplyID", "Name", "Category", "Price", "Stock"};
    private static final String[] fertilizers_columnNames = {"FertilizerID", "Name", "Type", "Price", "Stock"};
    private static DefaultTableModel tableModel;
    private static DefaultTableModel saletableModel;
    private static String select_table = "flower1";
    private TableModelListener tableModelListener;
    private BigDecimal totalprice = BigDecimal.valueOf(0);

    public BigDecimal getTotalprice() {
        return totalprice;
    }

    public ShopAssistantGUI() throws SQLException, ClassNotFoundException, IOException {
        super("店员管理销售信息");
        setVisible(true);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 170, 1000, 700);

        data = new FlowerDatabase().FindAll(select_table);
        refreshTable();
        refreshSaleTable();

        Font labelfont = new Font("SansSerif", Font.PLAIN, 20);
        Font textfont = new Font("SansSerif", Font.PLAIN, 17);
        JLabel label1 = new JLabel("商品名称：");
        label1.setBounds(10, 0, 130, 50);
        label1.setFont(labelfont);
        add(label1);
        JTextField textField = new JTextField();
        textField.setBounds(115, 14, 170, 28);
        textField.setFont(textfont);
        add(textField);

        String[] items = {"花卉", "花卉工具", "肥料"};
        JComboBox<String> stringJComboBox = new JComboBox<>(items);
        stringJComboBox.setFont(new Font("SansSerif", Font.PLAIN, 15));
        stringJComboBox.setSelectedItem(("花卉"));
        stringJComboBox.setBounds(820, 14, 130, 30);
        stringJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> source = (JComboBox<String>) e.getSource();
                String selectedItem = (String) source.getSelectedItem();
                if (selectedItem.equals(items[0])) {     //选择了花
                    columnNames = flower_columnNames;
                    select_table = "flower1";
                    try {
                        data = new FlowerDatabase().FindAll(select_table);
                    } catch (SQLException | ClassNotFoundException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //System.out.println(select_table);

                    refreshTable();
                } else if (selectedItem.equals(items[1])) {//选择了花卉工具
                    columnNames = supplies_columnNames;
                    select_table = "supplies";
                    try {
                        data = new FlowerDatabase().FindAll(select_table);
                    } catch (SQLException | ClassNotFoundException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //System.out.println(select_table);
                    refreshTable();
                } else if (selectedItem.equals(items[2])) {//选择了肥料
                    columnNames = fertilizers_columnNames;
                    select_table = "fertilizers";
                    try {
                        data = new FlowerDatabase().FindAll(select_table);
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


        JButton checkbutton = new JButton("查询");
        checkbutton.setBounds(300, 14, 80, 28);
        checkbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    data = new FlowerDatabase().Find(select_table, textField.getText());
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                refreshTable();
            }
        });
        add(checkbutton);


        JLabel userlabel = new JLabel("用户购买信息：");
        userlabel.setBounds(10, 250, 150, 50);
        userlabel.setFont(labelfont);
        add(userlabel);

        JLabel shoplist = new JLabel("购物清单：");
        shoplist.setBounds(300, 250, 150, 50);
        shoplist.setFont(labelfont);
        add(shoplist);

        JLabel idlabel = new JLabel("商品编号：");
        idlabel.setBounds(10, 280, 100, 50);
        idlabel.setFont(labelfont);
        add(idlabel);

        JTextField usershopID = new JTextField();
        usershopID.setBounds(120, 295, 170, 25);
        usershopID.setFont(textfont);
        add(usershopID);

        JLabel numlabel = new JLabel("商品数量：");
        numlabel.setBounds(10, 345, 100, 50);
        numlabel.setFont(labelfont);
        add(numlabel);

        JTextField usershopnum = new JTextField();
        usershopnum.setBounds(120, 360, 170, 25);
        usershopnum.setFont(textfont);
        add(usershopnum);

        JLabel totalpricelabel = new JLabel("总价：" + totalprice);
        totalpricelabel.setBounds(800, 450, 200, 50);
        totalpricelabel.setFont(labelfont);
        add(totalpricelabel);

        JButton addbutton = new JButton("添加");
        addbutton.setBounds(10, 420, 130, 35);
        ArrayList<Object[]> row = new ArrayList<>();
        addbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((!usershopID.getText().isEmpty()) && (!usershopnum.getText().isEmpty()))
                    try {
                        Object[] rowData = new FlowerDatabase().SaleFind(Integer.parseInt(usershopID.getText()), Integer.parseInt(usershopnum.getText()), select_table);
                        row.add(rowData);

                        totalprice = totalprice.add((BigDecimal) rowData[4]);
                        //String totalstring=String.format("%.2f",totalprice);
                        totalpricelabel.setText("总价：" + totalprice);
                        add(totalpricelabel);

                        sale_data = row.toArray(new Object[0][0]);
                        refreshSaleTable();
                        usershopID.setText("");
                        usershopnum.setText("");

                    } catch (SQLException | ClassNotFoundException | IOException ex) {
                        throw new RuntimeException(ex);
                    }

            }
        });
        add(addbutton);

        sale_data = row.toArray(new Object[0][0]);

        JButton endbutton = new JButton("结算");
        endbutton.setBounds(158, 420, 130, 35);
        endbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < sale_data.length; i++) {
                    try {
                        new FlowerDatabase().saleinsert(sale_data[i]);
                        //System.out.println("saledata:"+saledata[i][3]);
                        //totalprice = totalprice + ((double) sale_data[i][3] * (int) sale_data[i][2]);
                        //System.out.println("total:"+totalprice);
                        //System.out.println((int)saledata[i][0]+(int)saledata[i][2]);
                        new FlowerDatabase().saleupdate(sale_data[i]);
                    } catch (SQLException | ClassNotFoundException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    row.clear();
                    totalprice = BigDecimal.valueOf(0);
                }

                sale_data = new Object[0][0];
                //totalprice=0;
                try {
                    data = new FlowerDatabase().Find("flower1", textField.getText());
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                refreshTable();
                refreshSaleTable();
                String totalstring = String.format("%.2f", totalprice);
                totalpricelabel.setText("总价：" + totalstring);
                //add(totalpricelabel);

            }
        });
        add(endbutton);

        revalidate();
        repaint();
    }

    public void refreshTable() {

        if (table == null) {
            tableModel = new DefaultTableModel(data, columnNames);
            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBounds(0, 50, 985, 200);
            add(scrollPane);
        } else {
            // 表格存在，更新数据
            tableModel = (DefaultTableModel) table.getModel();
            tableModel.setDataVector(data, columnNames);
            tableModel.fireTableDataChanged();

        }
    }

    public void refreshSaleTable() {
        if (sale_table == null) {
            saletableModel = new DefaultTableModel(sale_data, sale_columnNames);
            sale_table = new JTable(saletableModel);
            JScrollPane jScrollPane = new JScrollPane(sale_table,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            jScrollPane.setBounds(300, 295, 680, 162);
            add(jScrollPane);
        } else {
            saletableModel = (DefaultTableModel) sale_table.getModel();
            saletableModel.setDataVector(sale_data, sale_columnNames);
            saletableModel.fireTableDataChanged();
        }
    }


    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.lightGray);
        g.drawLine(0, 290, getWidth(), 290);
        g.drawLine(0, 320, getWidth(), 320);
        //refreshTable();
    }
}
/*
class Checkout extends JFrame{
    public Checkout() throws HeadlessException, SQLException, ClassNotFoundException {
        super("结算");
        setVisible(true);
        setBounds(700,400,300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel();
        BigDecimal totalprice = new ShopAssistantGUI().getTotalprice();
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        label.setBounds(100,100,80,50);
        label.setFont(font);
        label.setText(String.valueOf(totalprice));
        add(label);
        //dispose();
    }
}
*/
