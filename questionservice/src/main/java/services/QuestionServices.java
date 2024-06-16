package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dao.QuestionsDao;
import model.Questions;
import model.QuizWrapper;
import model.Response;

@Service
public class QuestionServices {
	
	@Autowired
	QuestionsDao questiondao;
	
	public ResponseEntity<List<Questions>> getAllQuestion()
	{
		try {
			return new ResponseEntity<>(questiondao.findAll(), HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<List<Questions>> getQuestionsByCategory(String category){
		
		try {
			return new ResponseEntity<>(questiondao.findByCategory(category),HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<String> addQuestion(Questions question)
	{
		
		questiondao.save(question);
		return new ResponseEntity<>("success",HttpStatus.CREATED);
	}

	public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
		
		 List<Integer> questions = questiondao.findRandomQuestionsByCategory(categoryName, numQuestions);
	       
		 return new ResponseEntity<>(questions, HttpStatus.OK);
	}

	public ResponseEntity<List<QuizWrapper>> getQuestionsFromId(List<Integer> questionIds) {
		 List<QuizWrapper> wrappers = new ArrayList<>();
	        List<Questions> questions = new ArrayList<>();

	        for(Integer id : questionIds){
	            questions.add(questiondao.findById(id).get());
	        }

	        for(Questions question : questions){
	            QuizWrapper wrapper = new QuizWrapper();
	            wrapper.setQuestion_id(question.getQuestion_id());
	            wrapper.setQuestion_text(question.getQuestion_text());
	            wrapper.setOption_a(question.getOption_a());
	            wrapper.setOption_b(question.getOption_b());
	            wrapper.setOption_c(question.getOption_c());
	            wrapper.setOption_d(question.getOption_c());
	            wrappers.add(wrapper);
	        }

	        return new ResponseEntity<>(wrappers, HttpStatus.OK);
	}

	public ResponseEntity<Integer> getScore(List<Response> responses) {
		 int right = 0;

	        for(Response response : responses){
	            Questions question = questiondao.findById(response.getId()).get();
	            if(response.getResponse().equals(question.getCorrect_option()))
	                right++;
	        }
	        return new ResponseEntity<>(right, HttpStatus.OK);
	    }
	}
	
	
	
	


