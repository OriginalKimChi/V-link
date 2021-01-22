package kr.co.vlink.Vlink.persistance;

import kr.co.vlink.Vlink.domain.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends MongoRepository<Reply, String> {
    List<Reply> findByUser_Id(String userId);
}
