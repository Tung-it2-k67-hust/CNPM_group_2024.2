package com.inc.service;

import com.inc.POJO.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService
{
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap);
    public ResponseEntity<List<Category>> getAllCategory(String filterValue);
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
