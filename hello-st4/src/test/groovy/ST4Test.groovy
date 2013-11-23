/**
 * Created with IntelliJ IDEA.
 * User: Huabin Zhang
 * Date: 13-11-17
 * Time: 上午3:55
 * To change this template use File | Settings | File Templates.
 */


import org.antlr.stringtemplate.StringTemplateGroup
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized)
class ST4Test {

    static final NAME1 = 'ST4-1'
    static final NAME2 = 'ST4-2'
    static final EXPECTED_STR1 = """public class HelloST4 {

    public static void main(String[] args) {
        System.out.println("Hello, ${NAME1.toLowerCase()}");
    }

}"""
    static final EXPECTED_STR2 = "Hi, $NAME1, you're young guy."
    static final EXPECTED_STR3 = "Hi, $NAME2, you're old dog."
    static final EXPECTED_STR4 = """apple for 51RMB. high
pear for 49RMB. low
"""


    // parameters
    def model, stName, expected

    ST4Test(a, b, c) {
        model = a
        stName = b
        expected = c
    }

    @Parameters
    static data() {
        [
            // case 1, string conversion
            [
                [nameLowerCase:NAME1.toLowerCase()],
                'hello-st4',
                EXPECTED_STR1
            ] as Object[],
            // case 2, ifelse syntax
            [
                [user:[name:NAME1, age:40, elder:false]],
                'ifelse',
                EXPECTED_STR2
            ] as Object[],
            // case 3, ifelse
            [
                [user:[name:NAME2, age:80, elder:true]],
                'ifelse',
                EXPECTED_STR3
            ] as Object[],
            // case 4, list+ifelse+bean property
            [
                [items:[[name:'apple', price:51, high:true], [name:'pear', price:49]] as Item[]],
                'list',
                EXPECTED_STR4
            ] as Object[],
        ]
    }

    @Test
    void testTemplate() {
        def stg = ['test_group', this.class.getResource('templates').file] as StringTemplateGroup

        // 1, load template
        def st = stg.getInstanceOf stName

        // 2, merge template & model
        st.attributes = model

        // 3, validate result
        assert expected as String == st as String
    }


    static class Item {
        def name, price, high
        def doSomething() { 'ok' }
    }

}
