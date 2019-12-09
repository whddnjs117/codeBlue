package com.example.codeblue.test.jjw;

import java.util.List;

import com.example.codeblue.vo.Feild;
import com.example.codeblue.vo.Page;
import com.example.codeblue.vo.QuestionBoard;

@org.apache.ibatis.annotations.Mapper
public interface JJWMapper {
	
	
	//삭제된 게시글 전체행수
	public int selectWithdrawQuestionBoardTotalCount(Page page);
	//삭제된 게시글 가져오기
	public List<QuestionBoard> selectWithdrawQuestionBoardList(Page page);
	//삭제한질문 테이블 옮기고 삭제
	public void deleteQuestionBoard(List<String> list);
	//질문에 해당하는 quesiton_board_img 삭제
	public void deleteQuestionBoardImg(List<String> list);
	//질문에 해당하는 question_comment 삭제
	public void deleteQuestionComment(List<String> list);
	//질문에 해당하는 question_vote 삭제
	public void deleteQuestionVote(List<String> list);
	//질문 지우는조건에 해당하는 answer 삭제 
	public void deleteQuestionBoardAnswer(List<String> list);
	//질문 지우는조건에 해당하는 answer_comment 삭제 
	public void deleteQuestionBoardAnswerComment(List<String> list);
	//질문 지우는조건에 해당하는 answer_vote 삭제
	public void deleteQuestionBoardAnswerVote(List<String> list);
	//Id 값으로 가져온 전체값을 다른 테이블에 저장
	public void insertWithdrawQuestionBoard(List<QuestionBoard> list);
	//질문에 해당되는 답변 Id 값 가져오기
	public List<String> selectQuestionBoardAnswerList(List<String> list);
	//질문Id 값으로 전체값 가져오기(삭제를 위한)
	public List<QuestionBoard> selectQuestionBoardCheckList(List<String> list);
//	public List<Feild> selectFeildList();
//	public int selectQuestionBoardTotalCount(Page page);
//	public List<QuestionBoard> selectQuestionBoardList(Page page);
}
