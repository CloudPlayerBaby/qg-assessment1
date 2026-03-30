package com.yuriyuri;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//文档使用
@OpenAPIDefinition(
        info = @Info(
                title = "宿舍报修系统接口文档",
                description = "接口文档",
                version = "V1.0",
                contact = @Contact(name = "云玩家崽崽",url="")
        )
)
@SpringBootApplication
@MapperScan("com.yuriyuri.mapper")
public class RepairSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepairSystemApplication.class, args);
    }

}
