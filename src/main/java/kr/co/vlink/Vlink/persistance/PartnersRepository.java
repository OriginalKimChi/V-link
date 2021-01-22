package kr.co.vlink.Vlink.persistance;

import kr.co.vlink.Vlink.domain.Partners;
import kr.co.vlink.Vlink.domain.PartnersMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnersRepository extends MongoRepository<Partners, String> {
    Optional<Partners> findByEmail(String email);
    Optional<Partners> findByEmailAndAuthState(String email, Boolean authState);
    List<Partners> findByAuthState(Boolean authState);
    List<PartnersMapping> findTop3ByNameRegex(String partnersName);
    PartnersMapping findOneById(String partnersId);
    Page<PartnersMapping> findByNameRegexOrderByCreateAtDesc(String keywordSearch, Pageable pageable);
}
