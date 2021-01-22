package kr.co.vlink.Vlink.service;

import kr.co.vlink.Vlink.domain.Product;
import kr.co.vlink.Vlink.domain.Video;
import kr.co.vlink.Vlink.domain.VideoProduct;
import kr.co.vlink.Vlink.dto.ProductDTO;
import kr.co.vlink.Vlink.dto.SearchDTO;
import kr.co.vlink.Vlink.dto.VideoDTO;
import kr.co.vlink.Vlink.persistance.ProductRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VideoService videoService;

    public Product findProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product addViewCount(String id) {
        Product product = findProductById(id);
        if(product != null) {
            product.addViewCount();
            return productRepository.save(product);
        }
        return null;
    }

    public Product findProductByProductID(String productId) {
        return productRepository.findByProductId(productId).orElse(null);
    }

    public List<VideoProduct> createOrUpdateProductByVideoProductList(VideoDTO videoDTO) {
        List<VideoProduct> productList = videoDTO.getProductList();
        List<VideoProduct> newProductList = new ArrayList<>();

        for (VideoProduct videoProduct : productList) {
            Product partnersProduct = videoProduct.getProduct();
            Product product = findProductByProductID(partnersProduct.getProductId());

            if (product != null) {
                product.updateData(partnersProduct.getProductName(), partnersProduct.getProductImageURL(), partnersProduct.getPrice());
                videoProduct.updateProduct(productRepository.save(product));
            } else {
                videoProduct.updateProduct(productRepository.save(Product.builder()
                        .productId(partnersProduct.getProductId())
                        .productName(partnersProduct.getProductName())
                        .productImageURL(partnersProduct.getProductImageURL())
                        .price(partnersProduct.getPrice())
                        .productURL(partnersProduct.getProductURL())
                        .build()));
            }
            newProductList.add(videoProduct);
        }

        return newProductList;
    }

    public List<ProductDTO> getSearchDataFromHimart(String keyword) throws IOException {
        String HIMART_SEARCH_URL = "http://www.e-himart.co.kr/app/search/totalSearch?query=";
        String url = HIMART_SEARCH_URL + keyword;
        Document doc = Jsoup.connect(url).get();
        Elements products = doc.getElementsByClass("prdItem");

        List<ProductDTO> productList = new ArrayList<>();
        if(!products.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                Element product = products.get(i);
                String productId = product.attr("goodsno");
                String productImg = product.select("div a .prdImg img").attr("src");
                String productName = product.select("div a .prdInfo p").text();
                String productPrice = product.select("div a .prdInfo div .priceInfo").first().select("span strong").text();
                String link = product.select("div a").attr("href");
                productList.add(ProductDTO.builder()
                        .productId(productId)
                        .productImageURL(productImg)
                        .productName(productName)
                        .price(productPrice)
                        .productURL("http://www.e-himart.co.kr" + link)
                        .build());
            }
        }
        return productList;
    }

    public Page<Product> findProductListByKeyword(SearchDTO searchDTO) {
        String keyword = ".*.*";
        int page = 0;

        if(searchDTO.getKeyword() != null) {
            String keywordString = searchDTO.getKeyword().replace(" ", ".*)(.*");
            keyword = "(?i)(.*" + keywordString + ".*)";
        }
        if(searchDTO.getPage() != null && searchDTO.getPage() > 0) {
            page = searchDTO.getPage() - 1;
        }
        Pageable pageable = PageRequest.of(page, 10);
        return productRepository.findByProductNameRegex(keyword, pageable);
    }

    public List<Product> findTop3ProductByProductName(SearchDTO searchDTO) {
        String productName = ".*.*";
        int page = 0;

        if(searchDTO.getKeyword() != null) {
            String keywordString = searchDTO.getKeyword().replace(" ", ".*)(.*");
            productName = "(?i)(.*" + keywordString + ".*)";
        }
        return productRepository.findTop3ByProductNameRegex(productName);
    }

    public List<Product> findTop5ProductByViewCount() {
        return productRepository.findTop5ByOrderByViewCountDesc();
    }

    public HashMap<String, Product> findTop3ProductByPartnerIdAndViewCount(String partnersId) {
        List<Video> videoList = videoService.findProductListByPartnersId(partnersId);
//        List<String> productIdList = new ArrayList<>();
//        for(Video video : VideoList) {
//            for(VideoProduct videoProduct : video.getProductList()) {
//                productIdList.add(videoProduct.getProduct().getId());
//            }
//        }
        if(videoList.size() > 0 && videoList != null) {
            List<VideoProduct> videoProductList = new ArrayList<>();
            HashMap<String, Product> productHashMap = new HashMap<>();
            for(Video video : videoList) {
                for(VideoProduct videoProduct : video.getProductList()) {
                    videoProductList.add(videoProduct);
                }
            }

            Comparator<VideoProduct> compareByCount = (VideoProduct v1, VideoProduct v2) -> v1.getCount().compareTo(v2.getCount());
            Collections.sort(videoProductList, compareByCount);

            for(VideoProduct videoProduct : videoProductList) {
                if(productHashMap.size() > 2) break;
                productHashMap.put(videoProduct.getProduct().getId(), findProductById(videoProduct.getProduct().getId()));
            }

            return productHashMap;
        }
        return null;
    }
}
