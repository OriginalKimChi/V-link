package kr.co.vlink.Vlink.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

@Slf4j
@Component
public class KakaoRestApiHelper {

    @Value("${kakao.restapi.key}")
    private String restApiKey;

    private static final String API_SERVER_HOST  = "https://dapi.kakao.com";
    private static final String PRODUCT_DETECT_PATH = "/v2/vision/product/detect";

    public ResponseEntity<String> detectProductFromImage(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "KakaoAK " + restApiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("image", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        URI url = URI.create(API_SERVER_HOST + PRODUCT_DETECT_PATH);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class);

        file.delete();
        return response;
    }

}
