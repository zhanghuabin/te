/**
 * Created with IntelliJ IDEA.
 * User: Huabin Zhang
 * Date: 13-11-17
 * Time: 上午3:55
 * To change this template use File | Settings | File Templates.
 */

import freemarker.template.Configuration
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized)
class FreeMarkerTest {

    static final NAME1 = 'FreeMarker1'
    static final NAME2 = 'FreeMarker2'
    static final EXPECTED_STR1 = """public class HelloFreeMarker {

    public static void main(String[] args) {
        System.out.println("Hello, ${NAME1.toLowerCase()}");
    }

}
"""
    static final EXPECTED_STR2 = "Hi, $NAME1, you're young guy."
    static final EXPECTED_STR3 = "Hi, $NAME2, you're old dog."
    static final EXPECTED_STR4 = """apple for 51RMB. highok
pear for 49RMB. lowok
"""


    // parameters
    def model, ftlName, expected

    FreeMarkerTest(a, b, c) {
        model = a
        ftlName = b
        expected = c
    }

    @Parameters
    static data() {
        [
            // case 1, string conversion
            [
                [name:NAME1],
                'hello-freemarker.ftl',
                EXPECTED_STR1
            ] as Object[],
            // case 2, ifelse syntax
            [
                [user:[name:NAME1, age:40]],
                'ifelse.ftl',
                EXPECTED_STR2
            ] as Object[],
            // case 3, ifelse
            [
                [user:[name:NAME2, age:80]],
                'ifelse.ftl',
                EXPECTED_STR3
            ] as Object[],
            // case 4, list+ifelse+bean property
            [
                [items:[[name:'apple', price:51], [name:'pear', price:49]] as Item[]],
                'list.ftl',
                EXPECTED_STR4
            ] as Object[],
        ]
    }

    @Test
    void testTemplate() {
        // initialize fm config
        def cfg = new Configuration().with {
            setClassForTemplateLoading this.class, 'templates'
            it
        }

        // 1, load template
        def ftl = cfg.getTemplate ftlName

        // 2, merge template & model
        def out = new StringWriter()
        ftl.process model, out

        // 3, validate result
        assert expected as String == out as String
    }


    static class Item {
        def name, price
        def doSomething() { 'ok' }
    }

}
