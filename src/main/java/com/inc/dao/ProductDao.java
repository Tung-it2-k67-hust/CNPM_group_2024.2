package com.inc.dao;

import com.inc.POJO.Product;
import com.inc.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

    @Modifying
    @Transactional
    void updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

    ProductWrapper getProductById(@Param("id") Integer id);
}
