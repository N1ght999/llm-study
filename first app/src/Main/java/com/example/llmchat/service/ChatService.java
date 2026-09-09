// ========== 原有代码：包声明 ==========
package com.example.llmchat.service;
// ==============================================================


// ========== 新增代码：导入HTTP相关类 ==========
import java.util.HashMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
// ==============================================================


// ========== 原有代码：服务注解和类定义 ==========
import org.springframework.stereotype.Service;

@Service
public class ChatService {
// ==============================================================


    // ========== 新增代码：常量和HTTP客户端实例 ==========
    // Python常驻服务的地址，和Python端保持一致
    private static final String PYTHON_SERVICE_URL = "http://127.0.0.1:5000/chat";
    // Spring自带的HTTP客户端，不用额外加依赖
    private final RestTemplate restTemplate = new RestTemplate();
    // ==============================================================


    // ========== 原有代码：方法签名（完全不变，Controller不用改） ==========
    /**
     * 对接你的 Python LLM
     * @param userMessage 用户输入内容
     * @return LLM 回复内容
     */
    public String callLLM(String userMessage) {
        // ==============================================================


        // ========== 替换修改：方法体全部替换为HTTP调用逻辑 ==========
        // 原代码：ProcessBuilder启动Python进程、读取输出流的逻辑全部删除
        try {
            // 1. 组装请求体，和Python端接收的格式对应
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("message", userMessage);


            // 2. 设置请求头，声明是JSON格式
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 3. 封装成HTTP请求实体
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            // 4. 发送POST请求，接收Map格式的返回结果
            ResponseEntity<Map> response = restTemplate.exchange(
                    PYTHON_SERVICE_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // 5. 从返回结果里取出Python返回的回复文本
            Map<String, Object> resultBody = response.getBody();
            return resultBody != null ? (String) resultBody.get("result") : "空回复";

        } catch (Exception e) {
            e.printStackTrace();
            return "调用失败：" + e.getMessage();
        }
        // ==============================================================


        // ========== 原有代码：方法结束 ==========
    }
}
// ==============================================================
