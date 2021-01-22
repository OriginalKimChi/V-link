package kr.co.vlink.Vlink.contoller;

import kr.co.vlink.Vlink.domain.Product;
import kr.co.vlink.Vlink.dto.ProductDTO;
import kr.co.vlink.Vlink.dto.ResultDTO;
import kr.co.vlink.Vlink.dto.SearchDTO;
import kr.co.vlink.Vlink.service.ProductService;
import kr.co.vlink.Vlink.util.AuthCodeGenerator;
import kr.co.vlink.Vlink.util.KakaoRestApiHelper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductRestController {

    @Autowired
    private ProductService productService;
    @Autowired
    private KakaoRestApiHelper kakaoRestApiHelper;
    @Autowired
    private AuthCodeGenerator authCodeGenerator;

    @PostMapping("/find")
    public ResultDTO findProductById(@RequestBody ProductDTO productDTO) {
        ResultDTO resultDTO = new ResultDTO();
       Product product = productService.addViewCount(productDTO.getId());
        if (product != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(product);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Find failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/detect")
    public ResponseEntity detectProductFromImage(String base64) throws IOException {
        String testRootPath = "c:/";
        String deployRootPath = "/";

        base64 =  base64.replaceAll("data:application/octet-stream;base64,","");
        byte[] decode = Base64.decodeBase64(base64);
        String fileName = authCodeGenerator.getRandomCode(10);

        Path destinationFile = Paths.get(testRootPath, fileName + ".png");
        Files.write(destinationFile, decode);
        String filePath = testRootPath + fileName + ".png";

        return kakaoRestApiHelper.detectProductFromImage(filePath);
    }

    @PostMapping("/search")
    public ResultDTO findProducts(@RequestBody SearchDTO searchDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            Page<Product> productList = productService.findProductListByKeyword(searchDTO);
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(productList);
        } catch (Exception e) {
            resultDTO.setCode(500);
            resultDTO.setMessage(e.toString());
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/search-himart")
    public ResultDTO getProductByProductName(@RequestBody ProductDTO productDTO) throws IOException {
        List<ProductDTO> productList = productService.getSearchDataFromHimart(productDTO.getProductName());

        ResultDTO resultDTO = new ResultDTO();
        if(productList.size() != 0) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(productList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("parsing failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/top-view")
    public ResultDTO findTop5ProductByViewCount() {
        ResultDTO resultDTO = new ResultDTO();
        List<Product> productList = productService.findTop5ProductByViewCount();
        if (productList != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(productList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("No data");
            resultDTO.setData(null);
        }
        return resultDTO;
    }
}
