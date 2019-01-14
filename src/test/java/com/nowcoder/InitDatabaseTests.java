package com.nowcoder;

import com.nowcoder.DAO.CommentDAO;
import com.nowcoder.DAO.MessageDAO;
import com.nowcoder.DAO.QuestionDAO;
import com.nowcoder.DAO.UserDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {

	@Autowired
	UserDAO userDAO;

	@Autowired
	QuestionDAO questionDAO;

	@Autowired
	MessageDAO messageDAO;

	@Autowired
	CommentDAO commentDAO;

	@Test
	public void initDatabase() {
		Random random = new Random();
		for(int i= 0; i < 11; i++){
			//测试userDAO
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER %d", i));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);

			user.setPassword("xxx");
			userDAO.updatePassword(user);

			//测试questionDAO
			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000 * 3600 + i);
			question.setCreatedDate(date);
			question.setUserId(i + 1);
			question.setTitle(String.format("TITLE %d ", i));
			question.setContent(String.format("This is Content %d ", i));

			questionDAO.addQuestion(question);

			//测试messageDAO
			Message message = new Message();
			message.setContent(String.format("Message content %d", i));
			message.setConversationId(i);
			message.setCreatedDate(date);
			message.setFromid(i);
			message.setToid(i+1);
			messageDAO.addMessage(message);

			//测试CommentDAO
			Comment comment = new Comment();
			comment.setContent(String.format("Comment content %d", i));
			comment.setCreatedDate(date);
			comment.setEntityId(i);
			comment.setUserId(i);
			comment.setEntityType(String.format("EntityType %d", i));
			commentDAO.addComment(comment);
		}

		Assert.assertEquals("xxx", userDAO.selectById(1).getPassword());
		userDAO.deleteById(1);
		Assert.assertNull(userDAO.selectById(1));

		//测试question
		System.out.println(questionDAO.selectLatestQuestions(0,0,10));

		//测试message
		Assert.assertEquals(0,messageDAO.selectMessageById(1).getFromid());

		//测试comment
		Assert.assertEquals(0,commentDAO.selectCommentById(1).getEntityId());
	}

}

