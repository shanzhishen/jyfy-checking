package cn.com.jyfy.checking.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ClassExcelListener extends AnalysisEventListener {

    private List<List<String>> data = new ArrayList<>();

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        List<String> list = (List<String>) o;
        for (String s: list){
            System.out.print(s + " ");
        }
        System.out.println();
        data.add(list);
    }

    public void clearData(){
        this.data = new ArrayList<>();
    }


    private void addDates(List<String> list) {
//        String firstDay = list.get(0) + list.get(1);
//        LocalDate day = LocalDate.parse(firstDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
//        int length = list.size();
//        dates.add(day);
//        for (int j = 1; j < length; j++) {
//            String str = list.get(i);
//            if(!StringUtil.isNull(str)){
//
//            }
//        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
