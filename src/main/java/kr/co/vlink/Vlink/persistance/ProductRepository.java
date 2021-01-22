package kr.co.vlink.Vlink.persistance;

import kr.co.vlink.Vlink.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findByProductNameRegex(String keywordSearch, Pageable pageable);
    Optional<Product> findByProductId(String productId);
    List<Product> findTop3ByProductNameRegex(String productName);
    List<Product> findTop5ByOrderByViewCountDesc();
    List<Product> findTop3ByIdInOrderByViewCountDesc(List<String> productIdList);
}
