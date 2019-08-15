package com.yjy.jsqlparser;

import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        testSelect();
    }

    private static void testSelect() {
        Function function = new Function();
        function.setDistinct(true);
        function.setName("count");
        function.setParameters(new ExpressionList(new Column("cardid")));

        Select select = SelectUtils.buildSelectFromTableAndExpressions(new Table("card"), function);

        System.out.println(select);
    }
}
