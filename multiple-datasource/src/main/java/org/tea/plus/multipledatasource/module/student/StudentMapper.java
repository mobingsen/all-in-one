package org.tea.plus.multipledatasource.module.student;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author mbs on 2021-06-26 14:14
 */
@Mapper
public interface StudentMapper {

    @Select("SELECT * FROM student")
    List<StudentModel> selectAll();
}
