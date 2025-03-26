package com.inc.restImpl;

import com.inc.constants.CafeConstant;
import com.inc.rest.ProductRest;
import com.inc.service.ProductService;
import com.inc.utils.CafeUtils;
import com.inc.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ProductRestImpl implements ProductRest {
    @Autowired
    ProductService productService;
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
            return productService.addNewProduct(requestMap);
        }catch (Exception ex){
            log.error("Exception in addNewProduct", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            return productService.updateProduct(requestMap);
        }catch (Exception ex){
            log.error("Exception in updateProduct", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            return productService.deleteProduct(id);
        }catch (Exception ex){
            log.error("Exception in deleteProduct", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            return productService.updateStatus(requestMap);
        }catch (Exception ex){
            log.error("Exception in updateStatus", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return productService.getByCategory(id);
        } catch (Exception ex) {
            log.error("Exception in getByCategory", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return productService.getProductById(id);
        } catch (Exception ex) {
            log.error("Exception in getProductById", ex);
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return productService.getAllProduct();
        } catch (Exception ex) {
            log.error("Exception in getAllProduct", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
