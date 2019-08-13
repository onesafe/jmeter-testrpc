import TestJmeter.RpcClient;
import com._4paradigm.predictor.PredictRequest;
import com._4paradigm.predictor.PredictResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Created by wangyiping on 13/08/2019 10:30 AM.
 */
public class TestRpcPredictor {

    @Test
    public void predict() throws Exception {
        final ObjectMapper OM = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        RpcClient client = new RpcClient("172.27.128.70", 32238);

        URL url = this.getClass().getClassLoader().getResource("predict.json");
        if (url == null) {
            throw new Exception("not found predict.json error");
        }
        String predictStr = Files.toString(new File(url.getFile()), Charsets.UTF_8);

        // 设置request请求
        PredictRequest request = OM.readValue(predictStr.getBytes(), PredictRequest.class);
        PredictResponse response = client.getClient().predict(request);
        System.out.println(response.getInstances().get(0).getScores());
    }
}
