package TestJmeter;

import com._4paradigm.predictor.PredictRequest;
import com._4paradigm.predictor.PredictResponse;
import com._4paradigm.predictor.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.io.Files;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.File;
import java.net.URL;

/**
 * Created by wangyiping on 12/08/2019 6:21 PM.
 */
public class TestThriftByJmeter extends AbstractJavaSamplerClient {

    public RpcClient client;
    public PredictRequest request;
    public PredictResponse response;

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("ip", "172.27.128.70");
        arguments.addArgument("port", "32238");
        arguments.addArgument("filepath", "");
        return arguments;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
        try {
            final ObjectMapper OM = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL);

            // 获取参数
            String ip = context.getParameter("ip");
            int port = context.getIntParameter("port");
            String filepath = context.getParameter("filepath");


            // 创建客户端
            System.out.println("ip:port is " + ip + ":" + port);
            System.out.println("Predict body filepath is: " + filepath);
            client = new RpcClient(ip, port);

            String predictStr;
            if (filepath != null) {
                //predictStr = Files.toString(new File(filepath), Charsets.UTF_8);
                predictStr = new String(Files.toByteArray(new File(filepath)));
            } else {
                URL url = this.getClass().getResource("/predict.json");

                if (url == null) {
                    throw new Exception("not found predict.json error");
                }
                //predictStr = Files.toString(new File(url.getFile()), Charsets.UTF_8);
                predictStr = new String(Files.toByteArray(new File(url.getFile())));
            }

            // 设置request请求
            request = OM.readValue(predictStr.getBytes(), PredictRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setupTest(context);
    }

    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        result.sampleStart();
        try {
            long begin = System. currentTimeMillis();
            response = client.getClient().predict(request);
            long cost = (System.currentTimeMillis() - begin);
            System.out.println(response.toString() + " cost:[" + cost + "ms]");

            if (response.getStatus() == Status.OK) {
                result.setSuccessful(true);
            } else {
                result.setSuccessful(false);
            }
        } catch (Exception e) {
            result.setSuccessful(false);
            e.printStackTrace();
        }  finally {
            result.sampleEnd();
        }
        return result;
    }

    public void tearDownTest(JavaSamplerContext context) {
        super.teardownTest(context);
    }

}
