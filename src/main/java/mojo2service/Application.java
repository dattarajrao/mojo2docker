package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.io.*;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.prediction.*;
import hex.genmodel.MojoModel;

@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
        return "ShipExpress ETA Testing Micro-service<br>Endpoint = /predict?input_array=<array of 10 double values>";
    }

    @RequestMapping("/predict")
    public String predict(@RequestParam(value="input_array") Double[] in_array) {
      if(in_array == null)
        return "{'error': 'Bad inputs'}";
      else if(in_array.length != 10)
        return "{'error': 'Wrong number of inputs}";
      else {
        try {
          //EasyPredictModelWrapper.Config config = new EasyPredictModelWrapper.Config().setModel(MojoModel.load("drf_47a6e080_29b5_4cc4_a3a0_946c03c03f6f.zip")).setEnableLeafAssignment(true);
          EasyPredictModelWrapper.Config config = new EasyPredictModelWrapper.Config().setModel(MojoModel.load("model.zip")).setEnableLeafAssignment(true);
          EasyPredictModelWrapper model = new EasyPredictModelWrapper(config);

          RowData row = new RowData();
          row.put("ORIGIN_ID", in_array[0]);
          row.put("DESTINATION_ID", in_array[1]);
          row.put("O_LAT", in_array[2]);
          row.put("O_LONG", in_array[3]);
          row.put("D_LAT", in_array[4]);
          row.put("D_LONG", in_array[5]);
          row.put("ROUTE_ID", in_array[6]);
          row.put("START_MONTH", in_array[7]);
          row.put("WEEK_DAY", in_array[8]);
          row.put("distance", in_array[9]);

          RegressionModelPrediction p = model.predictRegression(row);
          return "{'result': " + p.value + "'}";
        } catch(Exception exp) {
          return "{'error': '" + exp + "'}";
        }
      }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
