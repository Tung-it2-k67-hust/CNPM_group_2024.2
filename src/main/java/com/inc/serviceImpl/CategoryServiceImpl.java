package com.inc.serviceImpl;


import com.inc.JWT.JwtFilter;
import com.inc.POJO.Category;
import com.inc.constants.CafeConstant;
import com.inc.dao.CategoryDao;
import com.inc.service.CategoryService;
import com.inc.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    //private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap, false)){
                    categoryDao.save(getCategoryFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Category Added Successfully", HttpStatus.OK);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            log.error("Exception occurred while adding category", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd){
        Category category = new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            //return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            if(StringUtils.hasText(filterValue) && filterValue.equalsIgnoreCase("true")){
                System.out.println("Inside getAllCategory");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        }catch (Exception ex){
            log.error("Exception occurred while getting all category", ex);
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()) {
                if(validateCategoryMap(requestMap, true)){
                    Optional<Category> optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(optional.isPresent()){
                        categoryDao.save(getCategoryFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
                    }else{
                        return CafeUtils.getResponseEntity("Category id doesn't not exist", HttpStatus.OK);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            else{
                return CafeUtils.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            log.error("Exception occurred while updating category", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}