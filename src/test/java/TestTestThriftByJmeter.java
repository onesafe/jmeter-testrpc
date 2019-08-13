import TestJmeter.TestThriftByJmeter;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.junit.Test;

/**
 * Created by wangyiping on 13/08/2019 2:15 PM.
 */
public class TestTestThriftByJmeter {
    @Test
    public void test1() {
        TestThriftByJmeter testThriftByJmeter = new TestThriftByJmeter();
        Arguments arguments = new Arguments();
        arguments.addArgument("ip", "172.27.128.70");
        arguments.addArgument("port", "32238");

        JavaSamplerContext context = new JavaSamplerContext(arguments);
        testThriftByJmeter.setupTest(context);
        testThriftByJmeter.runTest(context);
    }
}
