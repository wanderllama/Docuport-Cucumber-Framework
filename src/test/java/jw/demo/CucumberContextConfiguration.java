package jw.demo;

import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

//TODO update with the class replacing => src/main/java/jw/demo/MyApplication.java
@ContextConfiguration(classes = MyApplication.class, loader = SpringBootContextLoader.class)
@SpringBootTest(classes = MyApplication.class)
@io.cucumber.spring.CucumberContextConfiguration
public class CucumberContextConfiguration {
}

