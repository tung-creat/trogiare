package com.trogiare.controller;

import com.trogiare.model.Districts;
import com.trogiare.model.Provinces;
import com.trogiare.model.Wards;
import com.trogiare.repo.DistrictsRepo;
import com.trogiare.repo.ProvincesRepo;
import com.trogiare.repo.WardsRepo;
import com.trogiare.respone.MessageResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/v1/provinces")
public class VietNamProvincesCtrl {
    @Autowired
    private ProvincesRepo provincesRepo;
    @Autowired
    private DistrictsRepo districtsRepo;
    @Autowired
    private WardsRepo wardsRepo;
    @RequestMapping(path = "/",method = RequestMethod.GET)
    @ApiOperation(value = "get all provinces in VietNam", response = MessageResp.class)
    public HttpEntity<?> getProvinces(){
        List<Provinces> provincesList = (List<Provinces>) provincesRepo.findAll();
        return ResponseEntity.ok().body(MessageResp.ok(provincesList));
    }
    @RequestMapping(path="/get-districts-by-provice-id/{idProvince}",method = RequestMethod.GET)
    @ApiOperation(value = "get all districts for by id provinces", response = MessageResp.class)
    public HttpEntity<?> getDistricByProvinceId(@PathVariable(name="idProvince") String idProvince){
        List<Districts> districtsList = districtsRepo.findByProvinceCode(idProvince);
        return ResponseEntity.ok().body(MessageResp.ok(districtsList));
    }
    @RequestMapping(path="/get-wards-by-districts-id/{idDistrict}",method = RequestMethod.GET)
    @ApiOperation(value = "get all wards by districts id", response = MessageResp.class)
    public HttpEntity<?> getWardsByDistrictId(@PathVariable(name="idDistrict") String idDistrict){
        List<Wards> wardsList = wardsRepo.findByDistrictCode(idDistrict);
        return ResponseEntity.ok().body(MessageResp.ok(wardsList));
    }
}
