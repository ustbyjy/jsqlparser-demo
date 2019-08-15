package com.yjy.jsqlparser;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    private static final CCJSqlParserManager parserManager = new CCJSqlParserManager();


    public static void main(String[] args) throws Exception {
        testCustomSelect();

//        testSelect();
    }

    private static void testSelect() throws Exception {
        String statement = "select a+b, count(*) as c FROM t where col1 = 9 and col2 != null";
        Select select = (Select) parserManager.parse(new StringReader(statement));
        System.out.println(select);

        statement = "select * from t1 left join t2 on t1.id = t2.id";
        select = (Select) parserManager.parse(new StringReader(statement));
        System.out.println(select);

        statement = "select * from (select c1,c2 from t1 where c3 = 1) a left join (select c4,c5 from t2 where c6 = 0) b on a.id = b.id";
        select = (Select) parserManager.parse(new StringReader(statement));

        System.out.println(select);
    }

    private static void testCustomSelect() {
        Select select = new Select();
        PlainSelect selectBody = new PlainSelect();
        select.setSelectBody(selectBody);

        List<SelectItem> selectItems = new ArrayList<>();

        Division division = new Division();
        division.setLeftExpression(new Column("b.num"));
        division.setRightExpression(new Column("a.num"));

        selectItems.add(new SelectExpressionItem(new Column("a.cardid")));
        selectItems.add(new SelectExpressionItem(division));
        selectBody.setSelectItems(selectItems);

        SubSelect a = new SubSelect();
        a.setSelectBody(getLeft());
        a.setAlias(new Alias("a"));

        selectBody.setFromItem(a);

        SubSelect b = new SubSelect();
        b.setSelectBody(getRight());
        b.setAlias(new Alias("b"));

        Join join = new Join();
        join.setLeft(true);
        join.setRightItem(b);

        EqualsTo on = new EqualsTo();
        on.setLeftExpression(new Column("a.cardid"));
        on.setRightExpression(new Column("b.cardid"));
        join.setOnExpression(on);

        selectBody.setJoins(Arrays.asList(join));

        System.out.println(select);
    }

    private static SelectBody getLeft() {
        PlainSelect selectBody = new PlainSelect();

        selectBody.setFromItem(new Table("card"));

        Function function = new Function();
        function.setDistinct(true);
        function.setName("count");
        function.setParameters(new ExpressionList(new Column("fightid")));

        List<SelectItem> selectItems = new ArrayList<>();
        selectItems.add(new SelectExpressionItem(new Column("cardid")));
        SelectExpressionItem functionItem = new SelectExpressionItem(function);
        functionItem.setAlias(new Alias("num"));
        selectItems.add(functionItem);
        selectBody.setSelectItems(selectItems);

        EqualsTo and1Left = new EqualsTo();
        and1Left.setLeftExpression(new Column("matchtype"));
        and1Left.setRightExpression(new LongValue(1));

        NotEqualsTo and1Right = new NotEqualsTo();
        and1Right.setLeftExpression(new Column("ifattendant"));
        and1Right.setRightExpression(new LongValue(1));

        AndExpression and1 = new AndExpression(and1Left, and1Right);

        EqualsTo and2Right = new EqualsTo();
        and2Right.setLeftExpression(new Column("status"));
        and2Right.setRightExpression(new LongValue(2));

        AndExpression and2 = new AndExpression(and1, and2Right);

        EqualsTo and3Right = new EqualsTo();
        and3Right.setLeftExpression(new Column("result"));
        and3Right.setRightExpression(new LongValue(0));

        AndExpression and3 = new AndExpression(and2, and3Right);

        selectBody.setWhere(and3);

        selectBody.addGroupByColumnReference(new Column("cardid"));

        return selectBody;
    }

    private static SelectBody getRight() {
        PlainSelect selectBody = new PlainSelect();

        selectBody.setFromItem(new Table("card"));

        Function function = new Function();
        function.setDistinct(true);
        function.setName("count");
        function.setParameters(new ExpressionList(new Column("fightid")));

        List<SelectItem> selectItems = new ArrayList<>();
        selectItems.add(new SelectExpressionItem(new Column("cardid")));
        SelectExpressionItem functionItem = new SelectExpressionItem(function);
        functionItem.setAlias(new Alias("num"));
        selectItems.add(functionItem);
        selectBody.setSelectItems(selectItems);

        EqualsTo and1Left = new EqualsTo();
        and1Left.setLeftExpression(new Column("matchtype"));
        and1Left.setRightExpression(new LongValue(1));

        NotEqualsTo and1Right = new NotEqualsTo();
        and1Right.setLeftExpression(new Column("ifattendant"));
        and1Right.setRightExpression(new LongValue(1));

        AndExpression and1 = new AndExpression(and1Left, and1Right);

        EqualsTo and2Right = new EqualsTo();
        and2Right.setLeftExpression(new Column("status"));
        and2Right.setRightExpression(new LongValue(2));

        AndExpression and2 = new AndExpression(and1, and2Right);

        EqualsTo and3Right = new EqualsTo();
        and3Right.setLeftExpression(new Column("result"));
        and3Right.setRightExpression(new LongValue(1));

        AndExpression and3 = new AndExpression(and2, and3Right);

        selectBody.setWhere(and3);

        selectBody.addGroupByColumnReference(new Column("cardid"));

        return selectBody;
    }

}
