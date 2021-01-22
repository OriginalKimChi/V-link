package kr.co.vlink.Vlink.persistance;

import kr.co.vlink.Vlink.domain.User;
import kr.co.vlink.Vlink.domain.UserMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    UserMapping findOneById(String userId);
    List<User> findByFollowList_Id(String followId);
}
