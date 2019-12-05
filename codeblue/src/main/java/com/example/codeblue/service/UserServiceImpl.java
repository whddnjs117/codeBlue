package com.example.codeblue.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.codeblue.mapper.UserMapper;
import com.example.codeblue.vo.Expert;
import com.example.codeblue.vo.NoticeBoard;
import com.example.codeblue.vo.Page;
import com.example.codeblue.vo.QuestionBoard;
import com.example.codeblue.vo.QuestionComment;
import com.example.codeblue.vo.User;

@Transactional
@Service
public class UserServiceImpl implements UserService{
	@Autowired UserMapper userMapper; 
	@Autowired JavaMailSender javaMailSender;
	
	@Override
	public User selectUserOne(String UserId) {
		System.out.println("::: ProfileServiceImpl - selectUserOne :::");
		User user = userMapper.selectUserOne(UserId);
		
		return user;
	}
	@Override
	public int addQuestion(QuestionBoard questionBoard) {
		System.out.println("::: AskServiceImpl - addQuestion :::");
		return userMapper.insertQuestion(questionBoard);
	}
	@Override
	public List<QuestionComment> getQuestionCommentList(int questionId) {
		System.out.println("::: QuestionBoardServiceImpl - getQuestionCommentList :::");
		return userMapper.selectQuestionCommentList(questionId);
	}
	
	@Override
	public QuestionBoard getQuestionBoardOne(int questionId) {
		System.out.println("::: QuestionBoardServiceImpl - getQuestionBoardOne :::");
		QuestionBoard questionBoard = userMapper.selectQuestionBoardOne(questionId);
		questionBoard.setCommentCount(userMapper.selectQuestionCommentCount(questionId));
		System.out.println(questionBoard.toString());
		return questionBoard;
	}
	
	@Override
	public Map<String,Object> getQuestBoardList(int currentPage, int rowPerPage,String searchWord) {
		System.out.println("::: QuestionBoardServiceImpl - getQuestBoardList :::");
		//페이징객체 생성
		Page page = new Page();
		//페이징 시작값
		int beginRow = (currentPage -1) * rowPerPage;
		//페이징 객체 값을 저장
		page.setBeginRow(beginRow);
		page.setRowPerPage(rowPerPage);
		page.setSearchWord(searchWord);
		//질문 리스트 전체 행의 갯수
		int totalCount = userMapper.selectQuestionBoardTotalCount(page);
		//페이지 마지막값변수선언
		int lastPage = 0;
		//페이지갯수값 저장
		if(totalCount%rowPerPage == 0) {
			lastPage = totalCount/rowPerPage;
		} else {
			lastPage = (totalCount/rowPerPage)+1;
		}
		System.out.println(page.toString());
		// 질문 리스트 저장
		List<QuestionBoard> list = userMapper.selectQuestionBoardList(page);
		// 각질문에 해당하는 갯수 뽑기
		for(int i=0; i<list.size(); i++) {
			list.get(i).setAnswerCount(userMapper.selectQuestionBoardAnswerCount(list.get(i)));
			list.get(i).setVoteCount(userMapper.selectQuestionBoardVotes(list.get(i)));
		}
		System.out.println(list.toString());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("lastPage", lastPage);
		map.put("currentPage",currentPage);
		map.put("list",list);
		map.put("totalCount",totalCount);
		map.put("searchWord",searchWord);
		return map;
	}
	@Override
	public NoticeBoard getNoticeBoardOne(int noticeId) {
		System.out.println("::: NoticeBoardServiceImpl - getNoticeBoardOne :::");
		return userMapper.selectNoticeBoardOne(noticeId);
	}
	
	@Override
	public Map<String, Object> getNoticeBoardList(int currentPage, int rowPerPage) {
		
		List<NoticeBoard> list = new ArrayList<>();
		System.out.println("::: NoticeBoardServiceImpl - getNoticeBoardList :::");
		System.out.println("currentPage : " + currentPage);
		System.out.println("rowPerPage :  " + rowPerPage);
		
		
		Page page = new Page();
		page.setRowPerPage(rowPerPage);
		page.setBeginRow((currentPage-1)*rowPerPage);
		
		int totalRow = userMapper.noticeBoardTotalRow();
		int lastPage = totalRow/rowPerPage;
		
		if(totalRow % rowPerPage != 0) {
			lastPage ++;
		}
		
		list = userMapper.selectNoticeBoardList(page);
		System.out.println(list);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalRow", totalRow);
		map.put("list", list);
		map.put("currentPage", currentPage);
		map.put("rowPerPage", rowPerPage);
		map.put("lastPage", lastPage);
			
		System.out.println(map.toString());
		System.out.println("-------------------");
		return map;
	}
	@Override
	public User verifyUser(User user) {
		System.out.println("::: UserServluceImpl - verifyUser :::");
		return userMapper.selectUserId(user);
	}
	
	//회원가입
	@Override
	public int addUser(User user) { 
		System.out.println("::: UserServluceImpl - addUser :::");
		int rs = userMapper.insertUser(user);
		return rs;
	}
	
	//회원가입 메일 보내기
	@Override
	public String sendCodeToMail(User user) {	        
		System.out.println("::: UserServluceImpl - sendCodeToMail :::");
		
		String randNum = ""+(int)(Math.random()*100000)+1;
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	    
	    simpleMailMessage.setTo(user.getUserId()); 
	    simpleMailMessage.setSubject("코드블루 인증코드입니다. 💙");
	    simpleMailMessage.setText(user.getUserName()+"님! 인증코드는  ["+randNum+"]입니다. <br> 인증코드 입력란에 입력해주셔야 회원가입이 완료 됩니다! :) <br> 감사합니다.");
	    System.out.println("인증번호: " + randNum);
	    
	    javaMailSender.send(simpleMailMessage);
	    return randNum;
	}

	@Override
	public int addExpert(Expert expert) {
		System.out.println("::: UserServluceImpl - addExpert :::"); 
		return userMapper.insertExpert(expert);
	}

	@Override
	public Manager verifyManager(User user) {
		System.out.println("::: UserServluceImpl - verifyManager :::"); 
		return userMapper.selectManagerId(user);
	}

	@Override
	public String resetPassword(User user) {
		System.out.println("::: UserServluceImpl - resetPassword :::"); 
		System.out.println(user.toString());
		
		if(userMapper.selectUserId(user) == null) {
			return "noSuchUser";
		}
		
		String randNum = sendCodeToMail(user);
		return randNum;
	}

	@Override
	public String getUserIdForCheck(User user) {
		System.out.println("::: UserServluceImpl - verifyUserForReset :::");
		
		if(userMapper.selectUserIdForCheck(user) == null) {
			return null;
		}
		
		String randNum = sendCodeToMail(user);
		return randNum;
	}

	@Override
	public int modifyUserPw(User user) {
		System.out.println("::: UserServluceImpl - modifyUserPw :::");
		return userMapper.updateUserPw(user);
	}

}