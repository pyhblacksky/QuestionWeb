package com.nowcoder.service;

import com.nowcoder.DAO.QuestionDAO;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDAO questionDAO;


    //增加问题
    public int addQuestion(Question question){
        //敏感词过滤

        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    //获取最新问题
    public List<Question> getLastestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

}
