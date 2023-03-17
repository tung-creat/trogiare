package com.trogiare.component;

import com.trogiare.model.ConverStation;
import com.trogiare.repo.ConverStationRepo;
import com.trogiare.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConverstationComponent {
    @Autowired
    private  ConverStationRepo converStationRepo;
    private static Map<List<String>, ConverStation> converStationMap = new HashMap<>();

    public  ConverStation getConverStation(List<String> listPraList) {
        if(listPraList == null){
            return null;
        }
        ConverStation converStation = converStationMap.get(listPraList);
        if(converStation != null){
            return converStation;
        }
        converStation = converStationRepo.findByParticipants(listPraList);
        converStationMap.put(Collections.unmodifiableList(listPraList),converStation);
        return converStationMap.get(listPraList);
    }
    public static void main(String[] args){
        List<Integer> x = Arrays.asList(1, 2, 3);
        x.stream().sorted();
        System.out.println(x);
    }

}
