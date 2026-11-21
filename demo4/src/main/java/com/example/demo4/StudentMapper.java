package com.example.demo4;
import java.util.List;
import com.example.demo4.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface StudentMapper {
    @Select("SELECT * FROM student")
    List<Student> findAll();

    @Select("SELECT * FROM student WHERE id=#{id}")
    Student findById(int id);

    @Insert("INSERT INTO student (name,age,score) VALUES (#{name},#{age},#{score})")
    int insert(String name, int age, double score);

    @Delete("DELETE FROM student WHERE id=#{id}")
    int delete(int id);

    @Update("UPDATE student SET name=#{name},age=#{age},score=#{score} WHERE id=#{id}")
    int update(int id, String name, int age, double score);
}