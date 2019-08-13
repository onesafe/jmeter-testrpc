package TestJmeter;

import com._4paradigm.predictor.brpc.client.PredictorBrpcClient;
import lombok.Data;

/**
 * Created by wangyiping on 12/08/2019 6:26 PM.
 */
@Data
public class RpcClient {

    private PredictorBrpcClient client;

    public RpcClient() {}

    public RpcClient(String ip, int port) {
        this.client = new PredictorBrpcClient(String.format("%s:%d", ip, port));
    }
}
