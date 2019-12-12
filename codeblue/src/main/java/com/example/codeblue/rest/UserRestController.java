package com.example.codeblue.rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.codeblue.service.UserService;
import com.example.codeblue.vo.Answer;
import com.example.codeblue.vo.AnswerComment;
import com.example.codeblue.vo.Expert;
import com.example.codeblue.vo.Hospital;
import com.example.codeblue.vo.NoticeBoard;
import com.example.codeblue.vo.QuestionBoard;
import com.example.codeblue.vo.QuestionComment;
import com.example.codeblue.vo.User;
@RestController
public class UserRestController {
	@Autowired UserService userService;
	//질문 답변 추가하기
	@PostMapping("/rest/answerAdd")
	public int answerAdd(Answer answer) {
		System.out.println("::: post - answerAdd :::");
		System.out.println(answer.toString());
		return userService.addAnswer(answer);
	}
	//질문 답변 리스트 가져오기 
	@PostMapping("/rest/getAnswerList")
	public List<Answer> getAnswerList(int questionId){
		System.out.println("::: post - getAnswerList :::");
		System.out.println("questionId : "+ questionId);
		return userService.getAnswerList(questionId);
	}
	//질문 답변 댓글 리스트 가져오기
	@PostMapping("/rest/getAnswerCommentList")
	public List<AnswerComment> getAnswerCommentList(int answerId){
		System.out.println("::: post - getAnswerCommentList :::");
		System.out.println("answerId : "+ answerId);
		return userService.getAnswerCommentList(answerId);
	}
	
	//분야별 질문 리스트 출력
	@GetMapping("getBoardListByFeild")
	public List<QuestionBoard> getBoardListByFeild(){
		
		return null;
	}
	
	@GetMapping("/rest/getLoginUser")
	public User getLoginUser(HttpSession session) {
		System.out.println("::: get - getLoginUser :::"); 
		return (User) session.getAttribute("loginUser");
	}
	
	@PostMapping("/rest/userOne")
	public User getUserOne(String userId) {
		System.out.println("::: post - getUserOne :::"); 
		return userService.selectUserOne(userId);
	}
	
	// 댓글 입력하기
	// 댓글 전체리스트 가져오기
	@PostMapping("/rest/getQuestionCommentList")
	public List<QuestionComment> getCommentList(int questionId){
		System.out.println("::: post - getQuestionCommentList :::");
		System.out.println("questionId : "+questionId);
		return userService.getQuestionCommentList(questionId);
		
	}
	
	// 질문 리스트 가져오기
	@PostMapping("/rest/getBoardList")
	public Map<String,Object> getBoardList(	@RequestParam(value="currentPage",defaultValue = "1")int currentPage,
											@RequestParam(value="rowPerPage",defaultValue = "10")int rowPerPage,
											@RequestParam(value="searchWord", required = false)String searchWord,
											@RequestParam(value="searchCategory", defaultValue = "today")String searchCategory,
											@RequestParam(value="feildId", defaultValue = "99")int feildId) {
		System.out.println("::: post - getBoardList :::");
		System.out.println("currentPage : "+ currentPage);
		System.out.println("rowPerPage : "+ rowPerPage);
		System.out.println("searchWord : "+ searchWord);
		System.out.println("feildId : "+ feildId);
		System.out.println("searchCategory :"+searchCategory);
		
		return userService.getQuestBoardList(currentPage, rowPerPage,searchWord,searchCategory, feildId);
	}
	@PostMapping("/rest/questionBoardOne")
	public QuestionBoard QuestionBoardOne(@RequestParam(value="questionId" , required=true)int questionId) {
		System.out.println("::: post - QuestionBoardOne :::");
		System.out.println("questionId : "+questionId);
		QuestionBoard questionBoard = userService.getQuestionBoardOne(questionId);
		
		System.out.println(questionBoard.toString());
		
		return questionBoard;
	}
	
	@GetMapping("/rest/getNoticeList")
	public Map<String,Object> getNoticeList() {
		System.out.println("::: get - getnoticeList :::"); 
		return userService.getNoticeBoardList(1, 15);
		
	}
	
	@PostMapping("/rest/getNoticeList")
	public Map<String,Object> getNoticeList(@RequestParam(value="currentPage", defaultValue = "1")int currentPage,
									 @RequestParam(value="rowPerPage", defaultValue = "15")int rowPerPage) {
		System.out.println("::: post - getnoticeList :::"); 
		System.out.println("currentPage : " + currentPage);
		System.out.println("rowPerPage : " + rowPerPage);
		
		Map<String,Object> map = userService.getNoticeBoardList(currentPage, rowPerPage);
		System.out.println("리턴!");
		System.out.println(map.toString());
		return userService.getNoticeBoardList(currentPage, rowPerPage);
	}
	
	@PostMapping("/rest/noticeOne")
	public NoticeBoard getNoticeOne(int noticeId) {
		System.out.println("::: post - getNoticeOne :::");
		
		return userService.getNoticeBoardOne(noticeId);
	}
	

	@PostMapping("/rest/resetPassword")
	public int postAccountRecovery(User user) {
		System.out.println("::: post - resetPassword :::");
		System.out.println("User"+ user);
		
		return userService.modifyUserPw(user);
	}
	
	@PostMapping("/rest/verifyUserForReset")
	public String verifyUserForReset(HttpSession session,User user) { 
		System.out.println("::: post - verifyUserForReset :::");
		System.out.println(user.toString());
		
		String result = userService.getUserIdForCheck(user); 
		if(result == null) {
			System.out.println("noSuchUser!! ");
			return "noSuchUser";
		}
		
		session.setAttribute("verifyCode", result);
		return "success";
	}
	
	@PostMapping("/rest/sendEmailToConfirm")
	public String sendEmailToConfirm(HttpSession session, User user) {
		System.out.println("::: post - checkCode :::");
		
		System.out.println(user.toString());		
		String code = userService.sendCodeToMail(user);
		session.setAttribute("verifyCode", code);
		
		return "success";
	}
	
	// verify code & add User
	@PostMapping("/rest/verifyCode")
	public boolean verifyCode(User user, HttpSession session, String code) {
		System.out.println("::: post - verifyCode :::");
		System.out.println(user.toString() + "입력받은 코드:" +code);
		
		if(session.getAttribute("verifyCode").equals(code) != true) {
			System.out.println("!password not correct");
			return false;
		}
		
		System.out.println("code correct!");
		userService.addUser(user); 
		
		return true;
	}
	// verify code & add User
	@PostMapping("/rest/verifyCodeForRest")
	public boolean verifyCodeForRest(HttpSession session, String code) {
		System.out.println("::: post - verifyCodeForRest :::");
		
		if(session.getAttribute("verifyCode").equals(code) != true) {
			System.out.println("!password not correct");
			System.out.println("code : "+code+", verifyCode: "+session.getAttribute("verifyCode"));
			return false;
		}
		
		System.out.println("code correct!");	
		return true;
	}
	
	@PostMapping("/rest/addExpert")
	public boolean addExpert(Expert expert) {
		System.out.println("::: post - addExpert :::");    
		System.out.println("영향을 받은행: "+userService.addExpert(expert)); 
		return true;
	}
	
	//병원 리스트
	@PostMapping("/rest/hospital")
	public Map<String, Object>postHospital(@RequestParam(value="currentPage", defaultValue="1")int currentPage,
										@RequestParam(value="rowPerPage", defaultValue="10")int rowPerPage,
										   @RequestParam(value="searchWord", required = false)String searchWord) {
	System.out.println(":::post - postHospital:::");
	System.out.println("currentPage"+currentPage + " //rowPerPage"+rowPerPage + " //searchWord"+searchWord);
	
	return userService.getHospitalList(currentPage, rowPerPage, searchWord);
	}
	
	//병원 상세페이지
	@PostMapping("/rest/hospitalOne")
	public List<Hospital> hospitalOne(@RequestParam(value="hospitalId")int hospitalId){
		System.out.println(":::post - getHospitalOne");
		System.out.println("hospitalId"+hospitalId);
		
		return userService.getHospitalOne(hospitalId);
	}
	// 유저 조회
	@PostMapping("/rest/getUserList")
	public Map<String,Object> getUserList(@RequestParam(value="currentPage", defaultValue = "1")int currentPage,
			@RequestParam(value="rowPerPage", defaultValue = "36")int rowPerPage,
			@RequestParam(value="searchWord", required = false)String searchWord) {
		System.out.println("::: post - /getUserList :::");
		System.out.println("currentPage : "+currentPage);
		System.out.println("rowPerPage : "+rowPerPage);
		System.out.println("searchWord : "+searchWord);
		return userService.getUserList(currentPage, rowPerPage,searchWord);
	}
}
