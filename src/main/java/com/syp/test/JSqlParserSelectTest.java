package com.syp.test;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.insert.*;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shiyuping on 2024/6/6 20:27
 *
 * @author shiyuping
 */
public class JSqlParserSelectTest {

    public static final String SQL_SELECT = "SELECT DISTINCT u.id, r.role_name, u.user_name, u.sex, u.email " +
            "FROM t_user u " +
            "LEFT JOIN t_role r ON u.role_id = r.id " +
            "WHERE r.role_name = '管理员' " +
            "ORDER BY u.age DESC " +
            "LIMIT 0,10";

    public static final String SQL_INSERT = "INSERT INTO t_user (role_id, user_name, email, age, sex, register_time )\n" +
            "VALUES ( 1, 'xw', 'isxuwei@qq.com', 25, '男', '2024-04-12 17:37:18' );";

    public static void main(String[] args) {
        //sqlParseSelect();
        //sqlParseInsert();
        //modifyTable();
        //modifyTableForUpdate();
        //findQueryFiledByInsert();
        findQueryFieldByAlter();
    }

    /**
     * 测试 SQL 解析
     */
    public static void sqlParseSelect() {
        try {
            Select select = (Select) CCJSqlParserUtil.parse(SQL_SELECT);
            PlainSelect plainSelect = select.getSelectBody(PlainSelect.class);
            System.out.println("【DISTINCT 子句】：" + plainSelect.getDistinct());
            System.out.println("【查询字段】：" + plainSelect.getSelectItems());
            System.out.println("【FROM 表】：" + plainSelect.getFromItem());
            System.out.println("【WHERE 子句】：" + plainSelect.getWhere());
            System.out.println("【JOIN 子句】：" + plainSelect.getJoins());
            System.out.println("【LIMIT 子句】：" + plainSelect.getLimit());
            System.out.println("【OFFSET 子句】：" + plainSelect.getOffset());
            System.out.println("【ORDER BY 子句】：" + plainSelect.getOrderByElements());
            System.out.println("--------------------------------------------------------");
            // 取消去重
            plainSelect.setDistinct(null);
            // 修改查询字段为 *
            List<SelectItem> selectItems = new ArrayList<>();
            selectItems.add(new AllColumns());
            plainSelect.setSelectItems(selectItems);
            // 修改 WHERE 子句
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new Column("u.id"));
            equalsTo.setRightExpression(new LongValue(1));
            plainSelect.setWhere(equalsTo);
            // 修改 LIMIT 子句
            Limit limit = new Limit();
            limit.setRowCount(new LongValue(5));
            limit.setOffset(new LongValue(0));
            plainSelect.setLimit(limit);
            // 修改排序为 u.age ASC
            OrderByElement orderByElement = new OrderByElement();
            orderByElement.setExpression(new Column("u.age"));
            orderByElement.setAsc(true); // 升序
            plainSelect.setOrderByElements(Collections.singletonList(orderByElement));
            System.out.println("【处理后 SQL】" + plainSelect);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    public static void sqlParseInsert() {
        try {
            Insert insert = (Insert) CCJSqlParserUtil.parse(SQL_INSERT);
            System.out.println("【插入目标表】：" + insert.getTable());
            System.out.println("【插入字段】：" + insert.getColumns());
            System.out.println("【插入值】：" + insert.getSelect().getSelectBody());
            System.out.println("--------------------------------------------------------");
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    public static void modifyTable() {
        String sql = "insert into ads_t_user (id, name, age, pwd)" +
                "select id, name, age, pwd from ods_t_user_1 " +
                "inner join ods_t_user_2 on ods_t_user_1.id = ods_t_user_2.id " +
                "where ods_t_user_1.status = 1;";
        try {
            System.out.println("原SQL: " + sql);
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Insert) {
                Insert insert = (Insert) statement;
                String insertTable = insert.getTable().getName();
                System.out.println("Insert Table: " + insertTable);
                sql = sql.replaceAll(insertTable, insertTable + "_001_001_002");
                Select select = insert.getSelect();
                if (select != null) {
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List<String> tableList = tablesNamesFinder.getTableList(select);
                    System.out.println("Select Tables: " + tableList);
                    for (String table : tableList) {
                        System.out.println("Table: " + table);
                        sql = sql.replaceAll(table, table + "_001_001_002");
                    }
                }
                System.out.println("替换后SQL: " + sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modifyTableForUpdate() {
        String sql = "UPDATE ads_t_user a " +
                "SET a.name = (SELECT u.name FROM ods_t_user_1 u WHERE u.type = a.type " +
                "AND u.date IN (SELECT MAX(date) FROM ods_t_user_2)) " +
                "WHERE a.sex = '男';";
        try {
            System.out.println("原SQL: " + sql);
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Update) {
                Update update = (Update) statement;
                String updateTable = update.getTable().getName();
                System.out.println("update Table: " + updateTable);
                sql = sql.replaceAll(updateTable, updateTable + "_001_001_002");
                Select select = update.getSelect();
                if (select != null) {
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List<String> tableList = tablesNamesFinder.getTableList(select);
                    System.out.println("Select Tables: " + tableList);
                    for (String table : tableList) {
                        sql = sql.replaceAll(table, table + "_001_001_002");
                    }
                }
                System.out.println("替换后SQL: " + sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void findQueryFiledByInsert() {
        String sql = "insert into ads_t_user (id, name, age, pwd) " +
                "select ods_t_user_1.id, ods_t_user_1.name, ods_t_user_2.age, ods_t_user_2.pwd from ods_t_user_1 " +
                "inner join ods_t_user_2 on ods_t_user_1.id = ods_t_user_2.id " +
                "where ods_t_user_1.status = 1;";
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Insert) {
                Insert insert = (Insert) statement;
                String insertTable = insert.getTable().getName();
                System.out.println("Insert Table: " + insertTable);
                System.out.println("Insert Column: " + insert.getColumns());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void findQueryFieldByAlter() {
        String sql = "ALTER TABLE `ads_t_user` ADD COLUMN `source` int, ADD COLUMN `source2` int";
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Alter) {
                Alter alter = (Alter) statement;
                String insertTable = alter.getTable().getName();
                System.out.println("alter Table: " + insertTable);
                List<AlterExpression> alterExpressions = alter.getAlterExpressions();
                alterExpressions.forEach(alterExpression -> {
                    alterExpression.getColDataTypeList().forEach(colDataType -> {
                        System.out.println("alter.columnName: " + colDataType.getColumnName());
                    });
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}