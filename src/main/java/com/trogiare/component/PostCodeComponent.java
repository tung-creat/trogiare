package com.trogiare.component;

import com.trogiare.model.PostCode;
import com.trogiare.repo.PostCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostCodeComponent {
    @Autowired
    private PostCodeRepo postCodeRepo;
    private static Long numberCode = 0l;
    public String getCode() {
        PostCode postCode = new PostCode();
        if(numberCode == 0){
            try{
                numberCode = postCodeRepo.getRecordPostCode();
            }catch (Exception ex){
                numberCode = 1l;
            }
            if(numberCode == null){
                numberCode = 1l;
                postCode.setNumberCode(numberCode);
                postCodeRepo.save(postCode);
            }
            return  String.format("%07d", numberCode);
        }
        numberCode += 1;
        postCode.setNumberCode(numberCode);
        postCodeRepo.save(postCode);
        String result = String.format("%07d", numberCode);
        return result;
    }
}

