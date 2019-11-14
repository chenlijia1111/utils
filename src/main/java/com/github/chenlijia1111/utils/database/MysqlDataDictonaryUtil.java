package com.github.chenlijia1111.utils.database;

import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.database.pojo.dictionary.DictionaryFieldPojo;
import com.github.chenlijia1111.utils.database.pojo.dictionary.DictionaryTablePojo;
import com.github.chenlijia1111.utils.list.Lists;
import com.github.chenlijia1111.utils.office.word.XDOCWordUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlCursor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;

/**
 * mysql 数据字典导出工具
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/14 0014 下午 1:15
 **/
public class MysqlDataDictonaryUtil {


    /**
     * 导出到word
     *
     * @param exportFile 1
     * @return void
     * @since 下午 2:18 2019/11/14 0014
     **/
    public void writeToWord(String url, String userName, String password, String databaseName, File exportFile) throws SQLException {
        if (FileUtils.checkFileSuffix(exportFile.getName(), "docx")) {
            writeToDocx(url, userName, password, databaseName, exportFile);
        }
    }

    /**
     * 导出到docx
     *
     * @param exportFile 1
     * @return void
     * @since 下午 2:16 2019/11/14 0014
     **/
    private void writeToDocx(String url, String userName, String password, String databaseName, File exportFile) throws SQLException {

        //获取数据库连接
        CommonSqlUtil sqlUtil = new CommonSqlUtil();
        Connection connection = sqlUtil.createConnection(url, userName, password);

        //获取所有字典对象集合
        List<DictionaryTablePojo> dictionaryTablePojoList = getDictionaryTablePojoList(connection, databaseName);


        XWPFDocument document = new XWPFDocument();
        try (FileOutputStream outputStream = new FileOutputStream(exportFile)) {

            //添加标题
            XWPFParagraph titleParagraph = document.createParagraph();
            //插入一级标题
            XDOCWordUtil.setLevelTitle1(document, titleParagraph, "数据字典");

            //开始处理数据
            if (Lists.isNotEmpty(dictionaryTablePojoList)) {
                for (DictionaryTablePojo pojo : dictionaryTablePojoList) {
                    //表名-二级标题
                    XWPFParagraph tableNameParagraph = document.createParagraph();
                    XDOCWordUtil.setLevelTitle2(document, tableNameParagraph, pojo.getTableName() + "：" + pojo.getTableComment());

                    //插入表格
                    XWPFParagraph tableParagraph = document.createParagraph();
                    XmlCursor cursor = tableParagraph.getCTP().newCursor();
                    XWPFTable table = document.insertNewTbl(cursor);
                    //将对象数据导入到表格中
                    List<DictionaryFieldPojo> fieldList = pojo.getFieldList();
                    XDOCWordUtil.writePojoToTable(fieldList, table, DictionaryFieldPojo.class);
                }
            }

            document.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建数据字典对象
     *
     * @param connection   数据库连接
     * @param databaseName 数据库名称
     * @return java.util.List<com.github.chenlijia1111.utils.database.pojo.dictionary.DictionaryTablePojo>
     * @since 下午 1:31 2019/11/14 0014
     **/
    private List<DictionaryTablePojo> getDictionaryTablePojoList(Connection connection, String databaseName) throws SQLException {

        //获取所有表格
        List<String> allTables = getAllTables(connection);

        List<DictionaryTablePojo> tablePojoList = Lists.newArrayList();
        if (Lists.isNotEmpty(allTables)) {
            //切换到information_schema
            PreparedStatement preparedStatement = connection.prepareStatement("USE information_schema");
            preparedStatement.execute();

            //开始获取每个表的详细字段
            for (String tableName : allTables) {
                preparedStatement = connection.prepareStatement("SELECT C.TABLE_NAME     AS 'tableName',\n" +
                        "       T.TABLE_COMMENT  AS 'tableComment',\n" +
                        "       C.COLUMN_NAME    AS 'columnName',\n" +
                        "       C.COLUMN_TYPE    AS 'columnType',\n" +
                        "       C.IS_NULLABLE    AS 'nullAble',\n" +
                        "       C.COLUMN_COMMENT AS 'columnComment',\n" +
                        "       C.COLUMN_KEY     AS 'tableKey'\n" +
                        "FROM COLUMNS C\n" +
                        "         INNER JOIN TABLES T ON C.TABLE_SCHEMA = T.TABLE_SCHEMA\n" +
                        "    AND C.TABLE_NAME = T.TABLE_NAME\n" +
                        "WHERE T.TABLE_SCHEMA ='" + databaseName + "'" + "AND C.TABLE_NAME = '" + tableName + "'");
                ResultSet resultSet = preparedStatement.executeQuery();

                //开始处理数据
                DictionaryTablePojo tablePojo = new DictionaryTablePojo();
                tablePojo.setTableName(tableName);
                List<DictionaryFieldPojo> fieldPojos = Lists.newArrayList();
                while (resultSet.next()) {
                    String tableComment = resultSet.getString("tableComment");
                    tablePojo.setTableComment(tableComment);

                    DictionaryFieldPojo fieldPojo = new DictionaryFieldPojo();
                    String columnName = resultSet.getString("columnName");
                    String columnType = resultSet.getString("columnType");
                    String columnComment = resultSet.getString("columnComment");
                    fieldPojo.setFieldName(columnName);
                    fieldPojo.setFieldType(columnType);
                    fieldPojo.setFieldComment(columnComment);

                    fieldPojos.add(fieldPojo);
                }

                tablePojo.setFieldList(fieldPojos);

                tablePojoList.add(tablePojo);
            }
        }

        return tablePojoList;
    }

    /**
     * 获取数据库所有表名
     *
     * @param connection 1
     * @return java.util.List<java.lang.String>
     * @since 下午 1:37 2019/11/14 0014
     **/
    private List<String> getAllTables(Connection connection) {
        List<String> tablesList = Lists.newArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet tables = statement.executeQuery("show tables");
            while (tables.next()) {
                String tableName = tables.getString(1);
                tablesList.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tablesList;
    }


    public static void main(String[] args) {
        try {
            MysqlDataDictonaryUtil mysqlDataDictonaryUtil = new MysqlDataDictonaryUtil();
            mysqlDataDictonaryUtil.writeToWord("jdbc:mysql://58.250.17.31:3345/ende?serverTimezone=Asia/Shanghai&useSSL=false&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL",
                    "root", "0822myljsw", "ende", new File("E:\\公司资料\\公司\\南天司法\\test1.docx"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
