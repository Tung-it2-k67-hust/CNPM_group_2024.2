package com.inc.restImpl;


import com.inc.POJO.Category;
import com.inc.constants.CafeConstant;
import com.inc.rest.CategoryRest;
import com.inc.service.CategoryService;
import com.inc.utils.CafeUtils;
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
public class CategoryRestImpl implements CategoryRest {

    //private static final Logger log = LoggerFactory.getLogger(CategoryRestImpl.class);
    @Autowired
    CategoryService categoryService;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            return categoryService.addNewCategory(requestMap);
        }catch (Exception ex){
            log.error("Exception in addNewCategory", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            return categoryService.getAllCategory(filterValue);
        } catch (Exception ex) {
            log.error("Exception in getAllCategory", ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            return categoryService.updateCategory(requestMap);
        }catch (Exception ex){
            log.error("Exception in updateCategory", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}