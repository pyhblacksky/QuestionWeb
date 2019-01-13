package com.nowcoder;

import com.nowcoder.DAO.UserDAO;
import com.nowcoder.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {

	@Autowired
	UserDAO userDAO;

	@Test
	public void initDatabase() {
		Random random = new Random();
		for(int i= 0; i < 11; i++){
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER %d", i));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);
		}

	}

}

