package org.tea.plus.multipledatasource.module.student;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void selectAll() {
        List<StudentModel> studentModelList = studentMapper.selectAll();
        Assertions.assertNotNull(studentModelList);
        studentModelList.stream().map(JSON::toJSONString).forEach(System.out::println);
    }
}
