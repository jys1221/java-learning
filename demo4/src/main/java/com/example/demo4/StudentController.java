package com.example.demo4;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class StudentController {

    private final StudentMapper studentMapper;
    private final JdbcTemplate jdbcTemplate;

    public StudentController(StudentMapper studentMapper, JdbcTemplate jdbcTemplate) {
        this.studentMapper = studentMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentMapper.findAll();
    }

    @GetMapping("/student/{id}")
    public Student getStudentById(@PathVariable int id) {
        String sql = "SELECT * FROM student WHERE id = " + id;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setId(rs.getInt("id"));
            s.setName(rs.getString("name"));
            s.setAge(rs.getInt("age"));
            s.setScore(rs.getDouble("score"));
            return s;
        });
    }

    @GetMapping("/add")
    public String addStudent(@RequestParam String name, @RequestParam int age, @RequestParam double score) {
        String sql = "INSERT INTO student (name,age,score) VALUES ('" + name + "'," + age + "," + score + ")";
        jdbcTemplate.update(sql);
        return "新增成功";
    }

    @GetMapping("/delete")
    public String deleteStudent(@RequestParam int id) {
        String sql = "DELETE FROM student WHERE id = " + id;
        jdbcTemplate.update(sql);
        return "删除成功";
    }

    @GetMapping("/update")
    public String updateStudent(@RequestParam int id, @RequestParam String name, @RequestParam int age, @RequestParam double score) {
        String sql = "UPDATE student SET name='" + name + "', age=" + age + ", score=" + score + " WHERE id=" + id;
        jdbcTemplate.update(sql);
        return "修改成功";
    }
    @GetMapping("/student2/{id}")
    public Student getStudent2(@PathVariable int id) {

                return studentMapper.findById(id);

    }
    @GetMapping("/add2")
    public String addStudent2(@RequestParam String name, @RequestParam int age, @RequestParam double score) {
        studentMapper.insert(name,age,score);
        return "新增成功";
    }
    @GetMapping("/delete2")
    public String deleteStudent2(@RequestParam int id) {
        studentMapper.delete(id);
        return "删除成功";
    }
    @GetMapping("/update2")
    public String updateStudent2( @RequestParam int id,@RequestParam String name, @RequestParam int age, @RequestParam double score) {
        studentMapper.update(id,name,age,score);
        return "修改成功";
    }
    @GetMapping("/currentUser")
    public Map<String, String> currentUser() {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String name=auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        return Map.of("name",name,"role",role);

    }
}
