package kr.co.vlink.Vlink.persistance;

import kr.co.vlink.Vlink.domain.HashTag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashTagRepository extends MongoRepository<HashTag, String> {
    Optional<HashTag> findByHashTag(String hashTag);
    List<HashTag> findTop5ByOrderByCountDesc();
}
