package FMS;

import org.apache.poi.ss.formula.SheetRangeAndWorkbookIndexFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.table.DefaultTableModel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class FlowerDatabase {
    private final String url="jdbc:mysql://127.0.0.1:3306/florist";
    private final String user="root";
    private final String password="@twd20040401.";
    private final Connection connection = DriverManager.getConnection(url,user,password);
    private final XSSFWorkbook workbook = new XSSFWorkbook();
    public FlowerDatabase() throws ClassNotFoundException, SQLException {
    }

    public Object[][] FindAll(String tableName) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql="select * from "+tableName;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //preparedStatement.setObject(1,tableName);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Object> datalist = new ArrayList<>();
        String[] columnNames=null;
        String[] flower_columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
        String[] supplies_columnNames = {"SupplyID", "Name", "Category", "Price", "Stock"};
        String[] fertilizers_columnNames = {"FertilizerID", "Name", "Type", "Price", "Stock"};

        if (tableName.equals("flower1")){     //选择了花
            columnNames=flower_columnNames;
        }else if(tableName.equals("supplies")){//选择了花卉工具
            columnNames=supplies_columnNames;
        }else if(tableName.equals("fertilizers")){//选择了肥料
            columnNames=fertilizers_columnNames;
        }
        while(resultSet.next()){
            Object[] rowData=new Object[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                rowData[i]=resultSet.getObject(columnNames[i]);
            }
            datalist.add(rowData);
        }
        Object[][] objects = datalist.toArray(new Object[0][0]);
        return objects;
    }

    public Object[][] Find(String tableName,String Name) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql="select * from "+tableName+" where Name like ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //preparedStatement.setObject(1,tableName);
        preparedStatement.setObject(1,"%"+Name+"%");
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Object> datalist = new ArrayList<>();
        String[] columnNames=null;
        String[] flower_columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
        String[] supplies_columnNames = {"SupplyID", "Name", "Category", "Price", "Stock"};
        String[] fertilizers_columnNames = {"FertilizerID", "Name", "Type", "Price", "Stock"};


        if (tableName.equals("flower1")){          //选择了花
            columnNames=flower_columnNames;
        }else if(tableName.equals("supplies")){    //选择了花卉工具
            columnNames=supplies_columnNames;
        }else if(tableName.equals("fertilizers")){ //选择了肥料
            columnNames=fertilizers_columnNames;
        }
        while(resultSet.next()){
            Object[] rowData=new Object[columnNames.length];

            for (int i = 0; i < columnNames.length; i++) {
                rowData[i]=resultSet.getObject(columnNames[i]);
            }
            datalist.add(rowData);
        }
        Object[][] objects = datalist.toArray(new Object[0][0]);
        return objects;

    }

    public Object[] SaleFind(int ID, int Num,String tableName) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql="select name,price from flower1 where flowerID=? "+
                "union "+"select name,price from supplies where supplyID=? "+
                "union "+"select name,price from fertilizers where fertilizerID=? ";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,ID);
        preparedStatement.setObject(2,ID);
        preparedStatement.setObject(3,ID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Object[] rowData = new Object[6];
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        rowData[0]=ID;
        rowData[1]=resultSet.getObject("Name");
        rowData[2]=Num;
        rowData[3]=resultSet.getObject("price");
        BigDecimal price=(BigDecimal)resultSet.getObject("price");
        rowData[4]=price.multiply(BigDecimal.valueOf(Num));
        rowData[5]=timestamp;

        for (int i = 0; i < 6; i++) {
            System.out.println(rowData[i]);
        }
        return rowData;
    }

    private String sql;
    public void Insert(String tableName,Object field1, Object field2, Object field3, Object field4, Object field5 ,Object field6) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String[] columnNames=null;
        String[] flower_columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
        String[] supplies_columnNames = {"SupplyID", "Name", "Category", "Price", "Stock"};
        String[] fertilizers_columnNames = {"FertilizerID", "Name", "Type", "Price", "Stock"};

        if(tableName.equals("flower1")){
            sql="insert into flower1 (FlowerID,Name,Category,Origin,Price,Stock)" +
                    "values (?,?,?,?,?,?)";
        }else if (tableName.equals("supplies")){
            sql="insert into supplies (SupplyID,Name,Category,Price,Stock)" +
                    "values (?,?,?,?,?)";
        }else if(tableName.equals("fertilizers")){
            sql="insert into fertilizers (FertilizerID,Name,Type,Price,Stock)" +
                    "values (?,?,?,?,?)";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,field1);
        preparedStatement.setObject(2,field2);
        preparedStatement.setObject(3,field3);
        preparedStatement.setObject(4,field4);
        preparedStatement.setObject(5,field5);
        if(tableName.equals("flower1")){
            preparedStatement.setObject(6,field6);
        }

        int i = preparedStatement.executeUpdate();

        if(i>0){
            System.out.println("add_yes");
        }else {
            System.out.println("add_no");
        }
        preparedStatement.close();

    }

    public void Delete(String tableName,int ID,String fieldID) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql="delete from "+tableName+" where "+fieldID+"=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,ID);
        int i = preparedStatement.executeUpdate();
        preparedStatement.close();

    }


    public void init() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        for (int i = 14; i <= 315; i++) {
            String insertSQL = "INSERT INTO flower (flowerID, flowerName, FloralSupplies, manure, inventory, price) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "花名" + (i - 13));
                preparedStatement.setString(3, "花卉用品" + (i - 13));
                preparedStatement.setString(4, "肥料" + (i - 13));
                preparedStatement.setInt(5, 30 - i % 15);
                preparedStatement.setDouble(6, 15.5 + (i - 14) * 5);

                preparedStatement.executeUpdate();
            }
        }
    }
    public void update(int row,int col,Object oldvalue,Object newvalue,String tableName) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        //System.out.println(row);

        DefaultTableModel defaultTableModel =null;
        defaultTableModel=StoreManagerGUI.getTableModel();
        //String[] columnNames={"flowerID","flowerName","FloralSupplies","manure","inventory","price"};
        String[] columnNames=null;
        String[] flower_columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
        String[] supplies_columnNames = {"SupplyID", "Name", "Category", "Price", "Stock"};
        String[] fertilizers_columnNames = {"FertilizerID", "Name", "Type", "Price", "Stock"};
        //System.out.println(col);
        String fieldID=null;

        if (tableName.equals("flower1")){          //选择了花
            columnNames=flower_columnNames;
            fieldID="FlowerID";
        }else if(tableName.equals("supplies")){    //选择了花卉工具
            columnNames=supplies_columnNames;
            fieldID="SupplyID";
        }else if(tableName.equals("fertilizers")){ //选择了肥料
            columnNames=fertilizers_columnNames;
            fieldID="FertilizerID";
        }

        String ColumnName=columnNames[col];
        int ID=(int) (defaultTableModel.getValueAt(row,0));

        System.out.println(tableName+" "+ColumnName+ID+" "+fieldID+" "+newvalue+" ");

        String sql="update "+tableName+" set "+ ColumnName+"= ? where "+fieldID+"=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,newvalue);
        preparedStatement.setObject(2,ID);
        int i = preparedStatement.executeUpdate();

        if(i>0){
            System.out.println("yes");
        }else{
            System.out.println("no");
        }
        preparedStatement.close();
    }

    public void update_primary_key(int row,int col,Object oldvalue,Object newvalue,String tableName) throws SQLException {
        DefaultTableModel defaultTableModel =null;
        defaultTableModel=StoreManagerGUI.getTableModel();
        String[] columnNames=null;
        String[] flower_columnNames = {"FlowerID", "Name", "Category", "Origin", "Price", "Stock"};
        String[] supplies_columnNames = {"SupplyID", "Name", "Category", "Price", "Stock"};
        String[] fertilizers_columnNames = {"FertilizerID", "Name", "Type", "Price", "Stock"};
        String fieldID=null;

        if (tableName.equals("flower1")){          //选择了花
            columnNames=flower_columnNames;
            fieldID="FlowerID";
        }else if(tableName.equals("supplies")){    //选择了花卉工具
            columnNames=supplies_columnNames;
            fieldID="SupplyID";
        }else if(tableName.equals("fertilizers")){ //选择了肥料
            columnNames=fertilizers_columnNames;
            fieldID="FertilizerID";
        }

        int ID=(int) (defaultTableModel.getValueAt(row,0));
        String sql="select * from"+tableName+"where"+fieldID+"="+ID;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();


        String sql1="delete from"+tableName+"where"+fieldID+"="+ID;
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        preparedStatement1.executeUpdate();

        String sql2="update "+tableName+" set "+ fieldID+"= ? where "+fieldID+"="+ID;

    }

    public void saleinsert(Object[] rowDate) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql="insert into salesinformation(ID,Name,Num,Price,totalPrice,date)" +
                "values(?,?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,rowDate[0]);
        preparedStatement.setObject(2,rowDate[1]);
        preparedStatement.setObject(3,rowDate[2]);
        preparedStatement.setObject(4,rowDate[3]);
        preparedStatement.setObject(5,rowDate[4]);
        preparedStatement.setObject(6,rowDate[5]);
        int i = preparedStatement.executeUpdate();
        if (i>0){
            System.out.println("yes");
        }else {
            System.out.println("no");
        }
        preparedStatement.close();
    }

    public void saleupdate(Object sale_data[]) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql=null;
        String tableName;
        String tableID;
        //System.out.println(sale_data[0]);
        if(((int)sale_data[0]>10000)&&((int)sale_data[0]<20000)) {
            tableName="flower1";
            tableID="flowerID";

        }else if(((int)sale_data[0]>20000)&&((int)sale_data[0]<30000)){
            tableName="supplies";
            tableID="supplyID";
        }else {
            tableName="fertilizers";
            tableID="fertilizerID";
        }
        sql="select Stock from "+tableName+" where "+tableID+"=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, (int) sale_data[0]);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int Stock=resultSet.getInt("Stock");
        //System.out.println(Stock);
        preparedStatement.close();

        String sql_update="update "+tableName+" set Stock=? where "+tableID+"=?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql_update);
        preparedStatement1.setObject(1,Stock-(int)sale_data[2]);
        preparedStatement1.setObject(2,sale_data[0]);
        preparedStatement1.executeUpdate();

        preparedStatement.close();
    }



    public void insertFlowersData(int numRecords) throws SQLException {
        String query = "INSERT INTO Flower1 (FlowerID, Name, Category, Origin, Price, Stock) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 1; i <= numRecords; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "花名" + i);
                preparedStatement.setString(3, "种类" + i % 5);
                preparedStatement.setString(4, "产地" + i % 3);
                preparedStatement.setDouble(5, getRandomDouble(5, 50));
                preparedStatement.setInt(6, getRandomInt(10, 100));
                preparedStatement.executeUpdate();
            }
        }
    }

    public void insertSuppliesData(int numRecords) throws SQLException {
        String query = "INSERT INTO Supplies (SupplyID, Name, Category, Price, Stock) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 1; i <= numRecords; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "工具名" + i);
                preparedStatement.setString(3, "种类" + i % 4);
                preparedStatement.setDouble(4, getRandomDouble(2, 20));
                preparedStatement.setInt(5, getRandomInt(5, 50));
                preparedStatement.executeUpdate();
            }
        }
    }

    public void insertFertilizersData(int numRecords) throws SQLException {
        String query = "INSERT INTO Fertilizers (FertilizerID, Name, Type, Price, Stock) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 1; i <= numRecords; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "肥料名" + i);
                preparedStatement.setString(3, "种类" + i % 3);
                preparedStatement.setDouble(4, getRandomDouble(3, 30));
                preparedStatement.setInt(5, getRandomInt(5, 50));
                preparedStatement.executeUpdate();
            }
        }
    }

    public double getRandomDouble(double min, double max) {
        return min + (max - min) * new Random().nextDouble();
    }

    public int getRandomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public void update_ID() throws SQLException {
        String sql = "update Fertilizers set FertilizerID=? where FertilizerID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= 312; i++) {
            preparedStatement.setObject(2,i);
            preparedStatement.setObject(1,30000+i);
            preparedStatement.executeUpdate();
        }

    }

    public boolean IsLogin_boss(String user, String password) throws SQLException {
        String sql="select * from boss where user=? and password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,user);
        preparedStatement.setObject(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return true;
        }else {
            return false;
        }

    }
    public boolean IsLogin_employ(String user,String password) throws SQLException {
        String sql="select * from employ where user=? and password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,user);
        preparedStatement.setObject(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return true;
        }else {
            return false;
        }

    }

    public boolean insert_sale_table(String user,String password,String name,String sex,int age) throws SQLException {
        boolean flag=false;
        String sql1="select * from employ where user=?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        preparedStatement1.setObject(1,user);
        ResultSet resultSet = preparedStatement1.executeQuery();

        if(resultSet.next()){
            return false;
        }else {
            flag=true;
        }
        resultSet.close();
        preparedStatement1.close();

        String sql="insert into employ(user,password,name,sex,age)" +
                "values(?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,user);
        preparedStatement.setObject(2,password);
        preparedStatement.setObject(3,name);
        preparedStatement.setObject(4,sex);
        preparedStatement.setObject(5,age);
        int i = preparedStatement.executeUpdate();
        if(i>0){
            System.out.println("insert_yes!");
        }else {
            System.out.println("insert_no");
        }

        return flag;

    }
    public BigDecimal count_sale_information() throws SQLException {
        String sql ="select totalPrice from salesinformation ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        BigDecimal totalPrice= BigDecimal.valueOf(0);
        while (resultSet.next()){
            BigDecimal tp = resultSet.getBigDecimal("totalPrice");
            totalPrice=totalPrice.add(tp);
        }
        System.out.println(totalPrice);
        return totalPrice;
    }

    public void into_excel(String tableName) throws SQLException {
        String sql="select * from "+tableName;

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        //resultSet.next();

        XSSFSheet sheet = workbook.createSheet(tableName);
//获取表列数
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        XSSFRow hreadRow = sheet.createRow(0);
        for (int i = 1; i <= columnCount; i++) {
            String columnName=metaData.getColumnName(i);
            XSSFCell cell = hreadRow.createCell(i - 1);
            cell.setCellValue(columnName);
        }

        int rowNumber=1;
        while (resultSet.next()){
            XSSFRow row = sheet.createRow(rowNumber++);
            for (int i = 1; i<= columnCount; i++) {
                XSSFCell cell = row.createCell(i-1);
                cell.setCellValue(resultSet.getString(i));
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src\\excel\\"+tableName+".xlsx");
            workbook.write(fileOutputStream);
            System.out.println("excel数据写入成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
