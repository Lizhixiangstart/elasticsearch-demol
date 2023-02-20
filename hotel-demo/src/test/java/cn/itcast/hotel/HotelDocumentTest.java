package cn.itcast.hotel;/**
 * @ClassName HotelDocumentTest
 * @Description TODO
 * @Author LiZhixiang
 * @Date 2023/2/20 20:54
 * @Version 1.0
 */

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.impl.HotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 @Description:
 @Auth:Lzx
 @Create:2023-022023/2/2020-54
 @Version:1.0
 */
@SpringBootTest
public class HotelDocumentTest {
    RestHighLevelClient client;

    @Autowired
    HotelService hotelService;
    @BeforeEach
    void setUp(){
        client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.72.130:9200")));
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
    }

    @DisplayName("测试向索引库中插入文档")
    @Test
    void testAddDocument() throws IOException {
        Hotel hotel = hotelService.getById(61083L);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        String json = JSON.toJSONString(hotelDoc);
        IndexRequest request = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        request.source(json, XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @DisplayName("查询文档")
    @Test
    void testGetDocumentById() throws IOException {
        GetRequest request = new GetRequest("hotel", "38609");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String json = response.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        System.out.println(hotelDoc);
    }

    @DisplayName("测试删除文档")
    @Test
    void testDeleteDocuemnt() throws IOException {
        DeleteRequest request = new DeleteRequest("hotel", "61083");
        client.delete(request,RequestOptions.DEFAULT);
    }

    @DisplayName("")
    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("hotel", "61083");
        request.doc(
                "price","952",
                "starName","四钻"
        );
        client.update(request,RequestOptions.DEFAULT);
    }

    @DisplayName("测试批量插入文档")
    @Test
    void testBulkRequest() throws IOException {
        List<Hotel> hotels = hotelService.list();
        BulkRequest request = new BulkRequest();
        for (Hotel hotel : hotels ){
            HotelDoc hotelDoc = new HotelDoc(hotel);
            request.add(new IndexRequest("hotel")
                    .id(hotelDoc.getId().toString())
                    .source(JSON.toJSONString(hotelDoc),XContentType.JSON)
            );
            client.bulk(request,RequestOptions.DEFAULT);
        }
    }

}
