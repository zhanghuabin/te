/**
 * Created with IntelliJ IDEA.
 * User: Huabin Zhang
 * Date: 13-11-17
 * Time: 上午3:55
 * To change this template use File | Settings | File Templates.
 */


import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized)
class VelocitySingletonTest {

    static final NAME1 = 'Velocity1'
    static final NAME2 = 'Velocity2'
    static final EXPECTED_STR1 = """public class HelloVelocity {

    public static void main(String[] args) {
        System.out.println("Hello, ${NAME1.toLowerCase()}");
    }

}
"""
    static final EXPECTED_STR2 = "Hi, $NAME1, you're young guy."
    static final EXPECTED_STR3 = "Hi, $NAME2, you're old dog."
    static final EXPECTED_STR4 = """apple for 51RMB. high
pear for 49RMB. low"""

    @BeforeClass
    static void setup() {
        VelocitySingletonTest.class.getResource('velocity.conf').withReader {
            reader ->
            new Properties().with {
                load reader
                setProperty 'url.resource.loader.root', VelocitySingletonTest.class.getResource('templates/') as String
                Velocity.init it
            }
        }
//        Velocity.init VelocitySingletonTest.class.getResource('velocity.conf').file
    }

    // parameters
    def ctx, vmName, expected

    VelocitySingletonTest(a, b, c) {
        ctx = a
        vmName = b
        expected = c
    }

    @Parameters
    static data() {
        [
            // case 1, string conversion
            [
                [name:NAME1] as VelocityContext,
                'hello-velocity.vm',
                EXPECTED_STR1
            ] as Object[],
            // case 2, ifelse syntax
            [
                [user:[name:NAME1, age:40]] as VelocityContext,
                'ifelse.vm',
                EXPECTED_STR2
            ] as Object[],
            // case 3, ifelse
            [
                [user:[name:NAME2, age:80]] as VelocityContext,
                'ifelse.vm',
                EXPECTED_STR3
            ] as Object[],
            // case 4, list+ifelse+bean property
            [
                [items:[[name:'apple', price:51], [name:'pear', price:49]] as Item[]] as VelocityContext,
                'list.vm',
                EXPECTED_STR4
            ] as Object[],
        ]
    }

    @Test
    void testTemplate() {
        // 1, assemble template & context
        def out = new StringWriter()
        Velocity.mergeTemplate vmName, ctx, out

        // 2, validate result
        assert expected as String == out as String
    }


    static class Item {
        def name, price
        def doSomething() { 'ok' }
    }

}
