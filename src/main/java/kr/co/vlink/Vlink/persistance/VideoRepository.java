package kr.co.vlink.Vlink.persistance;

import kr.co.vlink.Vlink.domain.Video;
import kr.co.vlink.Vlink.domain.VideoMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    Page<VideoMapping> findByVideoNameRegexOrderByCreateAtDesc(String keywordSearch, Pageable pageable);
    List<VideoMapping> findTop3ByVideoNameRegexOrderByViewCountDesc(String videoName);
    Page<VideoMapping> findByPartners_IdInOrderByCreateAtDesc(List<String> partnersIdList, Pageable pageable);
    List<VideoMapping> findTop3ByProductList_Product_IdOrderByViewCountDesc(String productId);
    List<VideoMapping> findTop3ByOrderByViewCountDesc();
    List<Video> findTop3ByPartners_IdOrderByViewCountDesc(String partnersId);
    Page<Video> findByPartners_IdOrderByCreateAtDesc(String partnersId, Pageable pageable);
    List<Video> findByPartners_Id(String partnersId);
    Optional<VideoMapping> findOneById(String videoId);
    Page<VideoMapping> findByTagListOrderByViewCountDesc(String tag, Pageable pageable);
}
