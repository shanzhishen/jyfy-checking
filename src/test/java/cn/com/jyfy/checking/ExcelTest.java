package cn.com.jyfy.checking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.time.LocalDateTime;

public class ExcelTest {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Test1 test1 = new Test1().setId(1).setName("Test1").setCreateTime(LocalDateTime.now());
        String json = mapper.writeValueAsString(test1);
        Test2 test2 = mapper.readValue(json,Test2.class);
        System.out.println(test2);

        Test2 t2  = new Test2().setId(2).setName("Test2");
        Test1 t1 = mapper.readValue(mapper.writeValueAsString(t2),Test1.class);
        System.out.println(t1);
    }


}

@Data
@Accessors(chain = true)
class Test1 {
    private  Integer id;
    private String name;
    private LocalDateTime createTime;
}

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
class Test2 {
    private  Integer id;
    private String name;
}